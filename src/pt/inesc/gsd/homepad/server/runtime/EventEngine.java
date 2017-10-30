package pt.inesc.gsd.homepad.server.runtime;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.server.Event;
import pt.inesc.gsd.homepad.server.GlobalEvent;
import pt.inesc.gsd.homepad.server.GlobalEventDispatcher;
import pt.inesc.gsd.homepad.server.GlobalEventGenerator;

public class EventEngine implements GlobalEventDispatcher {
	
	private static final Logger LOGGER = Logger.getLogger(EventEngine.class);

	// the bounded buffer where pending events are kept to be dispatched
	private static int MAX_PENDING_EVENTS = 100;
	private BoundedEventBuffer _eventQueue = 
			new BoundedEventBuffer(MAX_PENDING_EVENTS);
	
	// the internal thread responsible for dispatching internal events
	private Thread _looper = null;
	private boolean _running = false;
	
	// interface for sending outgoing global events
	private GlobalEventGenerator _outSource = null;		// the app runtime service
	private GlobalEventDispatcher _outDispatcher = null;	// the global event bus

	// interface for handling
	private LocalEventDispatcher _inDispatcher = new LocalEventDispatcher();

	
	/*
	 * synchronized bounded buffer for handling events
	 */
	
	class BoundedEventBuffer {
		
	    private final Event[] buffer;
	    private final int capacity;

	    private int front;
	    private int rear;
	    private int count;

	    private final Lock lock = new ReentrantLock();

	    private final Condition notFull = lock.newCondition();
	    private final Condition notEmpty = lock.newCondition();

	    public BoundedEventBuffer(int capacity) {

	        this.capacity = capacity;

	        buffer = new Event[capacity];
	    }

	    public void blockingPut(Event event) throws InterruptedException {
	        lock.lock();

	        try {
	            while (count == capacity) {
	                notFull.await();
	            }

	            buffer[rear] = event;
	            rear = (rear + 1) % capacity;
	            count++;

	            notEmpty.signal();
	        } finally {
	            lock.unlock();
	        }
	    }

	    public int unblockingPut(Event event) {
	        lock.lock();

	        try {
	            if (count == capacity) {
	                return -1;
	            }

	            buffer[rear] = event;
	            rear = (rear + 1) % capacity;
	            count++;

	            notEmpty.signal();
	        } finally {
	            lock.unlock();
	        }
	        return 0;
	    }
	    
	    public Event blockingGet() throws InterruptedException {
	        lock.lock();

	        try {
	            while (count == 0) {
	                notEmpty.await();
	            }

	            Event result = buffer[front];
	            front = (front + 1) % capacity;
	            count--;

	            notFull.signal();

	            return result;
	        } finally {
	            lock.unlock();
	        }
	    }
	}
	
	protected void handleQueueOverflow(Event event) {
		LOGGER.log(Logger.ER, "Queue overflow: lost event '" +
				event.getName() + "'.");
	}

	/*
	 *  allow elements to send internal layout events and outgoing global events
	 */
	
	public void registerEventDispatcher(GlobalEventDispatcher dispatcher) {
		_outDispatcher = dispatcher;
	}

	class LocalEventDispatcher {
		
		public void dispatchLayoutEvent(LayoutEvent event) {
			if (_eventQueue.unblockingPut(event) < 0) {
				handleQueueOverflow(event);
			}
		}
	
		public void dispatchOutgoingGlobalEvent(GlobalEvent event) {
			if (event != null && _outDispatcher != null) {
				_outDispatcher.handleGlobalEvent(_outSource, event);
			}
		}
	}

	// returns reference to local dispatcher to the layout elements
	LocalEventDispatcher getLocalEventDispatcher() {
		return _inDispatcher;
	}

	@Override
	public void handleGlobalEvent(GlobalEventGenerator source, GlobalEvent event) {
		if (_eventQueue.unblockingPut(event) < 0) {
			handleQueueOverflow(event);
		}
	}

	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		// for now, accept all global events directed toward the application server
		return true;
	}
	
	/*
	 * event processing methods
	 */
	
	public void startProcessingLoop(GlobalEventGenerator outSource, 
			GlobalEventDispatcher outDispatcher) {
		
		_outSource = outSource;
		_outDispatcher = outDispatcher;
		_running = true;
		_looper = new Thread() {
		    public void run() {
	        	while(_running) {
	        		Event current = null;
			        try {
			        	current = _eventQueue.blockingGet();
			        } catch(InterruptedException v) {
			        	return;
			        }
		        	if (current instanceof LayoutEvent) {
		        		LayoutEvent event = (LayoutEvent)current;
		        		for (LayoutElement e : event.getDestination().getElements()) {
		        			e.handleLayoutEvent(event);
		        		}
		        	} else if (current instanceof GlobalEvent) {
		        		GlobalEvent event = (GlobalEvent)current;
		        		for (GlobalEventDispatcher e : event.getReceivers()) {
		        			e.handleGlobalEvent(null, event);
		        		}
		        	}
	        	}
		    }
		};
		_looper.start();
	}

	public void stopProcessingLoop() {
		_running = false;
		_looper.interrupt();
		try {
			_looper.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		_looper = null;
	}
}
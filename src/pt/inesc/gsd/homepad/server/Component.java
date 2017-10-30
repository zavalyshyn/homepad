package pt.inesc.gsd.homepad.server;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pt.inesc.gsd.homepad.Logger;

public abstract class Component implements GlobalEventDispatcher, GlobalEventGenerator {
	
	private static final Logger LOGGER = Logger.getLogger(Component.class);

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
	
	private void handleQueueOverflow(Event event) {
		LOGGER.log(Logger.ER, "Queue overflow: lost event '" +
				event.getName() + "'.");
	}

	public void initBus(Bus bus) {
		_outDispatcher = bus;
		_outSource = this;
	}
	
	/*
	 *  allow elements to send internal layout events and outgoing global events
	 */
	
	protected void sendOutgoingEvent(GlobalEvent event) {
		if (event != null && _outDispatcher != null) {
			_outDispatcher.handleGlobalEvent(_outSource, event);
		}
	}
	
	@Override
	public void handleGlobalEvent(GlobalEventGenerator source, GlobalEvent event) {
		if (_eventQueue.unblockingPut(event) < 0) {
			handleQueueOverflow(event);
		}
	}
	
	/*
	 * event processing methods
	 */

	public void enable() {
		enabling();
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
			        handleIncomingEvent((GlobalEvent) current);
	        	}
		    }
		};
		_looper.start();
	}

	public void disable() {
		_running = false;
		_looper.interrupt();
		try {
			_looper.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		_looper = null;
		disabling();
	}
	
	/*
	 * methods to be overridden by subclasses
	 */

	/*
	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		// for now, accept all global events directed toward this component
		return true;
	}
	*/
	
	protected void enabling() {};
 
	protected abstract void handleIncomingEvent(GlobalEvent event);

	protected void disabling() {};

}

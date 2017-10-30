package pt.inesc.gsd.homepad.server.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;

import pt.inesc.gsd.homepad.Registry;
import pt.inesc.gsd.homepad.annotations.BodyAppLayoutElement;
import pt.inesc.gsd.homepad.AppElements;
import pt.inesc.gsd.homepad.AppManifest;
import pt.inesc.gsd.homepad.Layout;
import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.server.Bus;
import pt.inesc.gsd.homepad.server.GlobalEvent;
import pt.inesc.gsd.homepad.server.GlobalEventDispatcher;
import pt.inesc.gsd.homepad.server.GlobalEventGenerator;

public class AppRuntime implements GlobalEventGenerator {

	private static final Logger LOGGER = Logger.getLogger(AppRuntime.class);

    private Bus _bus;
    private AppManifest _appManifest;
	private Registry _registry;

    private EventEngine _engine = new EventEngine();
	private boolean _appDeployed = false;
    private Hashtable<Integer, LayoutElement> _elements = 
    		new Hashtable<Integer, LayoutElement>();

    public AppRuntime(Bus bus, Registry registry, AppManifest appManifest) {
    	_bus = bus;
    	_registry = registry;
    	_appManifest = appManifest;
    }
    
	public int loadApp(String appname) {
		
		// instantiate the app classe name
		AppElements app = null;
		try {
			Class<?> appClass;
			appClass = Class.forName(appname);
			Constructor<?> ctor = appClass.getConstructor();
			app = (AppElements) ctor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // tell the app to load the layout specification
        //app.initLayout(_layout);
		Layout layout = _appManifest.getLayout();

        /*
         * instantiate the layout into the graph of interconnected elements
         */
        
		// look for the methods of the application that are annotated to be elements
		Hashtable<String, Method> handlerTable = new Hashtable<String, Method>();
	    Method[] methods = app.getClass().getMethods();
        for (Method method : methods) {
        	BodyAppLayoutElement annos = 
        			method.getAnnotation(BodyAppLayoutElement.class);
            if (annos != null) {
                try {
                	handlerTable.put(annos.name(), method);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
		
        // instantiate objects for each element
		int id = 0;
        for (String nodeName : layout.getNodes()) {
        	LayoutElement el = null;
        	if (_registry.isNativeElement(nodeName)) {
				el = _registry.getNewElementInstance(nodeName);
			} else if (handlerTable.keySet().contains(nodeName)) {
        		el = new AppLayoutElement(nodeName, handlerTable.get(nodeName), app);
        	} else {
        		LOGGER.log(Logger.ER, "Error: element '" + nodeName + "' was not found" );
        		return -1;
        	}
        	_elements.put((++id), el);
        }
        
        // set up the connections between elements of the layout
        for (Integer from : _elements.keySet()) {
        	LayoutConnector downstream = new LayoutConnector();
        	ArrayList<Integer> edges = layout.getNodeEdges(from);
        	if (edges != null) {
	        	for(Integer to : edges) {
	        		downstream.addElement(_elements.get(to));
	        	}
        	}
        	LayoutElement el = _elements.get(from);
        	el.initialize(from, _engine.getLocalEventDispatcher(), downstream);
        }
        
        return 0;
	}
	
	public int enable() {
		if (_appDeployed) {
			return -1;
		}
		_engine.startProcessingLoop(this, _bus);
		_appDeployed = true;
		return 0;
	}

	public int disable() {
		if (!_appDeployed) {
			return -1;
		}
		_engine.stopProcessingLoop();
		return 0;
	};
	
	public int emulateNewEvent(GlobalEvent event) {
		if (!_appDeployed || event == null) {
			return -1;
		}

		// before forwarding the event to the event engine, must define the receivers
		ArrayList<GlobalEventDispatcher> receivers = event.getReceivers();
		receivers.clear();

		int receiverId = event.getDestElementId();
		if (receiverId != 0) {
			// if the destination id is specified, forward the event to it
			LayoutElement el = _elements.get(receiverId);
			if (el == null) {
        		LOGGER.log(Logger.ER, "Unknown element id '"+ receiverId + "'.");
				return -2;
			}
			if (!(el instanceof NativeLayoutElement)) {
				LOGGER.log(Logger.ER, "Element cannot receive global events.");
				return -3;
			}
			if (!((NativeLayoutElement)el).canHandleEventType(event.getClass())) {
				LOGGER.log(Logger.ER, "Element does not handle'" + 
						event.getClass().getSimpleName() + "' events.");
				return -4;
			}
			receivers.add((NativeLayoutElement)el);
		} else {
			// the destination id is not specified, so forward the event based on class
			for (LayoutElement el : _elements.values()) {
				if (el instanceof NativeLayoutElement &&
					((NativeLayoutElement)el).canHandleEventType(event.getClass())) {
					receivers.add((NativeLayoutElement)el);
				}
			}
		}
		if (!receivers.isEmpty()) {
			_engine.handleGlobalEvent(null, event);
		}
		return 0;
	};
}

package pt.inesc.gsd.homepad.server.runtime;

import java.lang.reflect.Method;

import pt.inesc.gsd.homepad.AppElements;
import pt.inesc.gsd.homepad.Logger;

public class AppLayoutElement extends LayoutElement {

	private static final Logger LOGGER = Logger.getLogger(AppLayoutElement.class);

	private String _name;
	private Method _handler;
	private AppElements _app;
	
	public AppLayoutElement(String name, Method handler, AppElements app) {

		_name = name;

		// instantiate the app class with its independent class loader
		String appClassName = app.getClass().getName();
		String appClassPath = app.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
	    ClassLoader parentClassLoader = AppElementClassLoader.class.getClassLoader();
	    AppElementClassLoader classLoader = new AppElementClassLoader(parentClassLoader, appClassName, appClassPath);
	    AppElements appInstance = null;
	    try {
			Class<?> appClass = classLoader.loadClass(appClassName);
			appInstance = (AppElements) appClass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		_app = appInstance;
		
	    // look for the method handler
		String methodName = handler.toGenericString();
	    Method[] methods = appInstance.getClass().getMethods();
        for (Method method : methods) {
        	if (method.toGenericString().equals(methodName)) {
        		_handler = method;
        		break;
        	}
        }
	}
	
	@Override
	public String getName() {
		return _name;
	}
	
	@Override
	public void sendEventToPort(LayoutEvent event, int portNo) {
		super.sendEventToPort(event, portNo);
	}

	@Override
	public void handleLayoutEvent(LayoutEvent event) {		
		try {
			LOGGER.log(Logger.LE, "Received: " + event.getName());			
            _handler.invoke(_app, this, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

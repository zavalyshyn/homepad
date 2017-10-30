package pt.inesc.gsd.homepad.server;

import java.util.Hashtable;

import pt.inesc.gsd.homepad.Module;
import pt.inesc.gsd.homepad.server.runtime.AppRuntime;

public class Bus implements GlobalEventDispatcher {

	private Hashtable<Module,Component> _services = new Hashtable<Module,Component>();
	private AppRuntime _appRuntime;

	public void registerService(Module module, Component service) {
		_services.put(module,service);
	}

	public void registerAppRuntime(AppRuntime appService) {
		_appRuntime = appService;
	}

	@Override
	public void handleGlobalEvent(GlobalEventGenerator source, GlobalEvent event) {

		if (!(source instanceof AppRuntime)) {
			_appRuntime.emulateNewEvent(event);
			return;
		}

		for (Component service : _services.values()) {
			if (service.canHandleEventType(event.getClass())) {
				service.handleGlobalEvent(null, event);
			}
		}		
	}

	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		return true;
	}
}
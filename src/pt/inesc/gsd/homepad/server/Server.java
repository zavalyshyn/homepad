package pt.inesc.gsd.homepad.server;

import java.util.ArrayList;

import pt.inesc.gsd.homepad.AppManifest;
import pt.inesc.gsd.homepad.Module;
import pt.inesc.gsd.homepad.Registry;
import pt.inesc.gsd.homepad.server.runtime.AppRuntime;

public class Server {

	private Bus _bus = new Bus();
	private ArrayList<Component> _components = new ArrayList<Component>();
	private AppManifest _appManifest;
	private AppRuntime _appRuntime;

	public void initialize(Registry registry, AppManifest appManifest) {
		for (Module module : registry.getModules()) {
			Component service = module.getServiceInstance();
			service.initBus(_bus);
			_bus.registerService(module, service);
			_components.add(service);
		}

		_appRuntime = new AppRuntime(_bus, registry, appManifest);
		_bus.registerAppRuntime(_appRuntime);
		_appManifest = appManifest;
		_appRuntime.loadApp(_appManifest.getAppElementClassName());
	}
	
	public void enable() {
		_appRuntime.enable();
		for (Component service : _components) {
			service.enable();
		}
	}

	public void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void disable() {
		for (Component service : _components) {
			service.disable();
		}
	}
}

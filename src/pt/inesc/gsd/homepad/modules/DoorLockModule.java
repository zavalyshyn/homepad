package pt.inesc.gsd.homepad.modules;

import pt.inesc.gsd.homepad.Module;
import pt.inesc.gsd.homepad.modules.elements.DoorLockReadElement;
import pt.inesc.gsd.homepad.modules.elements.DoorLockUnlockElement;
import pt.inesc.gsd.homepad.modules.services.DoorLockService;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;

public class DoorLockModule extends Module {
	
	public DoorLockModule() {
		NativeLayoutElement el = new DoorLockReadElement();
		_elements.put(el.getName(), el);
		el = new DoorLockUnlockElement();
		_elements.put(el.getName(), el);
	}

	@Override
	public String getModuleName() {
		return "DoorLock";
	}

	@Override
	public String getDescription() {
		return "Provides an interface to the door lock.";
	}

	@Override
	public Component getServiceInstance() {
		return new DoorLockService();
	}
}

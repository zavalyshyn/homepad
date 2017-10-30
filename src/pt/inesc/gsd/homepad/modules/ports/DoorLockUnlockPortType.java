package pt.inesc.gsd.homepad.modules.ports;

import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class DoorLockUnlockPortType extends LayoutEvent implements PortType {

	private boolean _success;
	
	public DoorLockUnlockPortType(boolean success) {
		_success = success;
	}

	public boolean getStatus() {
		return _success;
	}
}
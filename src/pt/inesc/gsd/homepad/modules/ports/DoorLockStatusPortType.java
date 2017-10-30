package pt.inesc.gsd.homepad.modules.ports;

import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class DoorLockStatusPortType extends LayoutEvent implements PortType {

	public static final int DOOR_LOCK_LOCKED = 1;
	public static final int DOOR_LOCK_UNLOCKED = 2;
	
	private int _doorStatus;
	
	public DoorLockStatusPortType(int doorStatus) {
		_doorStatus = doorStatus;
	}

	public int getDoorLockStatus() {
		return _doorStatus;
	}
}

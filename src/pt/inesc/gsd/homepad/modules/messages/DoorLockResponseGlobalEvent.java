package pt.inesc.gsd.homepad.modules.messages;

import pt.inesc.gsd.homepad.server.GlobalEvent;

public class DoorLockResponseGlobalEvent extends GlobalEvent {

	public static final int DOOR_LOCK_LOCKED = 1;
	public static final int DOOR_LOCK_UNLOCKED = 2;
	
	private int _doorStatus;
	private DoorLockRequestGlobalEvent _request;
	private boolean _success;

	public DoorLockResponseGlobalEvent(DoorLockRequestGlobalEvent request,
			int sourceElementId, int doorStatus) {
		setDestElementId(sourceElementId);
		_request = request;
		_doorStatus = doorStatus;
	}

	public DoorLockResponseGlobalEvent(DoorLockRequestGlobalEvent request,
			int sourceElementId, boolean success) {
		setDestElementId(sourceElementId);
		_request = request;
		_success = success;
	}

	public DoorLockRequestGlobalEvent getRequest() {
		return _request;
	}
	
	public int doorStatus() {
		return _doorStatus;
	}

	public boolean unlockSucceded() {
		return _success;
	}
}

package pt.inesc.gsd.homepad.modules.messages;

import pt.inesc.gsd.homepad.server.GlobalEvent;

public class DoorLockRequestGlobalEvent extends GlobalEvent {

	public static final int READ_STATUS = 1;
	public static final int UNLOCK = 2;
	
	private int _operation;
	
	public DoorLockRequestGlobalEvent(int sourceElementId, int operation) {
		setSourceElementId(sourceElementId);
		_operation = operation;
	}
	
	public int getOperation() {
		return _operation;
	}
	
}

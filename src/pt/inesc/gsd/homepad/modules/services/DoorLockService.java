package pt.inesc.gsd.homepad.modules.services;

import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.modules.messages.DoorLockRequestGlobalEvent;
import pt.inesc.gsd.homepad.modules.messages.DoorLockResponseGlobalEvent;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.GlobalEvent;

public class DoorLockService extends Component {

	private static final Logger LOGGER = Logger.getLogger(DoorLockService.class);

	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		if (eventType.equals(DoorLockRequestGlobalEvent.class)) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleIncomingEvent(GlobalEvent event) {
		LOGGER.log(Logger.GE, "Received: " + event.getName());

		if (event instanceof DoorLockRequestGlobalEvent) {
			DoorLockRequestGlobalEvent ein = (DoorLockRequestGlobalEvent) event;
			int sourceId = ein.getSourceElementId();

			DoorLockResponseGlobalEvent eout;
			switch(ein.getOperation()) {
			
			case DoorLockRequestGlobalEvent.READ_STATUS:
				eout = new DoorLockResponseGlobalEvent(
						ein, sourceId, DoorLockResponseGlobalEvent.DOOR_LOCK_LOCKED);
				sendOutgoingEvent(eout);
				break;

			case DoorLockRequestGlobalEvent.UNLOCK:
				eout = new DoorLockResponseGlobalEvent(
						ein, sourceId, true);
				sendOutgoingEvent(eout);
				break;
			}
		}
	}
}
package pt.inesc.gsd.homepad.modules.services;

import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.modules.messages.CloudCallRequestGlobalEvent;
import pt.inesc.gsd.homepad.modules.messages.CloudCallResponseGlobalEvent;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.GlobalEvent;

public class CloudCallService extends Component {

	private static final Logger LOGGER = Logger.getLogger(CloudCallService.class);

	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		if (eventType.equals(CloudCallRequestGlobalEvent.class)) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleIncomingEvent(GlobalEvent event) {
		LOGGER.log(Logger.GE, "Received: " + event.getName());
		if (event instanceof CloudCallRequestGlobalEvent) {
			CloudCallRequestGlobalEvent ein = (CloudCallRequestGlobalEvent) event;
			int sourceId = ein.getSourceElementId();

			// TODO: need to make the web service call
			
			CloudCallResponseGlobalEvent eout = new CloudCallResponseGlobalEvent(
						ein, sourceId, true);			
			sendOutgoingEvent(eout);
		}
	}
}
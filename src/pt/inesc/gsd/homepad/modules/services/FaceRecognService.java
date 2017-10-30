package pt.inesc.gsd.homepad.modules.services;

import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.modules.messages.FaceRecognRequestGlobalEvent;
import pt.inesc.gsd.homepad.modules.messages.FaceRecognResponseGlobalEvent;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.GlobalEvent;

public class FaceRecognService extends Component {

	private static final Logger LOGGER = Logger.getLogger(FaceRecognService.class);

	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		if (eventType.equals(FaceRecognRequestGlobalEvent.class)) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleIncomingEvent(GlobalEvent event) {
		LOGGER.log(Logger.GE, "Received: " + event.getName());

		if (event instanceof FaceRecognRequestGlobalEvent) {
			// parse input parameters
			FaceRecognRequestGlobalEvent ein = (FaceRecognRequestGlobalEvent) event;
			int sourceId = ein.getSourceElementId();
			
			// TODO: process the frame ein.getFrame()

			// send out the response, assuming the user was recognized
			FaceRecognResponseGlobalEvent eout = new FaceRecognResponseGlobalEvent(
					ein, sourceId, true, new int[10]);
			sendOutgoingEvent(eout);
		}
	}
}

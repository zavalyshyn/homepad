package pt.inesc.gsd.homepad.apps;

import pt.inesc.gsd.homepad.AppElements;
import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.annotations.BodyAppLayoutElement;
import pt.inesc.gsd.homepad.modules.ports.DoorLockStatusPortType;
import pt.inesc.gsd.homepad.modules.ports.NullPortType;
import pt.inesc.gsd.homepad.server.runtime.AppLayoutElement;
import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;

public class FaceDoorApp extends AppElements {

	private static final Logger LOGGER = Logger.getLogger(FaceDoorApp.class);

	@BodyAppLayoutElement(name = "FaceDoorBegin")
	public void handleFaceDoorBegin(AppLayoutElement el, LayoutEvent event) {
		LOGGER.log(Logger.AP, "Invoked: FaceDoorBegin.");
		el.sendEventToPort(new NullPortType(), 0);
	}
	
	@BodyAppLayoutElement(name = "FaceDoorCheck")
	public void handleFaceDoorCheck(AppLayoutElement el, LayoutEvent event) {
		LOGGER.log(Logger.AP, "Invoked: FaceDoorCheck.");
		if (event instanceof DoorLockStatusPortType) {
			DoorLockStatusPortType e = (DoorLockStatusPortType) event;
			if (e.getDoorLockStatus() == DoorLockStatusPortType.DOOR_LOCK_LOCKED) {
				el.sendEventToPort(new NullPortType(), 0);
			}
		}
	}

	@BodyAppLayoutElement(name = "FaceDoorEnd")
	public void handleFaceDoorEnd(AppLayoutElement el, LayoutEvent event) {
		LOGGER.log(Logger.AP, "Invoked: FaceDoorEnd.");
	}
}

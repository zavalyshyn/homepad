package pt.inesc.gsd.homepad.apps;

import pt.inesc.gsd.homepad.AppElements;
import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.annotations.BodyAppLayoutElement;
import pt.inesc.gsd.homepad.modules.ports.CloudRequestMultipartPortType;
import pt.inesc.gsd.homepad.modules.ports.FramePortType;
import pt.inesc.gsd.homepad.server.runtime.AppLayoutElement;
import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;

public class HelloWorldApp extends AppElements{
	
	private static final Logger LOGGER = Logger.getLogger(HelloWorldApp.class);
	
	@BodyAppLayoutElement(name = "HelloWorldBegin")
	public void handleHelloWorldBegin(AppLayoutElement el, LayoutEvent event) {
		LOGGER.log(Logger.AP, "Invoked: HelloWorldBegin.");
		if (event instanceof FramePortType) {
			FramePortType e = (FramePortType) event;
			el.sendEventToPort(new CloudRequestMultipartPortType("http://146.193.41.153:8090", true, e.getFrame()), 0);
		}
	}
	
//	@BodyAppLayoutElement(name = "HelloWorldCheck")
//	public void handleHelloWorldCheck(AppLayoutElement el, LayoutEvent event) {
//		LOGGER.log(Logger.AP, "Invoked: HelloWorldCheck.");
//		if (event instanceof DoorLockStatusPortType) {
//			DoorLockStatusPortType e = (DoorLockStatusPortType) event;
//			if (e.getDoorLockStatus() == DoorLockStatusPortType.DOOR_LOCK_LOCKED) {
//				el.sendEventToPort(new NullPortType(), 0);
//			}
//		}
//	}

	@BodyAppLayoutElement(name = "HelloWorldEnd")
	public void handleHelloWorldEnd(AppLayoutElement el, LayoutEvent event) {
		LOGGER.log(Logger.AP, "Invoked: HelloWorldEnd.");
	}

}

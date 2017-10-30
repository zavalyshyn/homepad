package pt.inesc.gsd.homepad.modules.elements;

import java.util.ArrayList;

import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.modules.messages.FromCameraGlobalEvent;
import pt.inesc.gsd.homepad.modules.ports.FramePortType;
import pt.inesc.gsd.homepad.server.GlobalEvent;
import pt.inesc.gsd.homepad.server.GlobalEventGenerator;
import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class FromCameraElement extends NativeLayoutElement {

	private static final Logger LOGGER = Logger.getLogger(FromCameraElement.class);
	
	@Override
	public String getName() {
		return "FromCamera";
	}

	@Override
	public NativeLayoutElement newElement() {
		return new FromCameraElement();
	}
	
	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		if (eventType.equals(FromCameraGlobalEvent.class)) {
			return true;
		}
		return false;
	}

	@Override
	public void handleLayoutEvent(LayoutEvent event) {
		LOGGER.log(Logger.LE, "Unexpected: " + event.getName());
	}
	
	@Override
	public void handleGlobalEvent(GlobalEventGenerator source, GlobalEvent event) {
		LOGGER.log(Logger.GE, "Received: " + event.getName());
		if (event instanceof FromCameraGlobalEvent) {
			FromCameraGlobalEvent e = (FromCameraGlobalEvent) event;
			sendEventToPort(new FramePortType(e.getFrame()), 0);
		}
	}
	
	@Override
	public String getDescription() {
		return "Provides bitmap frame data obtained from the camera device.";
	}
	
	@Override
	public int getNumberInPorts() {
		return 0;
	}

	@Override
	public ArrayList<Class<? extends PortType>> getTypeInPorts() {
		return new ArrayList<Class<? extends PortType>>();
	}

	@Override
	public int getNumberOutPorts() {
		return 1;
	}

	@Override
	public ArrayList<Class<? extends PortType>> getTypeOutPorts() {
		ArrayList<Class<? extends PortType>> outPorts = 
				new ArrayList<Class<? extends PortType>>();
		outPorts.add(FramePortType.class);
		return outPorts;
	}

	@Override
	public String getOutData() {
		return "output(el(fromcamera), [ data(frame) ]).";
	}
}
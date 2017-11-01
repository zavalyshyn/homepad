package pt.inesc.gsd.homepad.modules.elements;

import java.util.ArrayList;

import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.modules.messages.ImageBlurrerRequestGlobalEvent;
import pt.inesc.gsd.homepad.modules.messages.ImageBlurrerResponseGlobalEvent;
import pt.inesc.gsd.homepad.modules.ports.FramePortType;
import pt.inesc.gsd.homepad.modules.ports.ImageBlurrerPortType;
import pt.inesc.gsd.homepad.server.GlobalEvent;
import pt.inesc.gsd.homepad.server.GlobalEventGenerator;
import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class ImageBlurrerElement extends NativeLayoutElement {
	
	private static final Logger LOGGER = Logger.getLogger(ImageBlurrerElement.class);
	
	@Override
	public String getName() {
		return "ImageBlurrer";
	}

	@Override
	public NativeLayoutElement newElement() {
		return new ImageBlurrerElement();
	}

	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		if (eventType.equals(ImageBlurrerResponseGlobalEvent.class)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void handleLayoutEvent(LayoutEvent event) {
		LOGGER.log(Logger.LE, "Received: " + event.getName());
		if (event instanceof FramePortType) {
			FramePortType e = (FramePortType) event;
			this.sendGlobalEvent(new ImageBlurrerRequestGlobalEvent(getId(), e.getFrame()));
		}
	}

	@Override
	public void handleGlobalEvent(GlobalEventGenerator source, GlobalEvent event) {
		LOGGER.log(Logger.GE, "Received: " + event.getName());
		if (event instanceof ImageBlurrerResponseGlobalEvent) {
			ImageBlurrerResponseGlobalEvent e = (ImageBlurrerResponseGlobalEvent) event;
			sendEventToPort(new ImageBlurrerPortType(e.blurFrame()), 0);
		}
	}
	
	@Override
	public String getDescription() {
		return "Runs image blurrer algorithm to blur the image from the camera.";
	}

	@Override
	public int getNumberInPorts() {
		return 1;
	}

	@Override
	public ArrayList<Class<? extends PortType>> getTypeInPorts() {
		ArrayList<Class<? extends PortType>> inPorts = 
				new ArrayList<Class<? extends PortType>>();
		inPorts.add(FramePortType.class);
		return inPorts;
	}

	@Override
	public int getNumberOutPorts() {
		return 1;
	}

	@Override
	public ArrayList<Class<? extends PortType>> getTypeOutPorts() {
		ArrayList<Class<? extends PortType>> outPorts = 
				new ArrayList<Class<? extends PortType>>();
		outPorts.add(ImageBlurrerPortType.class);
		return outPorts;
	}

	@Override
	public String getOutData() {
		return "output(el(imageblurrer), [ data(X) ]) " 
        		+ ":- input(el(imageblurrer), Y), member(data(Z), Y), X = f(Z).";
	}
}

package pt.inesc.gsd.homepad.modules.elements;

import pt.inesc.gsd.homepad.server.GlobalEvent;
import pt.inesc.gsd.homepad.server.GlobalEventGenerator;
import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;
import pt.inesc.gsd.homepad.server.runtime.PortType;

import java.util.ArrayList;

import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.modules.messages.FaceRecognRequestGlobalEvent;
import pt.inesc.gsd.homepad.modules.messages.FaceRecognResponseGlobalEvent;
import pt.inesc.gsd.homepad.modules.ports.FaceRecognPortType;
import pt.inesc.gsd.homepad.modules.ports.FramePortType;

public class FaceRecognElement extends NativeLayoutElement {

	private static final Logger LOGGER = Logger.getLogger(FaceRecognElement.class);

	@Override
	public String getName() {
		return "FaceRecogn";
	}

	@Override
	public NativeLayoutElement newElement() {
		return new FaceRecognElement();
	}
	
	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		if (eventType.equals(FaceRecognResponseGlobalEvent.class)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void handleLayoutEvent(LayoutEvent event) {
		LOGGER.log(Logger.LE, "Received: " + event.getName());
		if (event instanceof FramePortType) {
			FramePortType e = (FramePortType) event;
			this.sendGlobalEvent(new FaceRecognRequestGlobalEvent(getId(), e.getFrame()));
		}
	}

	@Override
	public void handleGlobalEvent(GlobalEventGenerator source, GlobalEvent event) {
		LOGGER.log(Logger.GE, "Received: " + event.getName());
		if (event instanceof FaceRecognResponseGlobalEvent) {
			FaceRecognResponseGlobalEvent e = (FaceRecognResponseGlobalEvent) event;
			sendEventToPort(new FaceRecognPortType(e.faceRecognized(), e.getFeatures()), 0);
		}
	}

	@Override
	public String getDescription() {
		return "Runs face recognition algorithm to detect specific faces in input image.";
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
		outPorts.add(FaceRecognPortType.class);
		return outPorts;
	}

	@Override
	public String getOutData() {
		return "output(el(facerecogn), [ data(X) ]) " 
        		+ ":- input(el(facerecogn), Y), member(data(Z), Y), X = f(Z).";
	}
}
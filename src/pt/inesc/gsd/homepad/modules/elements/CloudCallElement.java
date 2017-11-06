package pt.inesc.gsd.homepad.modules.elements;

import java.util.ArrayList;

import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.modules.messages.CloudCallRequestGlobalEvent;
import pt.inesc.gsd.homepad.modules.messages.CloudCallResponseGlobalEvent;
import pt.inesc.gsd.homepad.modules.ports.CloudRequestMultipartPortType;
import pt.inesc.gsd.homepad.modules.ports.CloudResponsePortType;
import pt.inesc.gsd.homepad.modules.ports.NullPortType;
import pt.inesc.gsd.homepad.modules.ports.UrlParamPortType;
import pt.inesc.gsd.homepad.server.GlobalEvent;
import pt.inesc.gsd.homepad.server.GlobalEventGenerator;
import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class CloudCallElement extends NativeLayoutElement {

	private static final Logger LOGGER = Logger.getLogger(CloudCallElement.class);
	
	@Override
	public String getName() {
		return "CloudCall";
	}

	@Override
	public NativeLayoutElement newElement() {
		return new CloudCallElement();
	}

	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		if (eventType.equals(CloudCallResponseGlobalEvent.class)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void handleLayoutEvent(LayoutEvent event) {
		LOGGER.log(Logger.LE, "Received: " + event.getName());			
		if (event instanceof NullPortType) {
			this.sendGlobalEvent(new CloudCallRequestGlobalEvent(getId(), null));
		} else if (event instanceof UrlParamPortType) {
			UrlParamPortType e = (UrlParamPortType) event;
			this.sendGlobalEvent(new CloudCallRequestGlobalEvent(getId(), e.getParam()));
		} else if (event instanceof CloudRequestMultipartPortType) {
			CloudRequestMultipartPortType e = (CloudRequestMultipartPortType) event;
			this.sendGlobalEvent(new CloudCallRequestGlobalEvent(getId(), e.getUrl(), e.isMultipart(), e.getPayload()));
		}
	}

	@Override
	public void handleGlobalEvent(GlobalEventGenerator source, GlobalEvent event) {
		LOGGER.log(Logger.GE, "Received: " + event.getName());			
		if (event instanceof CloudCallResponseGlobalEvent) {
			CloudCallResponseGlobalEvent e = (CloudCallResponseGlobalEvent) event;
			//sendEventToPort(new NullPortType(), 0);
			sendEventToPort(new CloudResponsePortType(e.getResponse()), 0);
		}
	}

	@Override
	public String getDescription() {
		return "Makes a HTTP request to a specific web service.";
	}

	@Override
	public int getNumberInPorts() {
		return 1;
	}

	@Override
	public ArrayList<Class<? extends PortType>> getTypeInPorts() {
		ArrayList<Class<? extends PortType>> inPorts = 
				new ArrayList<Class<? extends PortType>>();
		inPorts.add(NullPortType.class);
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
		outPorts.add(NullPortType.class);
		return outPorts;
	}

	@Override
	public String getOutData() {
		return "output(el(cloudcall), []).";
	}
}

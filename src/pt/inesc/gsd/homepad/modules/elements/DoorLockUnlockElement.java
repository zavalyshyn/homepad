package pt.inesc.gsd.homepad.modules.elements;

import java.util.ArrayList;

import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.modules.messages.DoorLockRequestGlobalEvent;
import pt.inesc.gsd.homepad.modules.messages.DoorLockResponseGlobalEvent;
import pt.inesc.gsd.homepad.modules.ports.NullPortType;
import pt.inesc.gsd.homepad.server.GlobalEvent;
import pt.inesc.gsd.homepad.server.GlobalEventGenerator;
import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class DoorLockUnlockElement extends NativeLayoutElement {

	private static final Logger LOGGER = Logger.getLogger(DoorLockUnlockElement.class);

	@Override
	public String getName() {
		return "DoorLockUnlock";
	}

	@Override
	public NativeLayoutElement newElement() {
		return new DoorLockUnlockElement();
	}
	
	@Override
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		if (eventType.equals(DoorLockResponseGlobalEvent.class)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void handleLayoutEvent(LayoutEvent event) {
		LOGGER.log(Logger.LE, "Received: " + event.getName());
		if (event instanceof NullPortType) {
			this.sendGlobalEvent(new DoorLockRequestGlobalEvent(getId(), 
					DoorLockRequestGlobalEvent.UNLOCK));
		}
	}

	@Override
	public void handleGlobalEvent(GlobalEventGenerator source, GlobalEvent event) {
		LOGGER.log(Logger.GE, "Received: " + event.getName());
		if (event instanceof DoorLockResponseGlobalEvent) {
			sendEventToPort(new NullPortType(), 0);
		}
	}

	@Override
	public String getDescription() {
		return "Issues a command to unlock the door lock.";
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
		return "output(el(doorlockunlock), []).";
	}
}
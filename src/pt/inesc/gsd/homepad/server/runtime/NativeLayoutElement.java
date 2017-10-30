package pt.inesc.gsd.homepad.server.runtime;

import java.util.ArrayList;

import pt.inesc.gsd.homepad.server.GlobalEvent;
import pt.inesc.gsd.homepad.server.GlobalEventDispatcher;

public abstract class NativeLayoutElement extends LayoutElement 
	implements GlobalEventDispatcher {
	
	protected void sendGlobalEvent(GlobalEvent event) {
		_dispatcher.dispatchOutgoingGlobalEvent(event);
	}
	
	public boolean canHandleEventType(Class<? extends GlobalEvent> eventType) {
		return false;
	}
		
	public abstract NativeLayoutElement newElement();
	
	public abstract String getDescription();

	public abstract int getNumberInPorts();

	public abstract ArrayList<Class<? extends PortType>> getTypeInPorts();

	public abstract int getNumberOutPorts();

	public abstract ArrayList<Class<? extends PortType>> getTypeOutPorts();

	public abstract String getOutData();

	public void listSpecs() {
		System.out.println("Name          : " + this.getName());
		System.out.println("Description   : " + this.getDescription());
		System.out.println("In Ports      : " + this.getNumberInPorts());
		int i = 0;
		for (Class<? extends PortType> type : getTypeInPorts()) {
			System.out.println("In Port [" + (i++) + "]   : " + type.getSimpleName() + " ");
		}
		System.out.println("Out Ports     : " + this.getNumberOutPorts());
		for (Class<? extends PortType> type : getTypeOutPorts()) {
			System.out.println("Out Port [" + (i++) + "]  : " + type.getSimpleName() + " ");
		}
		System.out.println("Out Data Rule : " + getOutData());
	}
}
package pt.inesc.gsd.homepad.server.runtime;

import java.util.ArrayList;

public abstract class LayoutElement {

	private LayoutConnector _downstream;
	protected EventEngine.LocalEventDispatcher _dispatcher;
	private int _id;
	
	public abstract String getName();
	
	public void initialize(int id, EventEngine.LocalEventDispatcher dispatcher,
			LayoutConnector downstream) {
		_id = id;
		_downstream = downstream;
		_dispatcher = dispatcher;
	}
	
	protected void sendEventToPort(LayoutEvent event, int portNo) {
		event.setSource(this);

		ArrayList<LayoutElement> outPorts = _downstream.getElements();
		if (portNo >= outPorts.size() || portNo < -1) {
			System.out.println("Error: Event dropped - port number invalid.");
			return;
		}

		LayoutConnector receiver = null;
		if (portNo == -1) {
			// -1 to broadcast
			receiver = _downstream;
		} else {
			receiver = new LayoutConnector();
			receiver.addElement(outPorts.get(portNo));
		}

		event.setDestination(receiver);
		_dispatcher.dispatchLayoutEvent(event);
	}
	
	public int getId() {
		return _id;
	}
	
	public void handleLayoutEvent(LayoutEvent event) {
		System.out.println("[LayoutElement] NOP");		
	}
}

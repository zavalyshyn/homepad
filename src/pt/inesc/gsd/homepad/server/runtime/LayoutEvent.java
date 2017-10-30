package pt.inesc.gsd.homepad.server.runtime;

import pt.inesc.gsd.homepad.server.Event;

public abstract class LayoutEvent extends Event {
	
	private LayoutElement _source;
	private LayoutConnector _destination;

	public LayoutEvent() {
	}
	
	public LayoutEvent(LayoutElement source, LayoutConnector destination) {
		_source = source;
		_destination = destination;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	public void setSource(LayoutElement source) {
		_source = source;
	}
	
	public LayoutElement getSource() {
		return _source;
	}
	
	public void setDestination(LayoutConnector destination) {
		 _destination = destination;
	}

	public LayoutConnector getDestination() {
		return _destination;
	}
}

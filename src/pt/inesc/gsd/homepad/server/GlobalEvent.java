package pt.inesc.gsd.homepad.server;

import java.util.ArrayList;

public abstract class GlobalEvent extends Event {

	private int _destElementId = 0;
	private ArrayList<GlobalEventDispatcher> _receivers = 
			new ArrayList<GlobalEventDispatcher>();
	
	private int _sourceElementId = 0;

	public void setDestElementId(int destElementId) {
		_destElementId = destElementId;
	}
	
	public int getDestElementId() {
		return _destElementId;
	}
	
	public ArrayList<GlobalEventDispatcher> getReceivers() {
		return _receivers;
	}

	public void setSourceElementId(int sourceElementId) {
		_sourceElementId = sourceElementId;
	}
	
	public int getSourceElementId() {
		return _sourceElementId;
	}
	
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}

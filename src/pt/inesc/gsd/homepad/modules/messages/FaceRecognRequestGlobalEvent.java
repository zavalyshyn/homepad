package pt.inesc.gsd.homepad.modules.messages;

import pt.inesc.gsd.homepad.server.GlobalEvent;

public class FaceRecognRequestGlobalEvent extends GlobalEvent {

	private byte[] _data;
	
	public FaceRecognRequestGlobalEvent(int sourceElementId, byte[] data) {
		setSourceElementId(sourceElementId);
		_data = data;
	}
	
	public byte[] getFrame() {
		return _data;
	}	
}

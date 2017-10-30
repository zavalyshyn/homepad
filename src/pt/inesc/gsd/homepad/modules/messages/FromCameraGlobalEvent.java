package pt.inesc.gsd.homepad.modules.messages;

import pt.inesc.gsd.homepad.server.GlobalEvent;

public class FromCameraGlobalEvent extends GlobalEvent {

	private byte[] _data;
	
	public FromCameraGlobalEvent(byte[] data) {
		_data = data;
	}

	public byte[] getFrame() {
		return _data;
	}
}
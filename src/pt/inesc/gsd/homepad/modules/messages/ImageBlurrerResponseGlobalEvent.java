package pt.inesc.gsd.homepad.modules.messages;

import pt.inesc.gsd.homepad.server.GlobalEvent;

public class ImageBlurrerResponseGlobalEvent extends GlobalEvent {
	
	private byte[] _blurredFrame;
	private ImageBlurrerRequestGlobalEvent _request;
	
	public ImageBlurrerResponseGlobalEvent(ImageBlurrerRequestGlobalEvent request,
			int sourceElementId, byte[] blurredFrame) {
		setDestElementId(sourceElementId);
		_blurredFrame = blurredFrame;
		_request = request;
	}

	public ImageBlurrerRequestGlobalEvent getRequest() {
		return _request;
	}
	
	public byte[] blurFrame() {
		return _blurredFrame;
	}

}

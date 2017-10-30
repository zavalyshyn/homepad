package pt.inesc.gsd.homepad.modules.messages;

import pt.inesc.gsd.homepad.server.GlobalEvent;

public class FaceRecognResponseGlobalEvent extends GlobalEvent {

	private boolean _recognized;
	private int[] _features;
	private FaceRecognRequestGlobalEvent _request;
	
	public FaceRecognResponseGlobalEvent(FaceRecognRequestGlobalEvent request,
			int sourceElementId, boolean recognized, int[] features) {
		setDestElementId(sourceElementId);
		_recognized = recognized;
		_features = features;
		_request = request;
	}

	public FaceRecognRequestGlobalEvent getRequest() {
		return _request;
	}
	
	public boolean faceRecognized() {
		return _recognized;
	}
	
	public int[] getFeatures() {
		return _features;
	}
}

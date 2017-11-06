package pt.inesc.gsd.homepad.modules.messages;

import pt.inesc.gsd.homepad.server.GlobalEvent;

public class CloudCallResponseGlobalEvent extends GlobalEvent {

	private boolean _success;
	private CloudCallRequestGlobalEvent _request;
	private int _response;
	
	public CloudCallResponseGlobalEvent(CloudCallRequestGlobalEvent request,
			int sourceElementId, boolean success) {
		setDestElementId(sourceElementId);
		_success = success;
		_request = request;
	}
	
	public CloudCallResponseGlobalEvent(CloudCallRequestGlobalEvent request,
			int sourceElementId, boolean success, int response) {
		setDestElementId(sourceElementId);
		_success = success;
		_request = request;
		_response = response;
	}

	public CloudCallRequestGlobalEvent getRequest() {
		return _request;
	}
	
	public boolean callSuceeded() {
		return _success;
	}
	
	public int getResponse() {
		return _response;
	}
	
}

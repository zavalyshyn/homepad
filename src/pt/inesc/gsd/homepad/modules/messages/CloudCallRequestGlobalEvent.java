package pt.inesc.gsd.homepad.modules.messages;

import pt.inesc.gsd.homepad.server.GlobalEvent;

public class CloudCallRequestGlobalEvent extends GlobalEvent {

	private String _params;
	private Boolean _isMultipart;
	private byte[] _payload;
	
	public CloudCallRequestGlobalEvent(int sourceElementId, String params) {
		setSourceElementId(sourceElementId);
		_params = params;
	}
	
	public CloudCallRequestGlobalEvent(int sourceElementId, String params,
			Boolean isMultipart, byte[] payload) {
		setSourceElementId(sourceElementId);
		_params = params;
		_isMultipart = isMultipart;
		_payload = payload;
		
	}
	
	public String getParams() {
		return _params;
	}
	
	public Boolean isMultipart() {
		return _isMultipart;
	}
	
	public byte[] getPayload() {
		return _payload;
	}
}

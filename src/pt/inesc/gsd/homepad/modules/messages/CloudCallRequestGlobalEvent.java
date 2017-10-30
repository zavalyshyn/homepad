package pt.inesc.gsd.homepad.modules.messages;

import pt.inesc.gsd.homepad.server.GlobalEvent;

public class CloudCallRequestGlobalEvent extends GlobalEvent {

	private String _params;
	
	public CloudCallRequestGlobalEvent(int sourceElementId, String params) {
		setSourceElementId(sourceElementId);
		_params = params;
	}
	
	public String getParams() {
		return _params;
	}
}

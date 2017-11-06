package pt.inesc.gsd.homepad.modules.ports;

import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class CloudRequestMultipartPortType extends LayoutEvent implements PortType {

	private String _url;
	private Boolean _isMultipart;
	private byte[] _payload;
	
	public CloudRequestMultipartPortType(String url, Boolean isMultipart, byte[] payload) {
		_url = url;
		_isMultipart = isMultipart;
		_payload = payload;
	}

	public String getUrl() {
		return _url;
	}
	
	public boolean isMultipart() {
		return _isMultipart;
	}
	
	public byte[] getPayload() {
		return _payload;
	}
}

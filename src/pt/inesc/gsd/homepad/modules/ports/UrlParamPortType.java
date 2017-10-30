package pt.inesc.gsd.homepad.modules.ports;

import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class UrlParamPortType extends LayoutEvent implements PortType {

	private String _param;
	
	public UrlParamPortType(String param) {
		_param = param;
	}

	public String getParam() {
		return _param;
	}
}
package pt.inesc.gsd.homepad.modules.ports;

import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class CloudResponsePortType extends LayoutEvent implements PortType {

	private int _cloudResponse;
	
	public CloudResponsePortType(int cloudResponse) {
		_cloudResponse = cloudResponse;
	}

	public int getCloudResponse() {
		return _cloudResponse;
	}
}
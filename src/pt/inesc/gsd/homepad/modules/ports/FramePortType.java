package pt.inesc.gsd.homepad.modules.ports;

import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class FramePortType extends LayoutEvent implements PortType {
	
	private byte[] _data;
	
	public FramePortType(byte[] data) {
		_data = data;
	}

	public byte[] getFrame() {
		return _data;
	}
}
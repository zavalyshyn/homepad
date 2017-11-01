package pt.inesc.gsd.homepad.modules.ports;

import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class ImageBlurrerPortType extends LayoutEvent implements PortType {
	
	private byte[] _blurredFrame;
	
	public ImageBlurrerPortType(byte[] blurredFrame) {
		_blurredFrame = blurredFrame;
	}
	
	public byte[] blurFrame() {
		return _blurredFrame;
	}

}

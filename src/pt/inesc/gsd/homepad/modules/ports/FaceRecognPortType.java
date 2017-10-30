package pt.inesc.gsd.homepad.modules.ports;

import pt.inesc.gsd.homepad.server.runtime.LayoutEvent;
import pt.inesc.gsd.homepad.server.runtime.PortType;

public class FaceRecognPortType extends LayoutEvent implements PortType {

	private boolean _recognized;
	private int[] _features;
	
	public FaceRecognPortType(boolean recognized, int[] features) {
		_recognized = recognized;
		_features = features;
	}

	public boolean facedRecognized() {
		return _recognized;
	}
	
	public int[] getFeatures() {
		return _features;
	}
}
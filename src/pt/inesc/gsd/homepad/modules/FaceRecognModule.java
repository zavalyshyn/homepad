package pt.inesc.gsd.homepad.modules;

import pt.inesc.gsd.homepad.Module;
import pt.inesc.gsd.homepad.modules.elements.FromCameraElement;
import pt.inesc.gsd.homepad.modules.services.FaceRecognService;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;

public class FaceRecognModule extends Module {

	public FaceRecognModule() {
		NativeLayoutElement el = new FromCameraElement();
		_elements.put(el.getName(), el);
	}

	@Override
	public String getModuleName() {
		return "FaceRecogn";
	}

	@Override
	public String getDescription() {
		return "Implements face recognition algorithm.";
	}

	@Override
	public Component getServiceInstance() {
		return new FaceRecognService();
	}
}

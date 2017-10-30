package pt.inesc.gsd.homepad.modules;

import pt.inesc.gsd.homepad.Module;
import pt.inesc.gsd.homepad.modules.elements.FaceRecognElement;
import pt.inesc.gsd.homepad.modules.services.FromCameraService;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;

public class FromCameraModule extends Module {
	
	public FromCameraModule() {
		NativeLayoutElement el = new FaceRecognElement();
		_elements.put(el.getName(), el);
	}

	@Override
	public String getModuleName() {
		return "FromCamera";
	}

	@Override
	public String getDescription() {
		return "Provides an interface to the camera device.";
	}
	
	@Override
	public Component getServiceInstance() {
		return new FromCameraService();
	}
}

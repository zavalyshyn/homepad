package pt.inesc.gsd.homepad.modules;

import pt.inesc.gsd.homepad.Module;
import pt.inesc.gsd.homepad.modules.elements.ImageBlurrerElement;
import pt.inesc.gsd.homepad.modules.services.ImageBlurrerService;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;

public class ImageBlurrerModule extends Module {
	
	public ImageBlurrerModule() {
		NativeLayoutElement el = new ImageBlurrerElement();
		_elements.put(el.getName(), el);
	}

	@Override
	public String getModuleName() {
		return "ImageBlurrer";
	}

	@Override
	public String getDescription() {
		return "Blurs the camera image for privacy protection.";
	}

	@Override
	public Component getServiceInstance() {
		return new ImageBlurrerService();
	}

}

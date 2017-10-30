package pt.inesc.gsd.homepad.modules;

import pt.inesc.gsd.homepad.Module;
import pt.inesc.gsd.homepad.modules.elements.CloudCallElement;
import pt.inesc.gsd.homepad.modules.services.CloudCallService;
import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;

public class CloudCallModule extends Module {
	
	public CloudCallModule() {
		NativeLayoutElement el = new CloudCallElement();
		_elements.put(el.getName(), el);
	}

	@Override
	public String getModuleName() {
		return "CloudCall";
	}

	@Override
	public String getDescription() {
		return "Handles web service calls.";
	}

	@Override
	public Component getServiceInstance() {
		return new CloudCallService();
	}
}

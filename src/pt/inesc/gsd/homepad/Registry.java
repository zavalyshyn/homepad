package pt.inesc.gsd.homepad;

import java.util.Collection;
import java.util.Hashtable;

import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;

public class Registry {

    private Hashtable<String, Module> _modules = 
    		new Hashtable<String,Module>();
    private Hashtable<String, NativeLayoutElement> _elements = 
    		new Hashtable<String,NativeLayoutElement>();
    private Hashtable<String, AppManifest> _applications = 
    		new Hashtable<String, AppManifest>();
	
	public int registerModule(Module module) {
		if (module == null || _modules.contains(module.getModuleName())) {
			return -1;
		}
		for (String elementName : module.getElements().keySet()) {
			if (_elements.contains(elementName)) {
				return -2;
			}
		}
		_modules.put(module.getModuleName(), module);
		for (NativeLayoutElement element : module.getElements().values()) {
			_elements.put(element.getName(), element);
		}
		return 0;
	}
	
	public int registerApplication(AppManifest appManifest) {
		if (appManifest == null || _applications.contains(appManifest.getAppName())) {
			return -1;
		}
		for (String e : appManifest.getRequiredElements()) {
			if (!_elements.contains(e)) {
				return -2;
			}
		}
		// TODO: check that the application layout is consistent
		_applications.put(appManifest.getAppName(), appManifest);
		return 0;
	}

	public void listModules() {
		for (Module m : _modules.values()) {
			System.out.println("Name        : " + m.getModuleName());
			System.out.println("Description : " + m.getDescription());
			for (NativeLayoutElement el : m.getElements().values()) {
				System.out.println("Element     : " + el.getName());
			}
			System.out.println("");
		}
	}

	public void listElements() {
		for (NativeLayoutElement el : _elements.values()) {
			el.listSpecs();
			System.out.println();			
		}
	}

	public NativeLayoutElement getNewElementInstance(String name) {
    	NativeLayoutElement elDB = _elements.get(name);
    	if (elDB != null) {
    		return elDB.newElement();
    	}
    	return null;
	}

	public boolean isNativeElement(String el) {
		return _elements.containsKey(el);
	}

	public String getOutputRules(String el) {
		if (!_elements.containsKey(el)) {
			return "";
		}
		String outputRule = _elements.get(el).getOutData();
		return (outputRule != null) ? outputRule : "";
	}

	public Collection<Module> getModules() {
		return _modules.values();
	}
}

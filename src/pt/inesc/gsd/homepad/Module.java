package pt.inesc.gsd.homepad;

import java.util.Hashtable;

import pt.inesc.gsd.homepad.server.Component;
import pt.inesc.gsd.homepad.server.runtime.NativeLayoutElement;

public abstract class Module {

	protected Hashtable<String,NativeLayoutElement> _elements = 
			new Hashtable<String,NativeLayoutElement>();

	public abstract String getModuleName();

	public abstract String getDescription();
	
	public abstract Component getServiceInstance();
	
	public Hashtable<String,NativeLayoutElement> getElements() {
		return _elements;
	}
	
	public NativeLayoutElement getElementByName(String name) {
		return _elements.get(name);
	}
}

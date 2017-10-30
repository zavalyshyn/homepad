package pt.inesc.gsd.homepad.server.runtime;

import java.util.ArrayList;

public class LayoutConnector {

	private ArrayList<LayoutElement> _elements;
	
	public LayoutConnector() {
		_elements = new ArrayList<LayoutElement>();
	}
	
	public ArrayList<LayoutElement> getElements() {
		return _elements;
	}
	
	public ArrayList<String> getElementNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (LayoutElement e : _elements) {
			names.add(e.toString());
		}
		return names;
	}
	
	public void addElement(LayoutElement element) {
		_elements.add(element);
	}
}

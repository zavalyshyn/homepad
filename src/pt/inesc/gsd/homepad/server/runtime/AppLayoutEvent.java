package pt.inesc.gsd.homepad.server.runtime;

public class AppLayoutEvent extends LayoutEvent implements PortType {

	@Override
	public String getName() {
		return "AppLayoutEvent";
	}
}

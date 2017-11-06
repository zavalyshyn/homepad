package pt.inesc.gsd.homepad.apps;

import pt.inesc.gsd.homepad.AppManifest;
import pt.inesc.gsd.homepad.Layout;

public class HelloWorldAppManifest extends AppManifest {

	@Override
	public String getAppName() {
		return "HelloWorldApp";
	}

	@Override
	public String getAppElementClassName() {
		return "pt.inesc.gsd.homepad.apps.HelloWorldApp";
	}

	@Override
	public String[] getRequiredElements() {
		return new String[] {
			"FromCamera",
			"CloudCall"
		};
	}

	@Override
	public String[] getDeclaredElements() {
		return new String[] {
			"HelloWorldBegin",
			"HelloWorldEnd"
		};
	}

	@Override
	public Layout getLayout() {
		Layout layout = new Layout();

		// specify the nodes
		String[] n = {
			"FromCamera",		// 1
			"HelloWorldBegin",	// 2
			"CloudCall",
			"HelloWorldEnd"
		};
		for (String s : n) layout.addNode(s);

		// specify the edges
		layout.addEdge(1, 2);
		layout.addEdge(2, 3);
		layout.addEdge(3, 4); // port: 0
		return layout;
	}
}

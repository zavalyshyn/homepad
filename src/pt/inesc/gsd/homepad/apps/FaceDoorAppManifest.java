package pt.inesc.gsd.homepad.apps;

import pt.inesc.gsd.homepad.AppManifest;
import pt.inesc.gsd.homepad.Layout;

public class FaceDoorAppManifest extends AppManifest {

	@Override
	public String getAppName() {
		return "FaceDoorApp";
	}

	@Override
	public String getAppElementClassName() {
		return "pt.inesc.gsd.homepad.apps.FaceDoorApp";
	}

	@Override
	public String[] getRequiredElements() {
		return new String[] {
			"FromCamera",
			"FaceRecogn",
			"DoorLockRead",
			"DoorLockUnlock",
			"CloudCall"
		};
	}

	@Override
	public String[] getDeclaredElements() {
		return new String[] {
			"FaceDoorBegin",
			"FaceDoorCheck",
			"FaceDoorEnd"
		};
	}

	@Override
	public Layout getLayout() {
		Layout layout = new Layout();
		
		// specify the nodes
		String[] n = {
			"FromCamera",		// 1
			"FaceRecogn",		// 2
			"FaceDoorBegin",	// ...
			"DoorLockRead", 
			"FaceDoorCheck",
			"DoorLockUnlock",
			"CloudCall",
			"FaceDoorEnd"
		};
		for (String s : n) layout.addNode(s);

		// specify the edges
		layout.addEdge(1, 2);
		layout.addEdge(2, 3);
		layout.addEdge(3, 4); // port: 0
		layout.addEdge(3, 7); // port: 1
		layout.addEdge(4, 5);
		layout.addEdge(5, 6); // port: 0
		layout.addEdge(5, 7); // port: 1
		layout.addEdge(6, 7);
		layout.addEdge(7, 8);
		return layout;
	}
}

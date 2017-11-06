package pt.inesc.gsd.homepad.examples;

import pt.inesc.gsd.homepad.AppManifest;
import pt.inesc.gsd.homepad.Registry;
import pt.inesc.gsd.homepad.apps.FaceDoorAppManifest;
import pt.inesc.gsd.homepad.apps.HelloWorldAppManifest;
import pt.inesc.gsd.homepad.checker.Model;
import pt.inesc.gsd.homepad.checker.Checker;
import pt.inesc.gsd.homepad.modules.CloudCallModule;
import pt.inesc.gsd.homepad.modules.DoorLockModule;
import pt.inesc.gsd.homepad.modules.FaceRecognModule;
import pt.inesc.gsd.homepad.modules.FromCameraModule;
import pt.inesc.gsd.homepad.server.Server;

public class DemoHelloWorld {

	public static void testChecker() {
		
		/*
		 * Initialize the registry describing the modules supported by the hub
		 */

		Registry registry = new Registry();
		registry.registerModule(new FromCameraModule());
		registry.registerModule(new CloudCallModule());

		System.out.println("MODULES");
		System.out.println();
		registry.listModules();
		System.out.println("ELEMENTS");
		System.out.println();
		registry.listElements();
		
		/*
		 * Declare the application we want to run on the hub
		 */

		AppManifest appManifest = new HelloWorldAppManifest();
		appManifest.writeLayoutGraphPdf();
		appManifest.writeLayoutGraphPng();

		/*
		 * Model check the privacy properties of this
		 */
		
		Model model = new Model(appManifest, registry);
		model.addSink("cloudcall");
		model.addSensitive("frame");
//		model.addSensitive("f(frame)");
		model.buildModel();

		Checker checker = new Checker(model);
		checker.checkModel();
		checker.writeReportConsole();
		checker.writeReportHtml();

	}

	public static void testServer() {
		/*
		 * Initialize the registry describing the modules supported by the hub
		 */

		Registry registry = new Registry();
		registry.registerModule(new FromCameraModule());
		registry.registerModule(new CloudCallModule());

		System.out.println("MODULES");
		System.out.println();
		registry.listModules();
		System.out.println("ELEMENTS");
		System.out.println();
		registry.listElements();
		
		AppManifest appManifest = new HelloWorldAppManifest();

		/*
		 * Run the hub server based on the current registry and application
		 */

		System.out.println("HomePad Server");
		Server server = new Server();
		server.initialize(registry, appManifest);
		server.enable();
		server.wait(3000);
		server.disable();
		System.out.println("Bye.");
	}
	
	public static void main(String[] args) { 
		
		//testChecker();
		testServer();
		
	}
}

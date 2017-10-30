package pt.inesc.gsd.homepad;

import pt.inesc.gsd.homepad.utils.GraphVizGenerator;

public abstract class AppManifest {

	public abstract String getAppName();

	public abstract String getAppElementClassName();

	public abstract String[] getRequiredElements();
	
	public abstract String[] getDeclaredElements();

	public abstract Layout getLayout();
	
	public void writeLayoutGraphPng() {
		GraphVizGenerator gv = new GraphVizGenerator();
		gv.generateManifestGraph(this, GraphVizGenerator.OUT_TYPE_PNG);
	}

	public void writeLayoutGraphPdf() {
		GraphVizGenerator gv = new GraphVizGenerator();
		gv.generateManifestGraph(this, GraphVizGenerator.OUT_TYPE_PDF);
	}
}

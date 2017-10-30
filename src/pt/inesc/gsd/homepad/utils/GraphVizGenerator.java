package pt.inesc.gsd.homepad.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import pt.inesc.gsd.homepad.AppManifest;
import pt.inesc.gsd.homepad.Config;
import pt.inesc.gsd.homepad.Layout;
import pt.inesc.gsd.homepad.Logger;

public class GraphVizGenerator {

	private static final Logger LOGGER = Logger.getLogger(GraphVizGenerator.class);

	public static final int OUT_TYPE_PNG = 1;
	public static final int OUT_TYPE_PDF = 2;

	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader =
                         new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}
	
	public int generateManifestGraph(AppManifest manifest, int outType) {
		if (manifest == null || (outType != OUT_TYPE_PNG && outType != OUT_TYPE_PDF)) {
			return -1;
		}
		
		String manifestDir = Config.getManifestGraphvizDir();
		String manifestDotFile = manifest.getAppName() + "-manifest.dot";

		String graphName = manifest.getAppName();
		Layout layout = manifest.getLayout();

		PrintWriter writer = null;
		try {
		    writer = new PrintWriter(new OutputStreamWriter(
		          new FileOutputStream(manifestDir + "/" + manifestDotFile)));

		    writer.println("/*");
		    writer.println(" * Homepad application graph: " + graphName);
		    writer.println(" */");
		    writer.println("");
		    writer.println("digraph {");
		    writer.println("");
		    writer.println("	graph [fontname = \"helvetica\"];");
		    writer.println("	node [fontname = \"helvetica\" fontsize=11 shape = box];");
		    writer.println("	edge [fontname = \"helvetica\" fontsize=9];");
		    writer.println("");

		    ArrayList<String> nodes = layout.getNodes();
		    for (int i = 0; i < nodes.size(); i++) {
		    	ArrayList<String> dependencies = new ArrayList<String> (
		    			Arrays.asList(manifest.getRequiredElements()));
		    	boolean isNative = dependencies.contains(nodes.get(i));
		    	writer.println("	" + (i+1) + " [label=\"" + nodes.get(i) + "\"" +
		    			((!isNative)?" style=filled":"") + "];"); 
		    }

		    writer.println("");
		    for (int from = 0; from < nodes.size(); from++) {
		    	for (int to : layout.getNodeEdges(from+1)) {
		    		writer.println("	" + (from+1) + " -> " + to + ";");
		    	}
		    }

		    writer.println("");
		    writer.println("}");

		} catch (IOException e) {
			LOGGER.log(Logger.ER, "Error: " + e.getMessage());
		} finally {
		   try {writer.close();} catch (Exception e) {}
		}
		
		switch (outType) {
		case OUT_TYPE_PNG:
			String manifestPngFile = manifest.getAppName() + "-manifest.png";
			executeCommand(Config.DOT_PATH + " -Tpng " + manifestDir + "/" + manifestDotFile + 
					" -o " + manifestDir + "/" + manifestPngFile);
			break;
		case OUT_TYPE_PDF:
			String manifestPdfFile = manifest.getAppName() + "-manifest.pdf";
			executeCommand(Config.DOT_PATH + " -Tpdf " + manifestDir + "/" + manifestDotFile + 
					" -o " + manifestDir + "/" + manifestPdfFile);
		}
		return 0;
	}
	
	public int generateModelGraph(AppManifest manifest, ArrayList<String> sensitive, ArrayList<String> sinks, int outType) {
		if (manifest == null || (outType != OUT_TYPE_PNG && outType != OUT_TYPE_PDF)) {
			return -1;
		}
		
		String manifestDir = Config.getManifestGraphvizDir();
		String manifestDotFile = manifest.getAppName() + "-model.dot";

		String graphName = manifest.getAppName();
		Layout layout = manifest.getLayout();

		PrintWriter writer = null;
		try {
		    writer = new PrintWriter(new OutputStreamWriter(
		          new FileOutputStream(manifestDir + "/" + manifestDotFile)));

		    writer.println("/*");
		    writer.println(" * Homepad application graph: " + graphName);
		    writer.println(" */");
		    writer.println("");
		    writer.println("digraph {");
		    writer.println("");
		    writer.println("	graph [fontname = \"helvetica\"];");
		    writer.println("	node [fontname = \"helvetica\" fontsize=11 shape = box];");
		    writer.println("	edge [fontname = \"helvetica\" fontsize=9];");
		    writer.println("");

		    ArrayList<String> nodes = layout.getNodes();
		    for (int i = 0; i < nodes.size(); i++) {
		    	ArrayList<String> dependencies = new ArrayList<String> (
		    			Arrays.asList(manifest.getRequiredElements()));
		    	boolean isNative = dependencies.contains(nodes.get(i));
		    	boolean isSink = sinks.contains(nodes.get(i).toLowerCase());
		    	writer.println("	" + (i+1) + " [label=\"" + nodes.get(i) + "\"" +
		    			((!isNative)?" style=filled":"") + ((isSink)?" color=red":"") + "];"); 
		    }

		    writer.println("");
		    for (int from = 0; from < nodes.size(); from++) {
		    	for (int to : layout.getNodeEdges(from+1)) {
		    		writer.println("	" + (from+1) + " -> " + to + ";");
		    	}
		    }

		    writer.println("");
		    writer.println("}");

		} catch (IOException e) {
			LOGGER.log(Logger.ER, "Error: " + e.getMessage());
		} finally {
		   try {writer.close();} catch (Exception e) {}
		}
		
		switch (outType) {
		case OUT_TYPE_PNG:
			String manifestPngFile = manifest.getAppName() + "-model.png";
			executeCommand(Config.DOT_PATH + " -Tpng " + manifestDir + "/" + manifestDotFile + 
					" -o " + manifestDir + "/" + manifestPngFile);
			break;
		case OUT_TYPE_PDF:
			String manifestPdfFile = manifest.getAppName() + "-model.pdf";
			executeCommand(Config.DOT_PATH + " -Tpdf " + manifestDir + "/" + manifestDotFile + 
					" -o " + manifestDir + "/" + manifestPdfFile);
		}
		return 0;
	}
	
	public int generateModelGraph(AppManifest manifest, ArrayList<String> sensitive, ArrayList<String> sinks, 
			ArrayList<ArrayList<String>> nodeOutput, int outType) {
		if (manifest == null || (outType != OUT_TYPE_PNG && outType != OUT_TYPE_PDF)) {
			return -1;
		}
		
		String manifestDir = Config.getManifestGraphvizDir();
		String manifestDotFile = manifest.getAppName() + "-model.dot";

		String graphName = manifest.getAppName();
		Layout layout = manifest.getLayout();

		PrintWriter writer = null;
		try {
		    writer = new PrintWriter(new OutputStreamWriter(
		          new FileOutputStream(manifestDir + "/" + manifestDotFile)));

		    writer.println("/*");
		    writer.println(" * Homepad application graph: " + graphName);
		    writer.println(" */");
		    writer.println("");
		    writer.println("digraph {");
		    writer.println("");
		    writer.println("	graph [fontname = \"helvetica\"];");
		    writer.println("	node [fontname = \"helvetica\" fontsize=11 shape = box];");
		    writer.println("	edge [fontname = \"helvetica\" fontsize=9];");
		    writer.println("");

		    ArrayList<String> nodes = layout.getNodes();
		    for (int i = 0; i < nodes.size(); i++) {
		    	ArrayList<String> dependencies = new ArrayList<String> (
		    			Arrays.asList(manifest.getRequiredElements()));
		    	boolean isNative = dependencies.contains(nodes.get(i));
		    	boolean isSink = sinks.contains(nodes.get(i).toLowerCase());
		    	writer.println("	" + (i+1) + " [label=\"" + nodes.get(i) + "\"" +
		    			((!isNative)?" style=filled":"") + ((isSink)?" color=red":"") + "];"); 
		    }

		    writer.println("");
		    for (int from = 0; from < nodes.size(); from++) {
		    	for (int to : layout.getNodeEdges(from+1)) {
		    		String outputItems = "";
		    		boolean isSensitive = false;
		    		for (String item : nodeOutput.get(from)) {
		    			outputItems += " " + item;
		    			if(sensitive.contains(item)) {
		    				isSensitive = true;
		    			}
		    		}
		    		writer.println("	" + (from+1) + " -> " + to + "[label = \"" + outputItems + "\"" + 
		    				((isSensitive)?" fontcolor=red":"") + "];");
		    	}
		    }

		    writer.println("");
		    writer.println("}");

		} catch (IOException e) {
			LOGGER.log(Logger.ER, "Error: " + e.getMessage());
		} finally {
		   try {writer.close();} catch (Exception e) {}
		}
		
		switch (outType) {
		case OUT_TYPE_PNG:
			String manifestPngFile = manifest.getAppName() + "-model.png";
			executeCommand(Config.DOT_PATH + " -Tpng " + manifestDir + "/" + manifestDotFile + 
					" -o " + manifestDir + "/" + manifestPngFile);
			break;
		case OUT_TYPE_PDF:
			String manifestPdfFile = manifest.getAppName() + "-model.pdf";
			executeCommand(Config.DOT_PATH + " -Tpdf " + manifestDir + "/" + manifestDotFile + 
					" -o " + manifestDir + "/" + manifestPdfFile);
		}
		return 0;
	}
}

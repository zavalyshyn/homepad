package pt.inesc.gsd.homepad.checker;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import pt.inesc.gsd.homepad.AppManifest;
import pt.inesc.gsd.homepad.Config;
import pt.inesc.gsd.homepad.Layout;
import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.Registry;

public class Model {

	private static final Logger LOGGER = Logger.getLogger(Model.class);

	private String _modelName;
	private String _modelFileName;
	private ArrayList<String> _elements = new ArrayList<String>();
	private ArrayList<String[]> _arrows = new ArrayList<String[]>();
	private ArrayList<String> _untrusted = new ArrayList<String>();
	private ArrayList<String> _sinks = new ArrayList<String>();
	private ArrayList<String> _sensitive = new ArrayList<String>();
	private ArrayList<String> _outputRules = new ArrayList<String>();
	private AppManifest _appManifest;
	private Registry _registry;
	
	class ModelWriter {
		
		PrintWriter _w;
		
		public ModelWriter(PrintWriter w) {
			_w = w;
		}

		public void modelWriterHeader() {
			_w.println("% ----------------------------------------------------------------");
			_w.println("% HomePad App Model");
			_w.println("% ");
			_w.println("% Name: " + _modelName);
			_w.println("% ----------------------------------------------------------------");
		}

		public void modelWriterWhiteSep() {
			_w.println("");
		}

		public void modelWriterElementFacts() {
			_w.println("% ");
			_w.println("% Declare which elements exist in the application");
			_w.println("% ");
			for(String el : _elements) {
				_w.println("");
				_w.println("el(" + el + ").");
			}
		}		

		public void modelWriterArrowFacts() {
			_w.println("% ");
			_w.println("% Declare the element interconnections of the application");
			_w.println("% ");
			for(String[] a : _arrows) {
				_w.println("");
				_w.println("arrow(el(" + a[0] + "),el(" + a[1] + ")).");
			}
		}		

		public void modelWriterUntrustedFacts() {
			_w.println("% ");
			_w.println("% Declare application elements as untrusted");
			_w.println("% ");
			for(String el : _untrusted) {
				_w.println("");
				_w.println("untrusted(el(" + el + ")).");
			}
		}

		public void modelWriterSinksFacts() {
			_w.println("% ");
			_w.println("% Declare which elements are data sinks");
			_w.println("% ");
			for(String el : _sinks) {
				_w.println("");
				_w.println("sink(el(" + el + ")).");
			}
		}

		public void modelWriterInputFacts() {
			_w.println("% ");
			_w.println("% Rule that returns the input data of a given element. The data is");
			_w.println("% provided as a list of data(_) atoms.");
			_w.println("% ");
			_w.println("");
			_w.println("input(el(X), Y) :-");
			_w.println("	arrow(el(Z), el(X)),		% there is an element Z connected to X...");
			_w.println("	output(el(Z), Y).			% whose output is data Y.");
		}

		public void modelWriterOutputFacts() {
			_w.println("% ");
			_w.println("% Facts and rules that tell how to determine the output of an element.");
			_w.println("% The first rule applies to untrusted elements and expresses the idea that the");
			_w.println("% element will try to pass forward all the input it can to the output in order");
			_w.println("% to direct the maximum data it can towards a data sink. The following rules");
			_w.println("% apply to all native elements and specify what data is ouput by each element.");
			_w.println("% ");
			_w.println("");
			_w.println("output(el(X), Y) :- ");
			_w.println("	untrusted(el(X)), 			% if element not trusted, assume worst case:");
			_w.println("	input(el(X), Y).			% output everything received from the input");
			_w.println("");
			for(String el : _outputRules) {
				_w.println("");
				_w.println(el);
			}
		}

		public void modelWriterDataOutRules() {
			_w.println("% ");
			_w.println("% Rules to determine which data is output by each element.");
			_w.println("% ");
			_w.println("");
			_w.println("dataout(X) :- output(el(_), Y), member(data(X), Y).");
			_w.println("dataout(X, Z) :- output(el(Z), Y), member(data(X), Y).");
		}

		public void modelWriterSensitiveFacts() {
			_w.println("% ");
			_w.println("% Declare user-defined sensitive data items.");
			_w.println("% ");
			for(String data : _sensitive) {
				_w.println("");
				_w.println("sensitive(data(" + data + ")).");
			}
		}

		public void modelWriterLeakRules() {
			_w.println("% ");
			_w.println("% Rules to determine if data is being leaked. Data is leaked if it appears");
			_w.println("% as the input to an element that is marked as sink.");
			_w.println("% ");
			_w.println("");
			_w.println("leakall(X) :- ");
			_w.println("	input(el(Y), X),	 		% there is leak if data arrives the input gate");
			_w.println("	sink(el(Y)).				% of a sink element");
			_w.println("leak(data(X)) :- ");
			_w.println("	input(el(Y), Z),	 		% there is leak if data arrives the input gate");
			_w.println("	sink(el(Y)),				% of a sink element");
			_w.println("	member(data(X), Z).			% check if specific piece of data in the list");
			_w.println("leak(data(X), el(Y)) :- ");
			_w.println("	input(el(Y), Z),	 		% there is leak if data arrives the input gate");
			_w.println("	sink(el(Y)),				% of a sink element");
			_w.println("	member(data(X), Z).			% check if specific piece of data in the list");
		}

		public void modelWriterDangerRules() {
			_w.println("% ");
			_w.println("% Rules to determine if if there's leakage of sensitive data.");
			_w.println("% ");
			_w.println("");
			_w.println("danger(data(X)) :- leak(data(X)), sensitive(data(X)).");
		}

		public void modelWriterFooter() {
			_w.println("% ----------------------------------------------------------------");
			_w.println("% Model END");
			_w.println("% ----------------------------------------------------------------");
		}
	}

	private void addArrow(String[] arrow) {
		if (arrow.length != 2) return;
		for (String[] a : _arrows) {
			if (a[0] == arrow[0] && a[1] == arrow[1]) {
				return;
			}
		}
		_arrows.add(arrow.clone());
	}

	public Model(AppManifest appManifest, Registry registry) {
		_appManifest = appManifest;
		_registry = registry;
	}
	
	public void addSink(String sinkName) {
		if (!_sinks.contains(sinkName)) {
			_sinks.add(sinkName);
		}
	}

	public ArrayList<String> getSinks() {
		return _sinks;
	}
	
	public void addSensitive(String sensitiveName) {
		if (!_sensitive.contains(sensitiveName)) {
			_sensitive.add(sensitiveName);
		}
	}

	public ArrayList<String> getSensitive() {
		return _sensitive;
	}
	
	public void buildModel() {
		
		_modelName = _appManifest.getAppName();

		// generate facts: elements
		Layout layout = _appManifest.getLayout();
		for(String n : layout.getNodes()) {
			String el = n.toLowerCase();
			if (!_elements.contains(el)) {
				_elements.add(el);
			}
			// TODO: lower case not the best; ideally, each module has a model name
		}
		
		// generate facts: arrows
		ArrayList<String> nodes = layout.getNodes();
		for (int from = 0; from < nodes.size(); from++) {
			for (int to : layout.getNodeEdges(from + 1)) {
				String fromName = nodes.get(from);
				String toName = nodes.get(to - 1);
				addArrow(new String[] {fromName.toLowerCase(), toName.toLowerCase()});
			}
		}
		
		// generate facts: untrusted
		for(String el : layout.getNodes()) {
			if (!_registry.isNativeElement(el)) {
				String untrustedName = el.toLowerCase();
				if (!_untrusted.contains(untrustedName)) {
					_untrusted.add(untrustedName);
				}
			}
		}

		// generate facts: output rules
		for(String el : layout.getNodes()) {
			if (_registry.isNativeElement(el)) {
				String outRule = _registry.getOutputRules(el);
				if (!_outputRules.contains(outRule)) {
					_outputRules.add(outRule);
				}
			}
		}

		// write to file
		String modelDir = Config.getModelDir();
		String modelFileName = modelDir + "/" + _appManifest.getAppName() + ".model";
		PrintWriter writer = null;
		try {
		    writer = new PrintWriter(new OutputStreamWriter(
		          new FileOutputStream(modelFileName)));
		    ModelWriter w = new ModelWriter(writer);
		    w.modelWriterHeader();
		    w.modelWriterWhiteSep();
		    w.modelWriterElementFacts();
		    w.modelWriterWhiteSep();
		    w.modelWriterArrowFacts();
		    w.modelWriterWhiteSep();
		    w.modelWriterUntrustedFacts();
		    w.modelWriterWhiteSep();
		    w.modelWriterSinksFacts();
		    w.modelWriterWhiteSep();
		    w.modelWriterInputFacts();
		    w.modelWriterWhiteSep();
		    w.modelWriterOutputFacts();
		    w.modelWriterWhiteSep();
		    w.modelWriterDataOutRules();
		    w.modelWriterWhiteSep();
		    w.modelWriterSensitiveFacts();
		    w.modelWriterWhiteSep();
		    w.modelWriterLeakRules();
		    w.modelWriterWhiteSep();
		    w.modelWriterDangerRules();
		    w.modelWriterWhiteSep();
		    w.modelWriterFooter();
		} catch (IOException e) {
			LOGGER.log(Logger.ER, "Error: " + e.getMessage());
		} finally {
		   try {writer.close();} catch (Exception e) {}
		}
		_modelFileName = modelFileName;
	}
	
	public String getModelFileName() {
		return _modelFileName;
	}
	
	public AppManifest getAppManifest() {
		return _appManifest;
	}
}
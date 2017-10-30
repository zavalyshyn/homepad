package pt.inesc.gsd.homepad.checker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cs3.prolog.connector.Connector;
import org.cs3.prolog.connector.process.PrologProcess;
import org.cs3.prolog.connector.process.PrologProcessException;

import pt.inesc.gsd.homepad.Config;
import pt.inesc.gsd.homepad.Logger;
import pt.inesc.gsd.homepad.utils.GraphVizGenerator;

public class Checker {
	
	private static final Logger LOGGER = Logger.getLogger(Checker.class);

	private Model _model;
	
    private List<Map<String, Object>> _queryElements;
    private List<Map<String, Object>> _queryUntrusted;
    private List<Map<String, Object>> _queryDataOut;
    private List<Map<String, Object>> _querySensitive;
    private List<Map<String, Object>> _querySink;
    private List<Map<String, Object>> _queryDanger;
    
    private ArrayList<ArrayList<String>> _nodeOutput;
	
	public Checker(Model m) {
		_model = m;
	}
	
	class QuerySession {
		
		/*
		 * // http://swi-prolog.iai.uni-bonn.narkive.com/BYz1Lfi8/swi-prolog-query
		 */
		
		private PrologProcess _prolog;
		
		public void startSession(String modelFilePath) {
	        try {
	            _prolog = Connector.newPrologProcess();
	            _prolog.setExecutablePath(Config.SWIPL_PATH);
	            File modelFile = new File(modelFilePath);
	        	_prolog.consult(modelFile);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

		/*
		 * If the result is null, the query failed (no results). If the query succeeds, 
		 * the resulting map contains mappings from variable name to the binding
		 */
		public Map<String, Object> queryOnce(String query) {
			if (_prolog == null) {
				LOGGER.log(Logger.ER, "Error: prolog session is not open.");
			}
			Map<String, Object> result = null;
			try {
				result = _prolog.queryOnce(query);
			} catch (PrologProcessException e) {
				LOGGER.log(Logger.ER, "Error: " + e.getMessage() + ".");
			}
			return result;
		}
		
		/*
		 * Get ALL results of the query as a list. Every element in this list is one 
		 * result. If the query fails, the list will be empty (but it won't be null).
		 */
		public List<Map<String, Object>> queryAll(String query) {
			if (_prolog == null) {
				LOGGER.log(Logger.ER, "Error: prolog session is not open.");
			}
			List<Map<String, Object>> results = null;
			try {
				results = _prolog.queryAll(query);
			} catch (PrologProcessException e) {
				LOGGER.log(Logger.ER, "Error: " + e.getMessage() + ".");
			}
			return results;
		}

		public void stopSession() {
			if (_prolog != null) {
				try {
					_prolog.stop();
				} catch (PrologProcessException e) {
					LOGGER.log(Logger.ER, "Error: " + e.getMessage() + ".");
				}
				_prolog = null;
			}
		}
	}

	public int checkModel() {
		if (_model == null) {
			LOGGER.log(Logger.ER, "Error: model cannot be null.");
			return -1;
		}
		String modelFileName = _model.getModelFileName();
		if (modelFileName == null) {
			LOGGER.log(Logger.ER, "Error: model file name not initialized.");
			return -2;
		}

		QuerySession qs = new QuerySession();
		qs.startSession(modelFileName);
		
        _queryElements = qs.queryAll("el(X)");
        _queryUntrusted = qs.queryAll("untrusted(el(X))");
        _queryDataOut = qs.queryAll("dataout(X,Z)");
        _querySensitive = qs.queryAll("sensitive(data(X))");
        _querySink = qs.queryAll("sink(el(X))");
        _queryDanger = qs.queryAll("danger(data(X))");

        // determine what each element returns for output
        _nodeOutput = new ArrayList<ArrayList<String>>();
        ArrayList<String> nodes = _model.getAppManifest().getLayout().getNodes();
        for (@SuppressWarnings("unused") String n : nodes) {
        	_nodeOutput.add(new ArrayList<String>());
        }
        for (Map<String, Object> r : _queryDataOut) {
        	String from = ((String)r.get("Z"));
        	String output = (String)r.get("X");
        	int i = 0;
        	for (String n : nodes) {
        		if (n.toLowerCase().equals(from)) {
        			ArrayList<String> outputItems = _nodeOutput.get(i);
        			if (!outputItems.contains(output)) {
        				outputItems.add(output);
        			}
        		}
        		i++;
        	}
        }

        /*
    	System.out.println("DATA OUTPUT");
        int i = 0;
        for (ArrayList<String> outputItems : _nodeOutput) {
        	System.out.println("Node: " + nodes.get(i));
        	for (String outputItem : outputItems) {
            	System.out.println("  Output: " + outputItem);        		
        	}
        	i++;
        }
    	System.out.println();
        */

        qs.stopSession();
        
        return 0;
	}

	public int writeReportConsole() {

		int q = 1;
		List<Map<String, Object>> results = null;

        System.out.println("Q" + (q++) + ": Which elements are used by the application (native*)?");
        results = _queryElements;
        List<Map<String, Object>> untrusted = _queryUntrusted;
        if (results == null) {
            System.out.println("none");
        } else {
	        for (Map<String, Object> r : results) {
	        	boolean isTrusted = true;
	        	for (Map<String, Object> u : untrusted) {
	        		Object el = u.get("X");
	        		if (el != null && ((String) el).equals(r.get("X"))) {
	        			isTrusted = false;
	        		}
	        	}
	            System.out.println("> " + r.get("X") + ((isTrusted)?"*":""));
	        }
        }
        System.out.println();
        
        System.out.println("Q" + (q++) + ": What types of data are generated within the application?");
        results = _queryDataOut;
        if (results == null) {
            System.out.println("none");
        } else {
	        for (Map<String, Object> r : results) {
	        	System.out.println("> " + r.get("X") + " [by " + r.get("Z") + "]");
	        }
        }
        System.out.println();

        System.out.println("Q" + (q++) + ": Which of these data items are considered to be sensitive?");
        results = _querySensitive;
        if (results == null) {
            System.out.println("none");
        } else {
	        for (Map<String, Object> r : results) {
	        	System.out.println("> " + r.get("X"));
	        }
        }
        System.out.println();
        
        System.out.println("Q" + (q++) + ": Which elements can cause data leakage?");
        results = _querySink;
        if (results == null) {
            System.out.println("none");
        } else {
	        for (Map<String, Object> r : results) {
	            System.out.println("> " + r.get("X"));
	        }
        }
        System.out.println();

        System.out.println("Q" + (q++) + ": Is there the danger of information leakage??");
        results = _queryDanger;
        if (results == null) {
            System.out.println("no");
        } else {
	        for (Map<String, Object> r : results) {
	            System.out.println("> " + r.get("X"));
	        }
        }
        System.out.println();
        
        return 0;
	}

	class HtmlWriter {
		
		PrintWriter _w;
		
		public HtmlWriter(PrintWriter w) {
			_w = w;
		}

		public void writePreamble() {
			_w.println("<!DOCTYPE html>");
			_w.println("<html>");
			_w.println("<body>");
			_w.println();
		}

		public void writeHeader(String header) {
			_w.println("<h2>" + header + "</h2>");
		}

		public void writeImage(String imageName) {
			_w.println("<img src=\"" + imageName + "\"></br>");
		}

		public void writeQuestion(String question) {
			_w.println("<b>" + question + "</b></br>");
		}

		public void writeParagraph() {
			_w.println("<p>");
		}

		public void writeParagraph(String para) {
			_w.println(para + "<p>");
		}

		public void writeBeginList() {
			_w.println("<ul>");
		}

		public void writeListItem(String item) {
			_w.println("<li>" + item);
		}

		public void writeEndList() {
			_w.println("</ul>");
		}

		public void writeEpilogue() {
			_w.println("</body>");
			_w.println("</html>");
		}
	}

	public int writeReportHtml() {

		// generate a png file for the application graph diagraph

		GraphVizGenerator gv = new GraphVizGenerator();
		gv.generateModelGraph(_model.getAppManifest(), 
				_model.getSensitive(), _model.getSinks(), _nodeOutput, GraphVizGenerator.OUT_TYPE_PNG);
		
		// write to file
		String modelDir = Config.getModelCheckerDir();
		String appName = _model.getAppManifest().getAppName();
		String modelFileName = modelDir + "/" + appName + ".html";
		PrintWriter writer = null;
		try {
		    writer = new PrintWriter(new OutputStreamWriter(
		          new FileOutputStream(modelFileName)));
		    HtmlWriter w = new HtmlWriter(writer);
		    
		    w.writePreamble();
		    
		    w.writeHeader(appName);
		    w.writeImage(_model.getAppManifest().getAppName() + "-model.png");
        	w.writeParagraph();
		    
			int q = 1;
			List<Map<String, Object>> results = null;

	        w.writeQuestion("Q" + (q++) + ": Which elements are used by the application (native*)?");
	        results = _queryElements;
	        List<Map<String, Object>> untrusted = _queryUntrusted;
	        if (results == null) {
	        	w.writeParagraph("none");
	        } else {
	        	w.writeBeginList();
		        for (Map<String, Object> r : results) {
		        	boolean isTrusted = true;
		        	for (Map<String, Object> u : untrusted) {
		        		Object el = u.get("X");
		        		if (el != null && ((String) el).equals(r.get("X"))) {
		        			isTrusted = false;
		        		}
		        	}
		        	w.writeListItem(r.get("X") + ((isTrusted)?"*":""));
		        }
		        w.writeEndList();
	        }
	        w.writeParagraph();
	        
	        w.writeQuestion("Q" + (q++) + ": What types of data are generated within the application?");
	        results = _queryDataOut;
	        if (results == null) {
	        	w.writeParagraph("none");
	        } else {
	        	w.writeBeginList();
		        for (Map<String, Object> r : results) {
		        	w.writeListItem(r.get("X") + " [by " + r.get("Z") + "]");
		        }
		        w.writeEndList();
	        }
	        w.writeParagraph();

	        w.writeQuestion("Q" + (q++) + ": Which of these data items are considered to be sensitive?");
	        results = _querySensitive;
	        if (results == null) {
	        	w.writeParagraph("none");
	        } else {
	        	w.writeBeginList();
		        for (Map<String, Object> r : results) {
		        	w.writeListItem((String)r.get("X"));
		        }
		        w.writeEndList();
	        }
	        w.writeParagraph();
	        
	        w.writeQuestion("Q" + (q++) + ": Which elements can cause data leakage?");
	        results = _querySink;
	        if (results == null) {
	        	w.writeParagraph("none");
	        } else {
	        	w.writeBeginList();
		        for (Map<String, Object> r : results) {
		        	w.writeListItem((String)r.get("X"));
		        }
		        w.writeEndList();
	        }
	        w.writeParagraph();

	        w.writeQuestion("Q" + (q++) + ": Is there the danger of information leakage?");
	        results = _queryDanger;
	        if (results == null) {
	        	w.writeParagraph("no");
	        } else {
	            w.writeBeginList();
		        for (Map<String, Object> r : results) {
		            w.writeListItem((String)r.get("X"));
		        }
		        w.writeEndList();
	        }
	        w.writeParagraph();
		    
		    w.writeEpilogue();

		} catch (IOException e) {
			LOGGER.log(Logger.ER, "Error: " + e.getMessage());
		} finally {
		   try {writer.close();} catch (Exception e) {}
		}
        
        return 0;
	}

}
package pt.inesc.gsd.homepad;

import java.util.ArrayList;
import java.util.Hashtable;

public class Layout {

	protected ArrayList<String> _nodes = 
			new ArrayList<String>();
	
	protected Hashtable<Integer, ArrayList<Integer>> _edges = 
			new Hashtable<Integer, ArrayList<Integer>>();

	public void addNode(String node) {
		if (!_nodes.contains(node)) {
			_nodes.add(node);
		}
	}

	public void addEdge(int from, int to) {
		if (from > _nodes.size() || to > _nodes.size() || from == to) {
			return;
		}
		ArrayList<Integer> toList = _edges.get(from);
		if (toList == null) {
			toList = new ArrayList<Integer>();
			_edges.put(from, toList);
		}
		if (!toList.contains(to)) {
			toList.add(to);
		}
	}
	
	public void dump() {
		System.out.println("NODES:");
		for (String s : _nodes) {
			System.out.println("\t" + s);
		}
		System.out.println("EDGES:");
		int nodeId = 0;
		for (String from : _nodes) {
			++nodeId;
			ArrayList<Integer> toList = _edges.get(nodeId);
			if (toList != null) {
				for (int to : toList) {
					System.out.println("\t" + from + " -> " + _nodes.get(to - 1));
				}
			}
		}
	}
	
	public ArrayList<String> getNodes() {
		return _nodes;
	}
	
	public ArrayList<Integer> getNodeEdges(Integer nodeId) {
		ArrayList<Integer> out = _edges.get(nodeId);
		return (out != null) ? out : new ArrayList<Integer>();
	}
}

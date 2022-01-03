package reduction;

import java.util.ArrayList;

import atoms.Atom;
import atoms.AtomType;

/**
 * A graph encapsulation with operations to apply on it
 * @author Rares
 *
 */
public class GraphOperator {
	private Graph graph;
	
	public GraphOperator(Graph graph) {
		this.graph = graph;
	}
	
	public GraphOperator() {}
	
	/**
	 * Boolean AND between two operands (String representation)
	 * @param a		First operand
	 * @param b		Second operand
	 * @return		{@code "(a & b)"}
	 */
	public static String AND(String a, String b) {
		return "(" + a + "&" + b + ")";
	}
	
	/**
	 * Boolean AND between many operands (String representation)
	 * @param operands	The operands
	 * @return			{@code AND} between all the operands:
	 * {@code "(a & b & c & ...)"}
	 */
	public static String AND(ArrayList<String> operands) {
		String toReturn = "";
		
		for (String currOperand : operands) {
			if (toReturn.equals("")) {
				toReturn = currOperand;
			} else {
				toReturn += "&" + currOperand;
			}
		}
		
		return "(" + toReturn + ")";
	}
	
	/**
	 * Boolean OR between two operands (String representation)
	 * @param a		First operand
	 * @param b		Second operand
	 * @return		{@code "(a | b)"}
	 */
	public static String OR(String a, String b) {
		return "(" + a + "|" + b + ")";
	}
	
	/**
	 * Boolean AND between many operands (String representation)
	 * @param operands	The operands
	 * @return			{@code OR} between all the operands:
	 * {@code "(a | b | c | ...)"}
	 */
	public static String OR(ArrayList<String> operands) {
		String toReturn = "";
		
		for (String currOperand : operands) {
			if (toReturn.equals("")) {
				toReturn = currOperand;
			} else {
				toReturn += "|" + currOperand;
			}
		}
		
		return "(" + toReturn + ")";
	}
	
	/**
	 * Boolean NOT for an operand (String representation)
	 * @param a		The operand
	 * @return		{@code "~(a)")}
	 */
	public static String NOT(String a) {
		return "~" + a;
	}
	
	/**
	 * Boolean XNOR between two operands (String representation)
	 * @param a		First operand
	 * @param b		Second operand
	 * @return		{@code "((a|~b)&(~a|b))"}
	 */
	public static String XNOR(String a, String b) {
		return AND(OR(a, NOT(b)), OR(NOT(a), b));
	}

	public static String noParanthesis(String string) {
		if (string.charAt(0) == '(' && string.charAt(string.length() - 1) == ')') {
			return string.substring(1, string.length() - 1);
		}

		return string;
	}
	
	/**
	 * Forms clauses representing, for every node, every path that could be
	 * taken THROUGH it (2 edges) along with all the possible distance numbers
	 * for the node.
	 * @return	String representation of the clauses mentioned above
	 */
	public String pathsAndDistances() {
		ArrayList<String> clauses = new ArrayList<>();
		
		// iterate through every node
		for (int nodeStart = 1; nodeStart <= graph.getNodes(); nodeStart++) {
			ArrayList<String> edges = new ArrayList<>();
			
			for (int nodeEnd = 1; nodeEnd <= graph.getNodes(); nodeEnd++) {
				if (nodeEnd == nodeStart) {
					// same node
					continue;
				}
				
				if (graph.hasEdge(nodeStart, nodeEnd)) {
					edges.add(new Atom().setType(AtomType.EDGE)
										.setArg1(nodeStart)
										.setArg2(nodeEnd).toString());
				}
			}
			
			if (edges.size() < 2) {
				// no cycle is possible if this node exists, abort the program
				return "";
			}
			
			clauses.add(twoByTwo(edges));
			
			if (nodeStart != 1) {
				// distance from 1 to 1 doesn't make sense
				clauses.add(possibleDistances(nodeStart));
			}
		}
		
		return noParanthesis(AND(clauses));
	}
	
	/**
	 * Takes a list of edges and creates clauses by taking every pair of edges
	 * with their own truth value and negating the rest of the edges (applying
	 * AND between the edges, and OR between the clauses)
	 * @param edges		The list of edges
	 * @return			The string representation of the mentioned clauses
	 */
	private String twoByTwo(ArrayList<String> edges) {
		ArrayList<String> clauses = new ArrayList<>();
		
		for (int i = 0; i < edges.size(); i++) {
			for (int j = i + 1; j < edges.size(); j++) {
				ArrayList<String> atoms = new ArrayList<>();
				
				atoms.add(edges.get(i).toString());
				atoms.add(edges.get(j).toString());
				
				for (int k = 0; k < edges.size(); k++) {
					// the remaining atoms, negated
					if (k != i && k != j) {
						atoms.add(NOT(edges.get(k)));
					}
				}
				
				clauses.add(AND(atoms));
			}
		}
		
		return OR(clauses);
	}
	
	/**
	 * For a node, simply lists all the possible values for the distance
	 * between it and the first node (namely, between {@code 1} and {@code
	 * N/2 + 1}, where {@code N} is the number of nodes in the graph.
	 * @param node		The given node
	 * @return			A string representation of a clause generated in the
	 * aforementioned manner
	 */
	private String possibleDistances(Integer node) {
		ArrayList<String> atoms = new ArrayList<>();
		
		for (int distance = 1; distance <= graph.getNodes()/2 + 1; distance++) {
			String distanceAtom = new Atom().setType(AtomType.DISTANCE)
											.setArg1(distance)
											.setArg2(node)
											.toString();
			
			atoms.add(distanceAtom);
		}
		
		return OR(atoms);
	}
	
	/**
	 * In an undirected graph, an edge can be crossed both ways. List clauses
	 * for every edge in this manner
	 * @return		String representation of the clauses mentioned above
	 */
	public String twoWayPaths() {
		ArrayList<String> clauses = new ArrayList<>();
		
		for (int nodeStart = 1; nodeStart <= graph.getNodes(); nodeStart++) {
			for (int nodeEnd = nodeStart + 1; nodeEnd <= graph.getNodes(); nodeEnd++) {
				if (graph.hasEdge(nodeStart, nodeEnd)) {
					String forward = new Atom().setType(AtomType.EDGE)
												.setArg1(nodeStart)
												.setArg2(nodeEnd)
												.toString();
					String backward = new Atom().setType(AtomType.EDGE)
												.setArg1(nodeEnd)
												.setArg2(nodeStart)
												.toString();
					
					clauses.add(XNOR(forward, backward));
				}
			}
		}
		
		return noParanthesis(AND(clauses));
	}
	
	/**
	 * Generates every way one may go from the first node
	 * @return	The string representation of the way
	 */
	public String pathsFromFirstNode() {
		ArrayList<String> clauses = new ArrayList<>();
		
		for (int currNode = 1; currNode <= graph.getNodes(); currNode++) {
			if (graph.hasEdge(1, currNode)) {
				String distance = new Atom().setType(AtomType.DISTANCE)
											.setArg1(1)
											.setArg2(currNode)
											.toString();
				String edge = new Atom().setType(AtomType.EDGE)
										.setArg1(1)
										.setArg2(currNode)
										.toString();
				clauses.add(XNOR(distance, edge));
			}
		}
		
		for (int currNode = 1; currNode <= graph.getNodes(); currNode++) {
			if (!graph.hasEdge(1, currNode)) {
				String distanceAtom = new Atom().setType(AtomType.DISTANCE)
												.setArg1(1)
												.setArg2(currNode)
												.toString();
				clauses.add(NOT(distanceAtom));
			}
		}
		
		return noParanthesis(AND(clauses));
	}
	
	/**
	 * Generates, for every node, every single way of reaching that node,
	 * depending especially on the intended distance to traverse from the first
	 * node to it
	 * @return		The string representation consisting of clauses in the
	 * manner described above
	 */
	public String pathsToEveryNode() {
		ArrayList<String> clauses = new ArrayList<>();
		
		for (int distance = 2; distance <= graph.getNodes()/2 + 1; distance++) {
			for (int currNode = 2; currNode <= graph.getNodes(); currNode++) {
				String distanceAtom = new Atom().setType(AtomType.DISTANCE)
												.setArg1(distance)
												.setArg2(currNode)
												.toString();
				String entriesAtom = possibleNodeEntries(distance, currNode);

				clauses.add(XNOR(distanceAtom, entriesAtom));
			}
		}
		
		return noParanthesis(AND(clauses));
	}
	
	/**
	 * Depending on the distance, list every possible entry for a node
	 * @param distance		The distance from the first node to the given one
	 * @param node			The given node
	 * @return				The string representation of clauses representing
	 * the aforementioned node entries
	 */
	private String possibleNodeEntries(Integer distance, Integer node) {
		ArrayList<String> allEntries = new ArrayList<>();
		
		for (int currNode = 2; currNode <= graph.getNodes(); currNode++) {
			if (graph.hasEdge(currNode, node)) {
				String distanceAtom = new Atom().setType(AtomType.DISTANCE)
												.setArg1(distance - 1)
												.setArg2(currNode)
												.toString();
				String edgeAtom = new Atom().setType(AtomType.EDGE)
											.setArg1(currNode)
											.setArg2(node)
											.toString();
				
				allEntries.add(AND(distanceAtom, edgeAtom));
			}
		}
		
		ArrayList<String> lowerDistances = new ArrayList<>();
		for (int currDistance = 1; currDistance < distance; currDistance++) {
			String distanceAtom = new Atom().setType(AtomType.DISTANCE)
											.setArg1(currDistance)
											.setArg2(node)
											.toString();
			
			lowerDistances.add(distanceAtom);
		}

		return AND(OR(allEntries), NOT(OR(lowerDistances)));
	}
	
}

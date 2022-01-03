package reduction;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class of the project, containing the 'main' method.
 * @author Rares
 *
 */
public class MainClass {
	public static Scanner inputStream;
	public static PrintStream outputStream;
	public static PrintStream console;
	
	public static Graph graph;
	public static GraphOperator graphOperator;
	
	/**
	 * Opens a new stream to use as input
	 * @param fileName	The name of the file
	 */
	public static void setInputStream(String fileName) {
		if (inputStream != null) {
			// close the previous stream
			inputStream.close();
		}
		
		try {
			inputStream = new Scanner(new File(fileName));
		}
		catch (Exception exc) {
			restoreIO();
			System.out.println(exc + "Exception in setInput");
		}
	}
	
	/**
	 * Open a new stream to use as output
	 * @param fileName	The name of the file
	 */
	public static void setOutputStream(String fileName) {
		if (outputStream != null) {
			// close the previous stream
			outputStream.close();
		}
		try {
			outputStream = new PrintStream(new File(fileName));
		}
		catch (Exception exc) {
			restoreIO();
			System.out.println(exc + "Exception in setOutput");
		}
	}
	
	/** 
	 * Initializes input and output streams and saves
	 * @param inFileName	File to use as input stream
	 * @param outFileName	File to use as output stream
	 */
	public static void setupIO(String inFileName, String outFileName) {
		setInputStream(inFileName);
		setOutputStream(outFileName);
		
		// backup the console if necessary
		if (console == null) {
			console = System.out;
		}
		
		// set the new output stream
		System.setOut(outputStream);
	}
	
	/**
	 * Closes input and output streams
	 */
	public static void restoreIO() {
		if (inputStream != null) {
			inputStream.close();
		}
		
		if (outputStream != null) {
			outputStream.close();
		}
		
		// restore console if necessary
		if (console != null) {
			System.setOut(console);
		}
	}
	
	/**
	 * Initialize a new graph with the given number of nodes and edges
	 */
	public static void registerGraph() {
		graph = new Graph(inputStream.nextInt());
		
		// Scanner clutter
		inputStream.nextLine();
		
		// read the edges
		while(inputStream.hasNextInt()) {
			Integer vertex1 = inputStream.nextInt();
			
			if (vertex1.equals(-1)) {
				// stop reading
				return;
			}
			
			Integer vertex2 = inputStream.nextInt();
			
			graph.addEdge(vertex1, vertex2);
			
			// Scanner clutter
			inputStream.nextLine();
		}
		
	}
	
	public static void convertToSAT() {
		graphOperator = new GraphOperator(graph);
		
		String pathsAndDists = graphOperator.pathsAndDistances();
		if (pathsAndDists.equals("")) {
			// return something that's always false
			System.out.println("x1-1 & ~x1-1");
			return;
		}
		
		ArrayList<String> clauses = new ArrayList<>();
		
		clauses.add(pathsAndDists);
		clauses.add(graphOperator.twoWayPaths());
		clauses.add(graphOperator.pathsFromFirstNode());
		clauses.add(graphOperator.pathsToEveryNode());
		
		System.out.println(GraphOperator.noParanthesis(GraphOperator.AND(clauses)));
	}
	
	public static void main(String[] args) {
		setupIO("graph.in", "bexpr.out");
		registerGraph();
		convertToSAT();
		restoreIO();
	}

}

package reduction;

/**
 * A simple graph, to which an edge may be added and may be tested for its
 * existence
 * @author Rares
 *
 */
public class Graph {
	private Integer nodes;
	private boolean[][] adjMatrix;
	
	public Graph(Integer nodes) {
		this.nodes = nodes;
		this.adjMatrix = new boolean[nodes][nodes];
	}
	
	/**
	 * Change the corresponding element in the adjacency matrix to {@code true}
	 * (default is {@code false}
	 * @param vertex1	A vertex of the edge
	 * @param vertex2	The other vertex of the edge
	 */
	public void addEdge(Integer vertex1, Integer vertex2) {
		if (vertex1 > nodes || vertex2 > nodes) {
			// no valid vertexes
			return;
		}
		
		// default value for the boolean primitive is 'false'
		adjMatrix[vertex1 - 1][vertex2 - 1] = true;
		adjMatrix[vertex2 - 1][vertex1 - 1] = true; // symmetry
	}
	
	/**
	 * Checks if there is an edge between two given vertexes
	 * @param vertex1	A vertex of the edge
	 * @param vertex2	The other vertex of the edge
	 * @return			{@code true} if there is an edge between the two given
	 * vertexes
	 */
	public boolean hasEdge(Integer vertex1, Integer vertex2) {
		if (vertex1 > nodes || vertex2 > nodes) {
			// no valid vertexes
			return false;
		}
		
		return adjMatrix[vertex1 - 1][vertex2 - 1];
	}

	// getters and setters
	public Integer getNodes() {
		return nodes;
	}

	public Graph setNodes(Integer nodes) {
		this.nodes = nodes;
		return this;
	}
	
	
}

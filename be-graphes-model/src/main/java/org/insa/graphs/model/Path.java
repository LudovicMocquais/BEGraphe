package org.insa.graphs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Class representing a path between nodes in a graph.
 * </p>
 * 
 * <p>
 * A path is represented as a list of {@link Arc} with an origin and not a list
 * of {@link Node} due to the multi-graph nature (multiple arcs between two
 * nodes) of the considered graphs.
 * </p>
 *
 */
public class Path {

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the fastest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     * Need to be implemented.
     */
    public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
    	Path path;
        if(nodes.size()>1){
	    	List<Arc> arcs = new ArrayList<Arc>();
	        for(int i = 0;i<nodes.size()-1;i++) {
	        	
	        	List<Arc> successors = nodes.get(i).getSuccessors();
	        	List<Arc> right_successors = new ArrayList<Arc>();
	        	for(Arc arc : successors) {
	        		if(arc.getDestination()==nodes.get(i+1)) {
	        			right_successors.add(arc);
	        		}	
	        	}
	        	
	        	double travel_min = Double.POSITIVE_INFINITY;	
	        	int id = 0;
	        	
	        	for(int j = 0;j<right_successors.size();j++) {
	        		double travel = right_successors.get(j).getMinimumTravelTime();
	        		if(travel < travel_min) {
	        			travel_min = travel;
	        			id = j;
	        		}
	        	}
	        	if(right_successors.size() !=0) {arcs.add(right_successors.get(id));}
	        	else {throw new IllegalArgumentException("Cannot proceed with the creation of the path, no link between two nodes of the path.");}
	        }
	        path = new Path(graph, arcs);
        }
        else if(nodes.size()==1) {
        	
        	path = new Path(graph, nodes.get(0));
        	
        } 
        else {
        	
        	path = new Path(graph);
        	
        }
        return path;
    }

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the shortest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * is.graph=graph;
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     * Need to be implemented.
     */
    public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
    	Path path;
        if(nodes.size()>1){
	    	List<Arc> arcs = new ArrayList<Arc>();
	        for(int i = 0;i<nodes.size()-1;i++) {
	        	
	        	List<Arc> successors = nodes.get(i).getSuccessors();
	        	List<Arc> right_successors = new ArrayList<Arc>();
	        	for(Arc arc : successors) {
	        		if(arc.getDestination()==nodes.get(i+1)) {
	        			right_successors.add(arc);
	        		}	
	        	}
	        	
	        	float dist_min = Float.POSITIVE_INFINITY;	
	        	int id = 0;
	        	
	        	for(int j = 0;j<right_successors.size();j++) {
	        		float dist = right_successors.get(j).getLength();
	        		if(dist < dist_min) {
	        			dist_min = dist;
	        			id = j;
	        		}
	        	}
	        	if(right_successors.size() !=0) {arcs.add(right_successors.get(id));}
	        	else {throw new IllegalArgumentException("Cannot proceed with the creation of the path, no link between two nodes of the path.");}
	        }
	        path = new Path(graph, arcs);
        }
        else if(nodes.size()==1) {
        	
        	path = new Path(graph, nodes.get(0));
        	
        } 
        else {
        	
        	path = new Path(graph);
        	
        }
        return path;
    }

    /**
     * Concatenate the given paths.
     * 
     * @param paths Array of paths to concatenate.
     * 
     * @return Concatenated path.
     * 
     * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of
     *         map do not match, or the end of a path is not the beginning of the
     *         next).
     */
    public static Path concatenate(Path... paths) throws IllegalArgumentException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("Cannot concatenate an empty list of paths.");
        }
        final String mapId = paths[0].getGraph().getMapId();
        for (int i = 1; i < paths.length; ++i) {
            if (!paths[i].getGraph().getMapId().equals(mapId)) {
                throw new IllegalArgumentException(
                        "Cannot concatenate paths from different graphs.");
            }
        }
        ArrayList<Arc> arcs = new ArrayList<>();
        for (Path path: paths) {
            arcs.addAll(path.getArcs());
        }
        Path path = new Path(paths[0].getGraph(), arcs);
        if (!path.isValid()) {
            throw new IllegalArgumentException(
                    "Cannot concatenate paths that do not form a single path.");
        }
        return path;
    }

    // Graph containing this path.
    private final Graph graph;

    // Origin of the path
    private final Node origin;

    // List of arcs in this path.
    private final List<Arc> arcs;

    /**
     * Create an empty path corresponding to the given graph.
     * 
     * @param graph Graph containing the path.
     */
    public Path(Graph graph) {
        this.graph = graph;
        this.origin = null;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path containing a single node.
     * 
     * @param graph Graph containing the path.
     * @param node Single node of the path.
     */
    public Path(Graph graph, Node node) {
        this.graph = graph;
        this.origin = node;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path with the given list of arcs.
     * 
     * @param graph Graph containing the path.
     * @param arcs Arcs to construct the path.
     */
    public Path(Graph graph, List<Arc> arcs) {
        this.graph = graph;
        this.arcs = arcs;
        this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
    }

    /**
     * @return Graph containing the path.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return First node of the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Last node of the path.
     */
    public Node getDestination() {
        return arcs.get(arcs.size() - 1).getDestination();
    }

    /**
     * @return List of arcs in the path.
     */
    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    /**
     * Check if this path is empty (it does not contain any node).
     * 
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.origin == null;
    }

    /**
     * Get the number of <b>nodes</b> in this .getMinimumTravelTime()path.
     * 
     * @return Number of nodes in this path.
     */
    public int size() {
        return isEmpty() ? 0 : 1 + this.arcs.size();
    }

    /**
     * Check if this path is valid.
     * 
     * A path is valid if any of the following is true:
     * <ul>
     * <li>it is empty;</li>
     * <li>it contains a single node (without arcs);</li>
     * <li>the first arc has for origin the origin of the path and, for two
     * consecutive arcs, the destination of the first one is the origin of the
     * second one.</li>
     * </ul>
     * 
     * @return true if the path is valid, false otherwise.
     * 
     * Need to be implemented.
     */
    public boolean isValid() {
    	boolean retour = false;
        if(this.isEmpty() == true || this.size() == 1) {retour = true;}
        else {
        	if(this.getArcs().get(0).getOrigin().getId() != this.getOrigin().getId()) {
        		retour=false;
        	}
        	else {
        		retour = true;
		    	for (int i = 0;i+1<this.getArcs().size();i++){
		    		Node Arc1Dest = this.getArcs().get(i).getDestination();
		    		Node Arc2Origin = this.getArcs().get(i+1).getOrigin();
	            	if(Arc1Dest!=Arc2Origin) {
	            		retour=retour&false;
	            	}
		        }
		    } 
        }
        return retour;
    }

    /**
     * Compute the length of this path (in meters).
     * 
     * @return Total length of the path (in mete.getMinimumTravelTime()rs).
     * 
     *  Need to be implemented.
     */
    public float getLength() {
    	float length = 0;
    	for (Arc arc : this.arcs){
            length += arc.getLength();
            
        }
        return length;
    }

    /**
     * Compute the time required to travel this path if moving at the given speed.
     * 
     * @param speed Speed to compute the travel time.
     * 
     * @return Time (in seconds) required to travel this path at the given speed (in
     *         kilometers-per-hour).
     * 
     *  Need to be implemented.
     */
    public double getTravelTime(double speed) {
    	double time = 0;
    	for (Arc arc : this.arcs){
            time += arc.getTravelTime(speed);
        }
        return time;
    }

    /**
     * Compute the time to travel this path if moving at the maximum allowed speed
     * on every arc.
     * 
     * @return Minimum travel time to travel this path (in seconds).
     * 
     *  Need to be implemented.
     */
    public double getMinimumTravelTime() {
    	double time = 0;
    	for (Arc arc : this.arcs){
            time += arc.getMinimumTravelTime();
        }
        return time;
    }

}

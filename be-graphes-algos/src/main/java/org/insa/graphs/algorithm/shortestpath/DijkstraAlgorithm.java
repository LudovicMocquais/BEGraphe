package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
		
    public DijkstraAlgorithm(ShortestPathData data) {
    	super(data);
    }
    //Initialisation function usefuk for A* (All peak written as false, infinite cost, no father)
    
	protected ArrayList<Label> labels = new ArrayList<Label>();
	
    protected void SetLabels(ShortestPathData data) {
    	
    	for(int j=0;j<data.getGraph().getNodes().size();j++) {
    		
        	labels.add(new Label(data.getGraph().getNodes().get(j),null,Double.POSITIVE_INFINITY,false));
        	
    	}
    }

    @Override
    protected ShortestPathSolution doRun() {
    
    	//Initialisation of data and heap
        final ShortestPathData data = getInputData();
        SetLabels(data);
        Graph graph = data.getGraph();
        ShortestPathSolution solution = null;
        Label labelOrigin = labels.get(data.getOrigin().getId());
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        labelOrigin.setCost(0.0);
        heap.insert(labelOrigin);
        
        while(!heap.findMin().isMarked() && !heap.isEmpty()) { 

        	Label minHeap = heap.deleteMin();
        	minHeap.setMarked();
        	notifyNodeMarked(minHeap.currentPeak);
        	
        	if(minHeap.currentPeak.getId() == data.getDestination().getId()) { 
        		break; 
        	}

        	for(Arc successor : minHeap.currentPeak.getSuccessors()){
        		
        		if (!data.isAllowed(successor)) {
                    continue;
                }
        		
        		if(!labels.get(successor.getDestination().getId()).isMarked()) {
        			double w = data.getCost(successor);
        			Label LabelSucc = labels.get(successor.getDestination().getId());
        			Label LabelCourant = labels.get(successor.getOrigin().getId());

        			if(LabelSucc.getCost() > LabelCourant.getCost() + w) { 
        				
        				if(LabelSucc.getFather()!=null) heap.remove(LabelSucc); 
        				LabelSucc.setCost(LabelCourant.getCost() + w);
        				LabelSucc.setFather(successor); 
        				heap.insert(LabelSucc);
        			}
        		}
        	}
        }
        
        if (labels.get(data.getDestination().getId()).getFather() == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }

        else {

            notifyDestinationReached(data.getDestination());
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = labels.get(data.getDestination().getId()).getFather();
            while (arc != null) {
                arcs.add(arc);
                arc = labels.get(arc.getOrigin().getId()).getFather();
            }
            
            Collections.reverse(arcs);
            Path chemin = new Path(graph, arcs);
            if(!chemin.isValid()) System.out.println("Chemin invalide");
            solution = new ShortestPathSolution(data, Status.OPTIMAL, chemin);
        }
        
        return solution;
    }

}
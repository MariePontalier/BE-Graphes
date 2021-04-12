package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;

import javax.print.attribute.standard.Destination;

import org.insa.graphs.model.*;
import org.insa.graphs.algorithm.utils.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	private class Label implements Comparable<Label> {
		private Node sommet_courant;
		private boolean marque;
		private double cout;
		private Arc arc_precedent;
		public double getCost() {return cout;};
		@Override
		public int compareTo(Label autre) {
			return (Double.compare(this.cout, autre.cout));
		}
	}

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        //INITIALISATION
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        Graph graph = data.getGraph();
        Label[] tab_label= new Label[graph.size()];
        for (int i=0; i<graph.size();i++) {
        	tab_label[i]=new Label();
        	tab_label[i].sommet_courant=graph.get(i);
        	tab_label[i].marque=false;
        	tab_label[i].cout=Double.MAX_VALUE;
        }
        tab_label[data.getOrigin().getId()].cout = 0;
        BinaryHeap<Label> tas= new BinaryHeap<Label>(); 
        tas.insert(tab_label[data.getOrigin().getId()]);

        //ITERATIONS
        while (!tas.isEmpty()){
        	Label x = tas.deleteMin();
        	x.marque=true;
        	if(data.getDestination()==x.sommet_courant) {
        		break;
        	}
        	for (Arc arc: x.sommet_courant.getSuccessors()) {
        		Label y = tab_label[arc.getDestination().getId()];
        		if (y.marque==false) {
        			double ancien_cout=y.cout;
        			y.cout=Math.min(ancien_cout, x.cout+arc.getLength());
        			if(ancien_cout!=y.cout) {
        				tas.insert(y);
        				y.arc_precedent=arc;
        			}
        		}
        	}
        }
        
        if(tab_label[data.getDestination().getId()].cout==Double.MAX_VALUE) {
        	solution=new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
        	
        	ArrayList<Arc> arcs = new ArrayList<>();
        	Arc arc = tab_label[data.getDestination().getId()].arc_precedent;
        	while (arc != null) {
                arcs.add(arc);
                arc = tab_label[arc.getOrigin().getId()].arc_precedent;
            }
        	Collections.reverse(arcs);
        	Path path;
        	path=new Path(graph, arcs);
        	solution= new ShortestPathSolution(data, Status.OPTIMAL, path);
        }
        
 
        return solution;
    }

}

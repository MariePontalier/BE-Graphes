package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
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
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        Graph graph = data.getGraph();
        Label tab_label[]= new Label[graph.size()];
        for (int i=0; i<graph.size();i++) {
        	tab_label[i].sommet_courant=graph.get(i);
        	tab_label[i].marque=false;
        	tab_label[i].cout=Double.MAX_VALUE;
        }
        tab_label[data.getOrigin().getId()].cout = 0;
        BinaryHeap<Label> tas= new BinaryHeap<Label>(); 
        
        return solution;
    }

}

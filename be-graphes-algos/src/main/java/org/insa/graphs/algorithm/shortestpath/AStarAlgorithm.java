package org.insa.graphs.algorithm.shortestpath;

import java.util.*;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm.Label;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.AbstractSolution.Status;
//import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm.Label;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;

public class AStarAlgorithm extends DijkstraAlgorithm {
	
	//DISTANCE OPTIMISTE : VOL D'OISEAU
		protected class LabelStar extends Label{
			protected double volOiseau;
			public double getTotalCost() {return cout+volOiseau;}
			public int compareTo(Label autre) {
				if (this.getTotalCost()==((LabelStar)autre).getTotalCost()) {
				return (Double.compare(this.volOiseau, ((LabelStar)autre).volOiseau));
				}
				else {
					return (Double.compare(this.getTotalCost(), ((LabelStar)autre).getTotalCost()));
				}
			}
		}

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    protected Label[] initLabel() {
    	Label[] tab_label= new LabelStar[data.getGraph().size()];
        for (int i=0; i<data.getGraph().size();i++) {
        	tab_label[i]=new LabelStar();
        	tab_label[i].sommet_courant=data.getGraph().get(i);
        	tab_label[i].marque=false;
        	tab_label[i].cout=Double.MAX_VALUE;
        	double v_max=data.getMaximumSpeed();
        	if (v_max==-1) {
        	v_max=data.getGraph().getGraphInformation().getMaximumSpeed();
        	}
        	if (data.getMode()==Mode.LENGTH) {
        	((LabelStar)tab_label[i]).volOiseau=Point.distance(data.getGraph().get(i).getPoint(),((ShortestPathData)data).getDestination().getPoint());
        	}
        	else { ((LabelStar)tab_label[i]).volOiseau=Point.distance(data.getGraph().get(i).getPoint(),((ShortestPathData)data).getDestination().getPoint())*3.6/v_max;
        	}
        }
        return tab_label; 
    }
}


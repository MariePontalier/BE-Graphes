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
    	Label[] tab_label= new LabelStar[data.getGraph().size()]; //init_label 
        for (int i=0; i<data.getGraph().size();i++) {
        	tab_label[i]=new LabelStar();
        	tab_label[i].sommet_courant=data.getGraph().get(i);
        	tab_label[i].marque=false;
        	tab_label[i].cout=Double.MAX_VALUE;
        	if (data.getMode()==Mode.LENGTH) {
        	((LabelStar)tab_label[i]).volOiseau=Point.distance(data.getGraph().get(i).getPoint(),((ShortestPathData)data).getDestination().getPoint());
        	}
        	else { ((LabelStar)tab_label[i]).volOiseau=Point.distance(data.getGraph().get(i).getPoint(),((ShortestPathData)data).getDestination().getPoint())*3.6/data.getGraph().getGraphInformation().getMaximumSpeed();
        	}
        }
        return tab_label; 
    }
    
    /*@Override
    protected ShortestPathSolution doRun() {

        //INITIALISATION
        /*final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        Graph graph = data.getGraph();
        Label[] tab_label= initLabel();
        tab_label[data.getOrigin().getId()].cout = 0;
        BinaryHeap<Label> tas= new BinaryHeap<Label>(); 
        tas.insert(tab_label[data.getOrigin().getId()]);
        notifyOriginProcessed(data.getOrigin());

        //ITERATIONS
        while (!tas.isEmpty()){
        	Label x = tas.deleteMin();
        	x.marque=true;
            System.out.println(x.cout);
            notifyNodeMarked(x.sommet_courant);
        	if(data.getDestination()==x.sommet_courant) {
        		break;
        	}
        	for (Arc arc: x.sommet_courant.getSuccessors()) {
        		if (!data.isAllowed(arc)) {
                    continue;
                }
        		LabelStar y = tab_label[arc.getDestination().getId()];
        		if (y.marque==false) {
        			//double ancien_cout=y.getTotalCost();
        			if(y.cout > x.cout+arc.getLength()) {
        				y.cout=x.cout+arc.getLength();
        				tas.insert(y);
        				y.arc_precedent=arc;
                        notifyNodeReached(arc.getDestination());
        			}
        		}
        	}
        }
        
        if(tab_label[data.getDestination().getId()].cout==Double.MAX_VALUE) {
        	solution=new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
        	notifyDestinationReached(data.getDestination());
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
        
    }*/
}


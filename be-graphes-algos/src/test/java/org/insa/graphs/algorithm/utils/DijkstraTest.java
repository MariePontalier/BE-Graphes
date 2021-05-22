package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.AccessRestrictions;
import java.util.EnumMap;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.insa.graphs.model.*;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;


public class DijkstraTest {
	
	        // Petit graphe de test
			private static Graph graphe;

			// Liste de Noeuds
			private static Node[] noeuds;

			// Liste d'Arcs du graphe
			@SuppressWarnings("unused")
			private static Arc a2c, a2d, b2d, b2e, c2b, c2e, d2b, d2f, e2f; //a2c va du noeud A vers le noeud B. Je n'ai mis qu'un seul arc entre 2 noeuds pour simplifier

			//Liste de Chemins
			private static ShortestPathData Shortest, Fastest, Pietons, Inexistant, Vide;
	
	
	
	@BeforeClass
    public static void initAll() throws IOException {
		
		//création des autorisations de routes
		EnumMap<AccessRestrictions.AccessMode, AccessRestrictions.AccessRestriction> VelosPietonsUniquement = new EnumMap<>(AccessRestrictions.AccessMode.class);
		EnumMap<AccessRestrictions.AccessMode, AccessRestrictions.AccessRestriction> VoituresUniquement = new EnumMap<>(AccessRestrictions.AccessMode.class);
        EnumMap<AccessRestrictions.AccessMode, AccessRestrictions.AccessRestriction> ToutLeMondeEstAutoriseCestLaFete= new EnumMap<>(AccessRestrictions.AccessMode.class);
        for (AccessRestrictions.AccessMode mode : AccessRestrictions.AccessMode.ALL){ // Parcourir tous les types de véhicules
        	ToutLeMondeEstAutoriseCestLaFete.put(mode, AccessRestrictions.AccessRestriction.ALLOWED); // Les autoriser tous
            VelosPietonsUniquement.put(mode, AccessRestrictions.AccessRestriction.FORBIDDEN);  // Les interdire tous
            VoituresUniquement.put(mode, AccessRestrictions.AccessRestriction.FORBIDDEN);  // Les interdire tous  
        }
        VelosPietonsUniquement.put(AccessRestrictions.AccessMode.BICYCLE, AccessRestrictions.AccessRestriction.ALLOWED); // Rajouter les vélos
        VelosPietonsUniquement.put(AccessRestrictions.AccessMode.FOOT, AccessRestrictions.AccessRestriction.ALLOWED); // Rajouter les piétons
        VoituresUniquement.put(AccessRestrictions.AccessMode.MOTORCAR, AccessRestrictions.AccessRestriction.ALLOWED); // Autoriser que les vélos
        
        //création des types de routes
        RoadInformation routeVille  = new RoadInformation(RoadType.MOTORWAY,  new AccessRestrictions(VoituresUniquement), true, 50 , "");
        RoadInformation autoroute = new RoadInformation(RoadType.MOTORWAY,  new AccessRestrictions(VoituresUniquement), true, 130, "");
        RoadInformation promenade = new RoadInformation(RoadType.MOTORWAY,  new AccessRestrictions(VelosPietonsUniquement), true, 15, "");
        RoadInformation chemin  = new RoadInformation(RoadType.MOTORWAY,  new AccessRestrictions(ToutLeMondeEstAutoriseCestLaFete), true, 30 , "");

               
		//Création des noeuds
		 noeuds = new Node[6];
	        for (int i = 0; i < noeuds.length; ++i) {
	            noeuds[i] = new Node(i, null);
	        }
	    //Création des arcs    
		a2c = Node.linkNodes(noeuds[0], noeuds[2], 2, chemin, null);
		a2d = Node.linkNodes(noeuds[0], noeuds[3], 5, routeVille, null);
		b2d = Node.linkNodes(noeuds[1], noeuds[3], 3, promenade, null);
		b2e = Node.linkNodes(noeuds[1], noeuds[4], 7, autoroute, null);
		c2b = Node.linkNodes(noeuds[2], noeuds[1], 2, chemin, null);
		c2e = Node.linkNodes(noeuds[2], noeuds[4], 1, chemin, null);
		d2b = Node.linkNodes(noeuds[3], noeuds[1], 3, promenade, null);
		d2f = Node.linkNodes(noeuds[3], noeuds[5], 2, routeVille, null);
		e2f = Node.linkNodes(noeuds[4], noeuds[5], 3, chemin, null);

		//création du graphe
        graphe = new Graph("ID", "", Arrays.asList(noeuds), null);
        
        //création des chemins qu'on va utiliser pour les tests
        Shortest = new ShortestPathData(graphe, noeuds[0], noeuds[3], ArcInspectorFactory.getAllFilters().get(0)); // Chemin de a à d, shortest - all roads
        Fastest = new ShortestPathData(graphe, noeuds[1], noeuds[5], ArcInspectorFactory.getAllFilters().get(3)); // Chemin de b à f, fastest  - only open to cars
        Vide = new ShortestPathData(graphe, noeuds[0], noeuds[0], ArcInspectorFactory.getAllFilters().get(0)); // Chemin de a à a, shortest - all roads
        Inexistant = new ShortestPathData(graphe, noeuds[4], noeuds[1], ArcInspectorFactory.getAllFilters().get(0)); // Chemin de e à b, shortest - all roads
        Pietons = new ShortestPathData(graphe, noeuds[3], noeuds[5], ArcInspectorFactory.getAllFilters().get(4)); // Chemin de d à f, fastest - pedestrian. Chemin IMPOSSIBLE
	}
	
	DijkstraAlgorithm dijkstra;
	Path attendu, calcule;
	String nomDuTest;
	
	@Test
    public void testCheminQuiFonctionne() {
        // Shortest path
        nomDuTest = "Shortest path";
        dijkstra = new DijkstraAlgorithm(Shortest);
        attendu = Path.createShortestPathFromNodes(graphe, Arrays.asList(new Node[] { noeuds[0], noeuds[3] })); // a - d        
        calcule = dijkstra.run().getPath();
        assertTrue(("[" + nomDuTest + "] Attendu : " + attendu + "; Calculé : " +calcule), attendu.toString().compareTo(calcule.toString())==0);
    
        // Fastest path
        nomDuTest = "Fastest path";
        dijkstra = new DijkstraAlgorithm(Fastest);
        attendu = Path.createFastestPathFromNodes(graphe, Arrays.asList(new Node[] { noeuds[0], noeuds[5], noeuds[3] })); // a - d - f
        calcule = dijkstra.run().getPath();
        assertTrue(("[" + nomDuTest + "] Attendu : " + attendu + "; Calculé : " +calcule), attendu.toString().compareTo(calcule.toString())==0);
    }
	
	@Test
    public void testCheminQuiNeFonctionnePas() {
        // Chemin inexistant e - b 
        nomDuTest = "Chemin inexistant";
        dijkstra = new DijkstraAlgorithm(Inexistant);
        attendu = null;
        calcule = dijkstra.run().getPath();
        assertTrue(("[" + nomDuTest + "] Attendu : " + attendu + "; Calculé : " +calcule), attendu == calcule);

        // Chemin impossible (on demande un chemin piéton alors qu'il n'y a que des chemins en voiture)
        nomDuTest = "Chemin pour piétons";
        dijkstra = new DijkstraAlgorithm(Pietons);
        attendu = null;
        calcule = dijkstra.run().getPath();
        assertTrue(("[" + nomDuTest + "] Attendu : " + attendu + "; Calculé : " +calcule), attendu == calcule);

        // Chemin a - a
        nomDuTest = "Départ = Arrivée";
        dijkstra = new DijkstraAlgorithm(Vide);
        attendu = null;
        calcule = dijkstra.run().getPath();
        assertTrue(("[" + nomDuTest + "] Attendu : " + attendu + "; Calculé : " +calcule), attendu == calcule);
	}
	
	   @Test
	    public void testDijkstraVSBellman(){
	        // Shortest path
	        nomDuTest = "Long chemin";
	        dijkstra = new DijkstraAlgorithm(Shortest);
	        BellmanFordAlgorithm bellman = new BellmanFordAlgorithm(Shortest);
	        attendu = bellman.run().getPath();
	        calcule = dijkstra.run().getPath();
	        assertTrue(("[" + nomDuTest + "] Attendu : " + attendu + "; Calculé : " +calcule), attendu.getLength()==calcule.getLength());
	    }
}

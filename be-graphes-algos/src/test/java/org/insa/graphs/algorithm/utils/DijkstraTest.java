package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraTest {
	@BeforeClass
    public static void initAll() throws IOException {
	final String mapName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
    final String pathName = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";
 // Create a graph reader.
    final GraphReader reader = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
    // Read the graph.
    final Graph graph = reader.read();
	}
	
	@Test
	public void testBF() {
		fail("Not yet implemented");
	}

}

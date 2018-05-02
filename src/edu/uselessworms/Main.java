package edu.uselessworms;
import edu.uselessworms.runners.*;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Welcome to Homerville Vehicle Routing");
        DataLoader loader = new DataLoader();
        String filePath = System.getProperty("user.dir") + "\\src\\edu\\uselessworms\\cycle1.txt";
        System.out.println(filePath);
        loader.loadFile(filePath);
        System.out.println("Houses: " + loader.getNumHousesToVisit());
        System.out.println("Bart: " + loader.getBartPackages());
        System.out.println("Lisa: " + loader.getLisaPackages());
        SplitGraphClusterer clusterer = new SplitGraphClusterer(loader.getHousesToVisit(), 25);
        clusterer.cluster();
        for(int i = 0; i < 25; i++) {
            NearestNeighbor q = new NearestNeighbor(clusterer.clusters.get(i));
            SimulatedAnnealing a = new SimulatedAnnealing(q.run());
            a.run((int) 500);
            a.printTime("#" + i + " ");
        }


    }
}
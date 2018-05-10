package edu.uselessworms.clusterers;

import edu.uselessworms.locations.House;
import edu.uselessworms.pathfinding.SimulatedAnnealing;

import java.util.ArrayList;

public class NearestNeighborClusterer {
    public ArrayList<House> getHouses() {
        return houses;
    }

    private ArrayList<House> houses;

    public ArrayList<ArrayList<House>> getClusters() {
        return clusters;
    }

    private ArrayList<ArrayList<House>> clusters = new ArrayList<>();

    public NearestNeighborClusterer(ArrayList<House> houseL) {
        houses = houseL;
    }
    public void cluster(int numClusters) {
        //System.out.println(clusterSize);
        int hNum = 1;
        int cluster = 0;
        clusters.add(new ArrayList<>());
        if(houses.size() == 0)
            return;
        int[] clusterSizes = new int[numClusters];
        for(int i = 0; i < numClusters; i++) {
            clusters.add(new ArrayList<>());
            clusters.get(i).add(SimulatedAnnealing.DISTRO_CENTER);
            clusterSizes[i] = 1;
        }


        houses.remove(0);
        int shortestDistance;
        int shortestI = -1;
        int dist;
        while(houses.size() > 0) {
            shortestDistance = Integer.MAX_VALUE;
            for(int i=0; i<houses.size(); i++) {
                //System.out.println(clusters.get(cluster).size());
                dist = House.getDistance(houses.get(i), clusters.get(cluster).get(clusterSizes[cluster]-1));
                if(dist < shortestDistance) {
                    shortestI = i;
                    shortestDistance = dist;
                }
            }
            clusterSizes[cluster] += 1;
            clusters.get(cluster).add(houses.get(shortestI));
            houses.remove(shortestI);
            cluster++;
            if(cluster == numClusters)
                cluster = 0;

        }

        return;
    }
}

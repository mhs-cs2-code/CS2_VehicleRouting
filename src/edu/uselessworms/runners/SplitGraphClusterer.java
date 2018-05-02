package edu.uselessworms.runners;

import edu.uselessworms.locations.House;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SplitGraphClusterer {
    public ArrayList<House> houses;
    public ArrayList<ArrayList<House>> clusters = new ArrayList<ArrayList<House>>();
    int numberOfClusters;
    class ClusterSizeCompare implements Comparator<ArrayList<House>> {
        public int compare(ArrayList<House> o1, ArrayList<House> o2) {
            return (o1.size() > o2.size()) ? 1 : -1;
        }
    }
    public SplitGraphClusterer(ArrayList<House> h, int nOC) {
        houses = h;
        numberOfClusters = nOC;
    }
    public void cluster() {
        ArrayList<ArrayList<House>> midClusters = new ArrayList<ArrayList<House>>();
        int formSegs = (int) Math.ceil(Math.sqrt(numberOfClusters));
        int clusterSize = (50000/formSegs);
        int xCluster, yCluster;
        for(int i=0; i<Math.pow(formSegs,2); i++) {
            midClusters.add(new ArrayList<House>());
        }
        for(int i=0; i<houses.size(); i++) {
            xCluster = (int) houses.get(i).getX() / clusterSize;
            xCluster = (xCluster == formSegs ? formSegs-1: xCluster);
            yCluster = (int) houses.get(i).getY() / clusterSize;
            yCluster = (yCluster == formSegs ? formSegs-1: yCluster);
            midClusters.get(xCluster*formSegs + yCluster).add(houses.get(i));
        }
        clusters = midClusters;

    }
}

package edu.uselessworms.runners;

import edu.uselessworms.locations.House;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SplitGraphClusterer {
    private ArrayList<House> houses;
    public ArrayList<ArrayList<House>> clusters = new ArrayList<>();
    private int numberOfClusters;
    private int[] XY = new int[2];
    private int[] xyClusterSize = new int[2];
    class ClusterSizeCompare implements Comparator<ArrayList<House>> {
        public int compare(ArrayList<House> o1, ArrayList<House> o2) {
            if(o1.equals(o2))
                return 0;
            return (o1.size() > o2.size()) ? 1 : -1;
        }
    }
    public SplitGraphClusterer(ArrayList<House> h, int nOC) {
        houses = h;
        numberOfClusters = nOC;
        int xClusters = (int) Math.sqrt(numberOfClusters);
        while((numberOfClusters % xClusters) != 0) {
            xClusters -= 1;
        }
        int yClusters = nOC / xClusters;
        XY[0] = xClusters;
        XY[1] = yClusters;
        xyClusterSize[0] = 50000/xClusters;
        xyClusterSize[1] = 50000/yClusters;
    }
    private int whichCluster(int x, int y) {
        int xCluster, yCluster;
        xCluster = x /  xyClusterSize[0];
        xCluster = (xCluster == XY[0] ? XY[0]-1: xCluster);
        yCluster = y / xyClusterSize[1];
        yCluster = (yCluster == XY[1] ? XY[1]-1: yCluster);
        return xCluster*XY[1] + yCluster;
    }
    public void cluster() {
        ArrayList<ArrayList<House>> midClusters = new ArrayList<>();
        int formSegs = (int) Math.ceil(Math.sqrt(numberOfClusters));

        for(int i=0; i<Math.pow(formSegs,2); i++) {
            midClusters.add(new ArrayList<>());
        }
        for(House h : houses) {
            midClusters.get(whichCluster(h.getX(), h.getY())).add(h);
        }
        clusters = midClusters;

    }
}

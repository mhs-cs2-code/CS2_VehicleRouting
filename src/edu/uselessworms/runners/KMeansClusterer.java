package edu.uselessworms.runners;
import java.util.ArrayList;
import java.util.Arrays;

import edu.uselessworms.locations.*;

public class KMeansClusterer {
    ArrayList<House> houses;
    int numberOfMeans = 12;

    public ArrayList<House> getHouses() {
        return houses;
    }

    public void setHouses(ArrayList<House> houses) {
        this.houses = houses;
    }

    public int getNumberOfMeans() {
        return numberOfMeans;
    }

    public void setNumberOfMeans(int numberOfMeans) {
        this.numberOfMeans = numberOfMeans;
    }

    public KMeansClusterer(ArrayList<House> hs, int numMeans) {
        houses = hs;
        numberOfMeans = numMeans;
    }
    public void cluster() { // TODO: Implement
        cluster(8);
    }
    public void cluster(int iterations) { // TODO: Implement
        int[] housesToCluster = new int[houses.size()];
        int[][] centroids = new int[numberOfMeans][2];
        for(int i=0; i<numberOfMeans; i++) // Init with random centroids
        {
            int q = (int) (Math.random() * houses.size());
            centroids[i][0]= houses.get(q).getX();
            centroids[i][1]= houses.get(q).getY();
        }
        int maxDist = Integer.MAX_VALUE;
        int closestCentroid = -1;
        int curDist = -1;
        int[][][] centroidChange = new int[numberOfMeans][iterations][2];
        for(int i=0; i<iterations; i++) {
            int[] sumX = new int[numberOfMeans];
            int[] sumY = new int[numberOfMeans];
            int[] count = new int[numberOfMeans];
            housesToCluster = new int[houses.size()]; // Clustering
            for(int j=0; j<houses.size(); j++) { // For each house, find closest centroid
                closestCentroid = -1;
                maxDist = Integer.MAX_VALUE;
                curDist = -1;
                for (int h = 0; h < numberOfMeans; h++) { // go through all centoids
                    curDist = House.getDistancePoint(houses.get(j), centroids[h]);
                    if (curDist < maxDist) {
                        maxDist = curDist;
                        closestCentroid = h;
                    }
                }
                housesToCluster[j] = closestCentroid;
                sumX[closestCentroid] += houses.get(j).getX();
                sumY[closestCentroid] += houses.get(j).getY();
                count[closestCentroid] += 1;
            }
            // Calc new centroids
            System.out.println(Arrays.toString(count));
            for(int j=0; j<numberOfMeans; j++) {
                int newX = sumX[j] / count[j];
                int newY = sumY[j] / count[j];
                centroidChange[j][i][0] = centroids[j][0] - newX;
                centroidChange[j][i][1] = centroids[j][1] - newY;
                centroids[j][0] = newX;
                centroids[j][1] = newY;
                //
                //System.out.println("Change of X=" + centroidChange[j][i][0] + " / Y=" + centroidChange[j][i][1]);
            }
        }
        for(int i=0; i<numberOfMeans; i++) {
            System.out.println("Centroid#" + i + ": X=" + centroids[i][0] + " Y=" + centroids[i][1]);
        }
    }
}

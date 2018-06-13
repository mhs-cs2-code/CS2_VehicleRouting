package edu.uselessworms.pathfinding;

import edu.uselessworms.locations.House;

import java.util.ArrayList;

public class NearestNeighbor { // By Steph
    private final int MAX_TIME = 1330;
    ArrayList<House> availableHouses = new ArrayList<>();
    ArrayList<House> currentPath = new ArrayList<>();
    private int numberEmployeePerTruck;
    public NearestNeighbor(ArrayList<House> houses, int nep) {
        availableHouses = new ArrayList<>(houses);
        numberEmployeePerTruck = nep;
    }

    public void getNextPath() {
        double currentTime = 0;
        currentPath = new ArrayList<>();
        currentPath.add(SimulatedAnnealing.DISTRO_CENTER);
        while(currentTime < MAX_TIME && availableHouses.size() != 0) {
            int minID = -1;
            int minDistance = Integer.MAX_VALUE;
            for(int i = 0; i < availableHouses.size(); i++) {
                int dist = House.getDistance(availableHouses.get(i), currentPath.get(currentPath.size()-1));

                if(minDistance > dist) {
                    minDistance = dist;
                    minID = i;
                }
            }
            currentPath.add(availableHouses.get(minID));
            availableHouses.remove(minID);
            currentTime += House.getDistance(currentPath.get(currentPath.size()-1), currentPath.get(currentPath.size()-2)) / 2000.0;
            currentTime += (.5/numberEmployeePerTruck);
            //System.out.println(currentTime);
        }
    }
    public ArrayList<House> getPath() {
        return new ArrayList<>(currentPath);
    }
    public ArrayList<House> getPathWithoutDistro() {
        return new ArrayList<>(currentPath.subList(1, currentPath.size()));
    }
    public int housesLeft() {
        return availableHouses.size();
    }
}

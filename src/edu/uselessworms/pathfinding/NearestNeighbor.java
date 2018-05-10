package edu.uselessworms.pathfinding;

import edu.uselessworms.locations.House;

import java.util.ArrayList;
import java.util.List;

public class NearestNeighbor {
    public List<House> getHouses() {
        return houses;
    }

    private List<House> houses;

    public NearestNeighbor(List<House> houseL) {
        houses = houseL;
    }
    public ArrayList<House> run() {
        ArrayList<House> fullList = new ArrayList<>();
        if(houses.size() == 0)
            return fullList;
        fullList.add(houses.get(0));
        houses.remove(0);
        int shortestDistance;
        int shortestI = -1;
        int dist;
        int currentSize = 1;
        while(houses.size() > 0) {
            shortestDistance = Integer.MAX_VALUE;
            for(int i=0; i<houses.size(); i++) {
                dist = House.getDistance(houses.get(i), fullList.get(currentSize-1));
                if(dist < shortestDistance) {
                    shortestI = i;
                    shortestDistance = dist;
                }
            }
            fullList.add(houses.get(shortestI));
            houses.remove(shortestI);

        }

        return fullList;
    }
}
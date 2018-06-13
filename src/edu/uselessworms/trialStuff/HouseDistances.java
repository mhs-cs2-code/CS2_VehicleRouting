package edu.uselessworms.trialStuff;

import edu.uselessworms.locations.House;
import edu.uselessworms.pathfinding.SimulatedAnnealing;

public class HouseDistances {
    public static void main(String args[]) {
        int x = House.getDistance(new House(1,1, "B"), new House(2,2, "J"));
        System.out.println(x);
        System.out.println(new House(1,1, "B").getX());
        System.out.println(new House(1,1, "B").getY());
    }
}

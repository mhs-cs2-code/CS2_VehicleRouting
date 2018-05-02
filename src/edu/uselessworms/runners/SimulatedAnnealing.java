package edu.uselessworms.runners;

import edu.uselessworms.locations.House;
import java.util.ArrayList;

public class SimulatedAnnealing {
    double initialTemp = 200;
    double tempRegression = 0.85;

    public ArrayList<House> getHouses() {
        return houses;
    }

    ArrayList<House> houses;
    private double tempAtI(int i) {
        return (double) initialTemp * Math.pow(tempRegression, i);
    }
    private double acceptenceProb(double energyDelta, double temp) {
        return 1 / (1 + Math.exp(energyDelta / temp));
    }
    private int getEnergyOfPath(ArrayList<House> list) {
        int energy = 0;
        for(int i = 1; i < list.size(); i++) {
            energy += House.getDistance(list.get(i), list.get(i-1));
        }
        return energy;
    }
    private int getTimeOfPath(ArrayList<House> list) {
        int time = 0;
        time += (getEnergyOfPath(list) / 2000.0) + list.size();
        return time;

    }
    public SimulatedAnnealing(ArrayList<House> houseList) {
        houses = houseList;
    }

    public void run(int iterations) {
        double curTemp = initialTemp;
        int swapA, swapB, newEnergy, oldEnergy;

        for(int i=0; i<iterations; i++) {
            curTemp = this.tempAtI(i);
            ArrayList<House> swaped = houses;
            swapA = (int) Math.random() * houses.size();
            swapB = (int) Math.random() * houses.size();
            House swapAA = houses.get(swapA);
            House swapBB = houses.get(swapB);
            swaped.set(swapA, swapBB);
            swaped.set(swapB, swapAA);
            oldEnergy = getEnergyOfPath(houses);
            newEnergy = getEnergyOfPath(swaped);
            if(newEnergy < oldEnergy || acceptenceProb((newEnergy - oldEnergy), curTemp) > Math.random())
                houses = swaped;
        }
    }
    public void printPath() {
        for(int i = 0; i < houses.size(); i++)
        {
            if(i==0)
                System.out.println("House: (" + houses.get(i).getX() + "," + houses.get(i).getY() + ")");
            else
                System.out.println("House: (" + houses.get(i).getX() + "," + houses.get(i).getY() + ") " + House.getDistance(houses.get(i), houses.get(i-1)));

        }
    }
    public void printDistance() {
        System.out.println("Number of Houses : " + houses.size());
        System.out.println("Total Distance (Feet) : " + getEnergyOfPath(houses));
        System.out.println("Total Distance (Miles) : " + getEnergyOfPath(houses) / 5000.0);
    }
    public void printTime(String add) {
        System.out.println(add + "Total Time (Hours) : " + getTimeOfPath(houses) / 60.0);
    }

}

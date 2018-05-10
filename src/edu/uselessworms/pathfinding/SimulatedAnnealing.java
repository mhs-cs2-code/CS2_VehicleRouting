package edu.uselessworms.pathfinding;

import edu.uselessworms.locations.House;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class SimulatedAnnealing {
    private double initialTemp = 200;
    private double tempRegression = 0.85;
    public static final House DISTRO_CENTER = new House(125,25,"A");

    public ArrayList<House> getHouses() {
        return houses;
    }

    private ArrayList<House> houses;
    private double tempAtI(int i) {
        return initialTemp * Math.pow(tempRegression, i);
    }
    private double acceptenceProb(double energyDelta, double temp) {
        return 1 / (1 + Math.exp(energyDelta / temp));
    }
    public int getEnergyOfPath() {
        if(houses.size() == 0)
            return 0;
        int energy = 0;
        for(int i = 1; i < houses.size(); i++) {
            energy += House.getDistance(houses.get(i), houses.get(i-1));
        }
        energy += House.getDistance(DISTRO_CENTER, houses.get(0));
        energy += House.getDistance(DISTRO_CENTER, houses.get(houses.size()-1));
        return energy;
    }
    private int getEnergyOfPath(ArrayList<House> list) {
        if(list.size() == 0)
            return 0;
        int energy = 0;
        for(int i = 1; i < list.size(); i++) {
            energy += House.getDistance(list.get(i), list.get(i-1));
        }
        energy += House.getDistance(DISTRO_CENTER, list.get(0));
        energy += House.getDistance(DISTRO_CENTER, list.get(list.size()-1));
        return energy;
    }
    public int getTimeOfPath() {
        if(houses.size() == 0)
            return 0;
        double time = 0;
        time += (getEnergyOfPath() / 2000.0) + 0.5;
        return (int) time;
    }
    public int getEmployeeCost() {
        if(houses.size() == 0)
            return 0;
        int time = getTimeOfPath();
        double hours = (time/60.0);
        //System.out.println(hours);
        if(hours > 8) {
            return 240 + (int) (hours-8)*45;
        }
        return (int) (hours)*30;
    }
    public SimulatedAnnealing(ArrayList<House> houseList, int shuffle) {
        houses = houseList;
        if(shuffle!=0)
            Collections.shuffle(houses);
    }

    public void run(int iterations) {
        double curTemp;
        int swapA, swapB, newEnergy, oldEnergy;

        for(int i=0; i<iterations; i++) {
            curTemp = this.tempAtI(i);
            ArrayList<House> swaped = new ArrayList<>(houses);
            swapA = (int) (Math.random() * houses.size());
            swapB = (int) (Math.random() * houses.size());
            swaped.set(swapA, houses.get(swapB));
            swaped.set(swapB, houses.get(swapA));
            oldEnergy = getEnergyOfPath();
            newEnergy = getEnergyOfPath(swaped);
            if(newEnergy < oldEnergy)// || acceptenceProb((newEnergy - oldEnergy), curTemp) > Math.random())
                houses = swaped;
            //System.out.println(oldEnergy);
        }
    }
    public String linePathPrint() {
        String path = "(";
        path += houses.size();
        path += ") (";
        path += getTimeOfPath() / 60.0;
        path += ") distro ";
        House oldHouse = DISTRO_CENTER;
        for(House i : houses) {
            path += House.getDistance(oldHouse, i);
            path += " ";
            path += i.toString();
            path += " ";
            oldHouse = i;
        }
        path += House.getDistance(oldHouse, DISTRO_CENTER);
        path += " ";
        path += "distro";
        path += " -- time: " + getTimeOfPath() + " -- employee cost " + getEmployeeCost();
        return path;
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
    public void printDistance(String add) {
        System.out.println(add + "Total Distance (Miles) : " + getEnergyOfPath() / 5000.0);
    }
    public void printTime(String add) {
        System.out.println(add + "Total Time (Hours) : " + getTimeOfPath() / 60.0);
    }
    public void printCost(String add) {
        System.out.println(add + "Employee Cost ($) : " + getEmployeeCost());
    }

}

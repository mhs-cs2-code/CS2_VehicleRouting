package edu.uselessworms.pathfinding;

import edu.uselessworms.locations.House;
import edu.uselessworms.solvers.Solver;

import java.util.ArrayList;
import java.util.Collections;


public class SimulatedAnnealing {
    public static final House BART_COMPLEX;
    public static final House LISA_COMPLEX;
    public static final House DISTRO_CENTER;
    static {
        BART_COMPLEX = new House(3, 2, "A");
        LISA_COMPLEX = new House(34, 149, "A");
        DISTRO_CENTER = new House(125, 25, "A");
    }

    private int numEmployeeTruck;
    private int bartNeeded; // num of packages
    private int lisaNeeded;
    private ArrayList<House> houses;

    public ArrayList<House> getHouses() {
        return houses;
    }

    private double tempAtI(int i) {
        double initialTemp = 200;
        double tempRegression = 0.85;
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
        time += (getEnergyOfPath() / Solver.FEET_PER_MINUTE) + (0.5 / numEmployeeTruck)*(bartNeeded+lisaNeeded) + (1/numEmployeeTruck)*(houses.size()-2);
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
    public SimulatedAnnealing(ArrayList<House> houseList) {
        houses = houseList;
        numEmployeeTruck = -1;
        bartNeeded = 0;
        lisaNeeded = 0;
    }
    public SimulatedAnnealing(ArrayList<House> houseList, int shuffle) {
        houses = houseList;
        if(shuffle!=0)
            Collections.shuffle(houses);
        numEmployeeTruck = -1;
        bartNeeded = 0;
        lisaNeeded = 0;
    }

    public void run(int iterations, int nep) {
        double curTemp;
        int swapA, swapB, newEnergy, oldEnergy;
        numEmployeeTruck = nep;
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
        path += " -- distance: " + getEnergyOfPath()/5000.0;
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
    private int findIndexClosestTo(House x) {
        x = new House(x);
        int i = 0;
        int minDist = Integer.MAX_VALUE;
        int minID = -1;
        for(House q : new ArrayList<>(houses)) {
            int d = House.getDistance(new House(q), new House(x));
            if(d > 1 && d < minDist) {
                minDist = d;
                minID = i;
            }
            i++;
        }

        return minID;
    }
    public void addComplexes(int bart, int lisa) {
        bartNeeded = bart;
        lisaNeeded = lisa;

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

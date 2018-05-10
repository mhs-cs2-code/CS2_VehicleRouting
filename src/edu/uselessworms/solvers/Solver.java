package edu.uselessworms.solvers;

import edu.uselessworms.locations.House;
import edu.uselessworms.pathfinding.SimulatedAnnealing;
import edu.uselessworms.runners.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Solver {
    private static final int TRUCK_OWN_PRICE = 100000;
    private static final int TRUCK_RENT_PRICE = 15000;
    private static final int HOUSES_PER_TRUCK = 450;
    private static final double MILES_PER_DOLLAR_FUEL = 1000.0;
    private static final int SIMULATED_ANNEALING_ITERATIONS = 30000;
    private static final int FEET_PER_MILE = 5000;
    private static final String BASE_FILE_PATH = "\\src\\edu\\uselessworms\\";
    private static final String BASE_SAVE_PATH = "solvedData\\";
    private static final String BASE_OPEN_PATH = "cycleData\\";
    static public NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
    private PrintWriter problemWriter;

    private int ownedTrucks;
    private int numberOfCycles;
    private double totalPrice;
    private int cycleID = 1;
    private ArrayList<Double> milesOwned;
    private DataLoader loadData(String filePath) throws FileNotFoundException {
        DataLoader x = new DataLoader();
        x.loadFile(System.getProperty("user.dir") + filePath);
        return x;
    }
    private static void saveAndOutput(PrintWriter x, String y) {
        System.out.println(y);
        x.println(y);
    }
    private static void saveAndOutput(PrintWriter x, PrintWriter a, String y) {
        System.out.println(y);
        x.println(y);
        a.println(y);
    }
    private static void doubleSave(PrintWriter x, PrintWriter a, String y) {
        x.println(y);
        a.println(y);
    }
    private double calculateCycle(DataLoader data) throws FileNotFoundException, UnsupportedEncodingException {
        double cycleCost = 0;
        double gasPrice=0, employeeCost=0, totalTime=0, packagesDelivered;
        ArrayList<House> houses = new ArrayList<>(data.getHousesToVisit());
        packagesDelivered = data.getBartPackages() + data.getLisaPackages() + data.getNumHousesToVisit();
        PrintWriter cyclewriter = new PrintWriter(System.getProperty("user.dir") + BASE_FILE_PATH + BASE_SAVE_PATH + "solved" + cycleID + ".txt", "UTF-8");
        saveAndOutput(problemWriter,"");
        saveAndOutput(problemWriter, cyclewriter,"Cycle #" + cycleID + " (" + data.getNumHousesToVisit() + ")");
        saveAndOutput(problemWriter, cyclewriter,"------------------------------------------");
        int totalDistance = 0;
        double truckTime = 0;
        int trucksUsed = 0;
        ArrayList<ArrayList<House>> paths = new ArrayList<>();
        while(truckTime < 1440) {
            trucksUsed++;
            paths.add(new ArrayList<>());
            paths.get(trucksUsed-1).add(SimulatedAnnealing.DISTRO_CENTER);
            int closestHouse = getClosest(houses, paths.get(trucksUsed-1).get(paths.get(trucksUsed-1).size()-1));
            int dist = House.getDistance(paths.get(trucksUsed-1).get(paths.get(trucksUsed-1).size()-1), houses.get(closestHouse)) / 2000;
            totalDistance += dist;
            truckTime += dist/2000.0;
            totalTime += dist/2000.0;
            paths.get(trucksUsed-1).add(houses.get(closestHouse));
            houses.remove(closestHouse);
        }

        int numRented = Math.max(0, ownedTrucks-trucksUsed);
        int costRented = numRented  * TRUCK_RENT_PRICE;
        cycleCost += costRented;
        cycleCost += gasPrice;
        cycleCost += employeeCost;
        cyclewriter.println();
        saveAndOutput(problemWriter, cyclewriter,"Purchased Trucks: $0");
        saveAndOutput(problemWriter, cyclewriter,"Rented Trucks (" + numRented + "): " + moneyFormat.format(costRented));
        saveAndOutput(problemWriter, cyclewriter,"Gas Price: " + moneyFormat.format(gasPrice));
        saveAndOutput(problemWriter, cyclewriter,"Employee Cost (" + (trucksUsed) + "): " + moneyFormat.format(employeeCost));
        saveAndOutput(problemWriter, cyclewriter,"Packages Delivered: " + packagesDelivered);
        saveAndOutput(problemWriter, cyclewriter,"Total Time (hours): " + new DecimalFormat("#0.00").format(totalTime / 60.0));
        saveAndOutput(problemWriter, cyclewriter,"Total Price For Cycle #" + cycleID + ": " + moneyFormat.format(cycleCost));
        cyclewriter.close();
        return cycleCost;
    }
    private int getClosest(ArrayList<House> housesAvailable, House from) { // TODO IMPLEMENTs
        return 0;
    } // TODO: Implement
    public void addTruckWork() {
        double added= 0;
        for(int i=0; i < ownedTrucks; i++) {
            added += Math.floor(milesOwned.get(i) / 100) * 1000;
        }
        totalPrice += added;
        saveAndOutput(this.problemWriter, "");
        saveAndOutput(this.problemWriter, "Added " + moneyFormat.format(added) + " for truck maintenance");
    }
    public Solver(int num, int own) throws FileNotFoundException, UnsupportedEncodingException {
        numberOfCycles = num;
        ownedTrucks = own;
        milesOwned = new ArrayList<>(ownedTrucks);
        for(int i = 0; i < ownedTrucks; i++) {
            milesOwned.add(0.0);
        }
        totalPrice += TRUCK_OWN_PRICE * ownedTrucks;
        problemWriter = new PrintWriter(System.getProperty("user.dir") + BASE_FILE_PATH + BASE_SAVE_PATH + "solvedProblem.txt", "UTF-8");
        saveAndOutput(this.problemWriter, "At Cycle 0: Purchased " + ownedTrucks + " trucks for a price of $" + TRUCK_OWN_PRICE * ownedTrucks);

    }
    public void addAndRunCycle(String filePath) throws FileNotFoundException, UnsupportedEncodingException {
        DataLoader data = new DataLoader();
        data.loadFile(System.getProperty("user.dir") + BASE_FILE_PATH + BASE_OPEN_PATH + filePath);
        double cyclePrice = this.calculateCycle(data);
        totalPrice += cyclePrice;
        cycleID += 1;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void end() {
        problemWriter.println();
        problemWriter.println("Total Price: " + moneyFormat.format(totalPrice));
        this.problemWriter.close();
    }
}

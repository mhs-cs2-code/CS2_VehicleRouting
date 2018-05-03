package edu.uselessworms.solvers;

import edu.uselessworms.runners.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Solver {
    private static final int TRUCK_OWN_PRICE = 100000;
    private static final int TRUCK_RENT_PRICE = 15000;
    private static final int HOUSES_PER_TRUCK = 230;
    private static final double MILES_PER_DOLLER_FUEL = 1000.0;
    private static final int SIMULATED_ANNEALING_ITERATIONS = 0;
    private static final int FEET_PER_MILE = 5000;
    private static final String BASE_FILE_PATH = "\\src\\edu\\uselessworms\\";
    private static final String BASE_SAVE_PATH = "solvedData\\";
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
        packagesDelivered = data.getBartPackages() + data.getLisaPackages() + data.getNumHousesToVisit();
        PrintWriter cyclewriter = new PrintWriter(System.getProperty("user.dir") + BASE_FILE_PATH + BASE_SAVE_PATH + "solved" + cycleID + ".txt", "UTF-8");
        saveAndOutput(problemWriter,"");
        saveAndOutput(problemWriter, cyclewriter,"Cycle #" + cycleID);
        saveAndOutput(problemWriter, cyclewriter,"------------------------------------------");
        // Calculate Number of Clusters to Form, about 130 houses per truck
        int numClusters  = data.getNumHousesToVisit() / HOUSES_PER_TRUCK;
        numClusters = (numClusters % 2 == 0) ? numClusters : numClusters + 1; // Make # of clusters even
        SplitGraphClusterer clusterer = new SplitGraphClusterer(data.getHousesToVisit(), numClusters);
        clusterer.cluster();
        double[] distances = new double[numClusters+1];

        for(int i = 0; i < numClusters; i++) {
            NearestNeighbor q = new NearestNeighbor(clusterer.clusters.get(i));
            SimulatedAnnealing a = new SimulatedAnnealing(q.run());
            a.run(SIMULATED_ANNEALING_ITERATIONS);
            employeeCost += a.getEmployeeCost(); // employee cost
            gasPrice += a.getEnergyOfPath() / MILES_PER_DOLLER_FUEL; // $5 per mile, $1 per 1000ft
            if(a.getTimeOfPath() / 60.0 > 23) {
                System.out.println("INVALID");
            }
            totalTime += a.getTimeOfPath();
            distances[i] = a.getEnergyOfPath() / FEET_PER_MILE;
            cyclewriter.println("Cluster #" + (i+1) + " - " + a.linePathPrint());
        }
        ComplexTruck complexTruckSolver = new ComplexTruck(data.getBartPackages(), data.getLisaPackages());
        complexTruckSolver.solve();
        employeeCost += complexTruckSolver.getEmployeeCost();
        gasPrice += complexTruckSolver.getTruckDistance() / MILES_PER_DOLLER_FUEL;
        totalTime += complexTruckSolver.getTruckTime();
        distances[numClusters] = complexTruckSolver.getTruckDistance();
        Arrays.sort(distances);
        Collections.sort(milesOwned);
        Collections.reverse(milesOwned); // add lowest miles to truck with most miles - minimize work
        for(int i=0; i < Math.min(numClusters, ownedTrucks); i++) {
            milesOwned.set(i, milesOwned.get(i) + distances[i]);
        }
        int numRented = Math.max(0, numClusters-ownedTrucks+1);
        int costRented = numRented  * TRUCK_RENT_PRICE;
        cycleCost += costRented;
        cycleCost += gasPrice;
        cycleCost += employeeCost;
        cyclewriter.println();
        saveAndOutput(problemWriter, cyclewriter,"Purchased Trucks: $0");
        saveAndOutput(problemWriter, cyclewriter,"Rented Trucks (" + numRented + "): " + moneyFormat.format(costRented));
        saveAndOutput(problemWriter, cyclewriter,"Gas Price: " + moneyFormat.format(gasPrice));
        saveAndOutput(problemWriter, cyclewriter,"Employee Cost (" + (numClusters+1) + "): " + moneyFormat.format(employeeCost));
        saveAndOutput(problemWriter, cyclewriter,"Packages Delivered: " + packagesDelivered);
        saveAndOutput(problemWriter, cyclewriter,"Total Time (hours): " + new DecimalFormat("#0.00").format(totalTime / 60.0));
        saveAndOutput(problemWriter, cyclewriter,"Total Price For Cycle #" + cycleID + ": " + moneyFormat.format(cycleCost));
        cyclewriter.close();
        return cycleCost;
    }
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
        data.loadFile(System.getProperty("user.dir") + BASE_FILE_PATH + filePath);
        double cyclePrice = this.calculateCycle(data);
        totalPrice += cyclePrice;
        cycleID += 1;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void end() {
        problemWriter.println();
        problemWriter.println("Total Price: " + moneyFormat.format(totalPrice));
        this.problemWriter.close();
    }
}

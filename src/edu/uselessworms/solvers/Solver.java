package edu.uselessworms.solvers;

import edu.uselessworms.runners.*;

import java.io.FileNotFoundException;
import java.text.NumberFormat;

public class Solver {
    private static final int TRUCK_OWN_PRICE = 100000;
    private static final int TRUCK_RENT_PRICE = 15000;
    private static final int HOUSES_PER_TRUCK = 230;
    private static final double MILES_PER_DOLLER_FUEL = 1000.0;
    private static final int SIMULATED_ANNEALING_ITERATIONS = 0;
    static public NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();

    int ownedTrucks = 0;
    private double totalPrice = 0;
    private int cycleID = 1;
    private DataLoader loadData(String filePath) throws FileNotFoundException {
        DataLoader x = new DataLoader();
        x.loadFile(System.getProperty("user.dir") + filePath);
        return x;
    }
    private double calculateCycle(DataLoader data) {
        double cycleCost = 0;
        // Calculate Number of Clusters to Form, about 130 houses per truck
        int numClusters  = data.getNumHousesToVisit() / HOUSES_PER_TRUCK;
        numClusters = (numClusters % 2 == 0) ? numClusters : numClusters + 1; // Make # of clusters even
        SplitGraphClusterer clusterer = new SplitGraphClusterer(data.getHousesToVisit(), numClusters);
        clusterer.cluster();
        for(int i = 0; i < numClusters; i++) {
            NearestNeighbor q = new NearestNeighbor(clusterer.clusters.get(i));
            SimulatedAnnealing a = new SimulatedAnnealing(q.run());
            a.run(SIMULATED_ANNEALING_ITERATIONS);
            cycleCost += (a.getEnergyOfPath() != 0) ? TRUCK_RENT_PRICE : 0; // truck rental
            cycleCost += a.getEmployeeCost(); // employee cost
            cycleCost += a.getEnergyOfPath() / MILES_PER_DOLLER_FUEL; // $5 per mile, $1 per 1000ft
            if(a.getTimeOfPath() / 60.0 > 23) {
                System.out.println("INVALID");
            }
        }
        ComplexTruck complexTruckSolver = new ComplexTruck(data.getBartPackages(), data.getLisaPackages());
        complexTruckSolver.solve();
        cycleCost += TRUCK_RENT_PRICE; // truck rental
        cycleCost += complexTruckSolver.getEmployeeCost();
        return cycleCost;
    }
    public Solver() {

    }
    public void addAndRunCycle(String filePath) throws FileNotFoundException {
        DataLoader data = new DataLoader();
        data.loadFile(System.getProperty("user.dir") + "\\src\\edu\\uselessworms\\" + filePath);
        double cyclePrice = this.calculateCycle(data);
        System.out.println("#" + cycleID + " ($) : " + moneyFormat.format(cyclePrice));
        totalPrice += cyclePrice;
        cycleID += 1;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

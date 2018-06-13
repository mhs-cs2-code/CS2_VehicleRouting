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
    private static final int MINUTES_IN_DAY = 1440;
    private static final int MILES_PER_MAINT = 100;
    private static final int COST_PER_MAINT = 1000;
    public  static final double FEET_PER_MINUTE = 2000.0;
    private static final double MINUTES_PER_PACKAGE = 1;
    private static final double MINUTES_PER_COMPLEX_PACKAGE = 0.5;
    private static final double FEET_PER_DOLLAR_FUEL = 1000.0;
    private static final int SIMULATED_ANNEALING_ITERATIONS = 5000;
    private static final int FEET_PER_MILE = 5000;
    private static final String BASE_FILE_PATH = "\\src\\edu\\uselessworms\\";
    private static final String BASE_SAVE_PATH = "solvedData\\";
    private static final String BASE_OPEN_PATH = "cycleData\\";
    public  static       NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
    private              PrintWriter problemWriter;
    private              int numberEmployeePerTruck;
    private              int ownedTrucks;
    private              double totalPrice;
    private              int cycleID = 1;
    private              int feetOwned;
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
        ArrayList<Integer> distances = new ArrayList<>();
        int bLeft = data.getBartPackages(), lLeft = data.getLisaPackages();
        int activeB = 0, activeL = 0;
        int bID = 0, lID = 0;
        if(bLeft > 0)
            houses.add(SimulatedAnnealing.BART_COMPLEX);
        if(lLeft > 0)
            houses.add(SimulatedAnnealing.LISA_COMPLEX);
        while(houses.size() > 0) {
            trucksUsed++;
            paths.add(new ArrayList<>());
            paths.get(trucksUsed - 1).add(SimulatedAnnealing.DISTRO_CENTER);
            truckTime = 0;
            int maxTime = 1740;
            while (truckTime < maxTime && houses.size() > 0) {
                int lastHouseID = paths.get(trucksUsed - 1).size() - 1;
                House lastHouse = paths.get(trucksUsed - 1).get(lastHouseID);

                if(bLeft > 0 && activeB > 0 && bID % 2 == 0) {
                    paths.get(trucksUsed - 1).add(SimulatedAnnealing.BART_COMPLEX);
                    double dist = House.getDistance(lastHouse, SimulatedAnnealing.BART_COMPLEX);
                    totalDistance += dist;
                    truckTime += dist / FEET_PER_MINUTE;
                    bLeft -= 100;
                    activeB += 1;
                    truckTime += (0.5 / numberEmployeePerTruck) * (100);
                }
                else if(lLeft > 0 && activeL > 0 && lID % 2 == 0) {
                    paths.get(trucksUsed - 1).add(SimulatedAnnealing.LISA_COMPLEX);
                    double dist = House.getDistance(lastHouse, SimulatedAnnealing.LISA_COMPLEX);
                    totalDistance += dist;
                    truckTime += dist / FEET_PER_MINUTE;
                    bLeft -= 100;
                    activeB += 1;
                    truckTime += (0.5 / numberEmployeePerTruck) * (100);
                }
                else {
                    int closestHouse = getClosest(houses, lastHouse);
                    double dist = House.getDistance(lastHouse, houses.get(closestHouse));
                    totalDistance += dist;
                    truckTime += dist / FEET_PER_MINUTE;
                    truckTime += (MINUTES_PER_PACKAGE / numberEmployeePerTruck);
                    totalDistance += dist;
                    paths.get(trucksUsed - 1).add(houses.get(closestHouse));
                    if (House.isSame(houses.get(closestHouse), SimulatedAnnealing.BART_COMPLEX)) {
                        bLeft -= 100;
                        activeB += 1;
                        truckTime += (MINUTES_PER_COMPLEX_PACKAGE / numberEmployeePerTruck) * (100);
                    }
                    if (House.isSame(houses.get(closestHouse), SimulatedAnnealing.LISA_COMPLEX)) {
                        lLeft -= 100;
                        activeL += 1;
                        truckTime += (MINUTES_PER_COMPLEX_PACKAGE / numberEmployeePerTruck) * (100);
                    }
                    houses.remove(closestHouse);
                }
                if(activeB == 1) {
                    bID += 1;
                }
                if(activeL == 1) {
                    lID += 1;
                }
            }

            SimulatedAnnealing truckAnneal = new SimulatedAnnealing(new ArrayList<>(paths.get(trucksUsed - 1).subList(1,paths.get(trucksUsed - 1).size())));
            truckAnneal.run(SIMULATED_ANNEALING_ITERATIONS, numberEmployeePerTruck);
            totalDistance += truckAnneal.getEnergyOfPath();
            totalTime += truckAnneal.getTimeOfPath();
            if(truckAnneal.getTimeOfPath() > MINUTES_IN_DAY)
                System.out.println("INVALID");
            employeeCost += truckAnneal.getEmployeeCost()*numberEmployeePerTruck;
            gasPrice += truckAnneal.getEnergyOfPath()/FEET_PER_DOLLAR_FUEL;
            distances.add(truckAnneal.getEnergyOfPath());
            doubleSave(problemWriter, cyclewriter, truckAnneal.linePathPrint());

        }
        int numRented = Math.max(0, trucksUsed-ownedTrucks);
        int costRented = numRented  * TRUCK_RENT_PRICE;
        for(int q = 1; q < ownedTrucks+1; q++) {
            try {
                feetOwned += distances.get(distances.size() - q);
            }
            catch(Exception e){}
        }
        cycleCost += costRented;
        cycleCost += gasPrice;
        cycleCost += employeeCost;
        cyclewriter.println();
        saveAndOutput(problemWriter, cyclewriter,"Purchased Trucks: $0");
        saveAndOutput(problemWriter, cyclewriter,"Rented Trucks (" + numRented + "): " + moneyFormat.format(costRented));
        saveAndOutput(problemWriter, cyclewriter,"Gas Price: " + moneyFormat.format(gasPrice));
        saveAndOutput(problemWriter, cyclewriter,"Employee Cost (" + (trucksUsed*numberEmployeePerTruck) + "): " + moneyFormat.format(employeeCost));
        saveAndOutput(problemWriter, cyclewriter,"Packages Delivered: " + packagesDelivered);
        saveAndOutput(problemWriter, cyclewriter,"Total Time (hours): " + new DecimalFormat("#0.00").format(totalTime / 60.0));
        saveAndOutput(problemWriter, cyclewriter,"Total Price For Cycle #" + cycleID + ": " + moneyFormat.format(cycleCost));
        cyclewriter.close();
        return cycleCost;
    }
    private int getClosest(ArrayList<House> housesAvailable, House from) { // TODO IMPLEMENTs
        int minDist = Integer.MAX_VALUE;
        int minID = -1;
        ArrayList<House> newList = new ArrayList<House>(housesAvailable);
        int housesAvailNum = newList.size();
        for(int i = 0; i < housesAvailNum; i++) {
            int dist = House.getDistance(from, newList.get(i));
            if(dist < minDist) {
                minDist = dist;
                minID = i;
            }
        }
        return minID;
    }
    public void addTruckWork() {
        int miles = feetOwned / FEET_PER_MILE;
        int timesAdded = miles / MILES_PER_MAINT; // 100 miles per
        int moneyAdded = timesAdded * COST_PER_MAINT;
        totalPrice += moneyAdded;
        saveAndOutput(this.problemWriter, "");
        saveAndOutput(this.problemWriter, "Added " + moneyFormat.format(moneyAdded) + " for truck maintenance");
    }
    public Solver(int own, int ept) throws FileNotFoundException, UnsupportedEncodingException {
        ownedTrucks = own;
        numberEmployeePerTruck = ept;
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

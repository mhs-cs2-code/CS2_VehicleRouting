package edu.uselessworms.pathfinding;

public class ComplexTruck {
    private static int DistDistroToBart = 47400; // Minutes
    private static int DistDistroToLisa = 142400;
    private static int DistBartToLisa = 153000;
    private static int DistClosestHouse = 1000;
    private int bartPackages;
    private int lisaPackages;
    private int truckDistance;
    private int truckTime;

    public int getTruckDistance() {
        return truckDistance;
    }

    public void setTruckDistance(int truckDistance) {
        this.truckDistance = truckDistance;
    }

    public int getTruckTime() {
        return truckTime;
    }

    public void setTruckTime(int truckTime) {
        this.truckTime = truckTime;
    }

    public ComplexTruck(int b, int l) {
        bartPackages = b;
        lisaPackages = l;
    }
    public int getEmployeeCost() {
        int time = truckTime;
        double hours = time/60.0;
        if(hours > 8) {
            return 240 + (int) Math.ceil(hours-8)*45;
        }
        return (int) Math.ceil(hours)*30;
    }
    public void solve() {
        int dist = 0;
        dist += DistDistroToBart;
        dist += DistBartToLisa;
        dist += DistDistroToLisa;
        dist += Math.ceil(bartPackages / 100.0) * DistClosestHouse;
        dist += Math.ceil(truckTime / 100.0) * DistClosestHouse;
        truckDistance = dist;
        truckTime = (int) ((dist / 2000) + 0.5*bartPackages + 0.5*lisaPackages);
    }
}

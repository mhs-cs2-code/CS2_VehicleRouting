package edu.uselessworms.runners;
import edu.uselessworms.locations.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataLoader {
    private int cycleNumber;
    private int numHousesToVisit;
    private ArrayList<House> housesToVisit = new ArrayList<>();
    private int bartPackages;
    private int lisaPackages;

    public int getCycleNumber() {
        return cycleNumber;
    }

    public int getNumHousesToVisit() {
        return numHousesToVisit;
    }

    public ArrayList<House> getHousesToVisit() {
        return housesToVisit;
    }

    public int getBartPackages() {
        return bartPackages;
    }

    public int getLisaPackages() {
        return lisaPackages;
    }

    public DataLoader() {
    }
    public void loadFile(String filepath) throws FileNotFoundException {
        File cyclefile = new File(filepath);
        Scanner reader = new Scanner(cyclefile);
        cycleNumber = reader.nextInt();
        reader.next();
        String nextHouse = reader.next();
        String[] nextHouseParts;
        while(!nextHouse.equals("Bart"))
        {
            nextHouseParts = nextHouse.split(",");
            nextHouseParts[0] = nextHouseParts[0].substring(0, nextHouseParts[0].length() - 1);
            nextHouseParts[1] = nextHouseParts[1].substring(0, nextHouseParts[1].length() - 1);
            housesToVisit.add(new House(Integer.parseInt(nextHouseParts[0]), Integer.parseInt(nextHouseParts[1]), nextHouseParts[2]));
            nextHouse = reader.next();
            numHousesToVisit++;
        }
        reader.next();
        bartPackages = reader.nextInt();
        reader.next();
        reader.next();
        lisaPackages = reader.nextInt();
    }

}

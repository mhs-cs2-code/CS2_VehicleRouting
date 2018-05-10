package edu.uselessworms;

import edu.uselessworms.solvers.Solver;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("Welcome to Homerville Vehicle Routing");
        Solver problemSolver = new Solver(10, 6);
        problemSolver.addAndRunCycle("cycle1.txt");
        problemSolver.addAndRunCycle("cycle2.txt");
        problemSolver.addAndRunCycle("cycle3.txt");
        problemSolver.addAndRunCycle("cycle4.txt");
        problemSolver.addAndRunCycle("cycle5.txt");
        problemSolver.addAndRunCycle("cycle6.txt");
        problemSolver.addAndRunCycle("cycle7.txt");
        problemSolver.addAndRunCycle("cycle8.txt");
        problemSolver.addAndRunCycle("cycle9.txt");
        problemSolver.addAndRunCycle("cycle10.txt");

        problemSolver.addTruckWork(); // add cost for truck maitenence
        System.out.println("Total Price: " + Solver.moneyFormat.format(problemSolver.getTotalPrice()));
        problemSolver.end();




    }
}
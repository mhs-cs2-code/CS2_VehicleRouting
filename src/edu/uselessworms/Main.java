package edu.uselessworms;

import edu.uselessworms.solvers.Solver;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Welcome to Homerville Vehicle Routing");
        Solver problemSolver = new Solver();
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
        problemSolver.addAndRunCycle("cycle11.txt");
        System.out.println("Total Price: " + Solver.moneyFormat.format(problemSolver.getTotalPrice()));




    }
}
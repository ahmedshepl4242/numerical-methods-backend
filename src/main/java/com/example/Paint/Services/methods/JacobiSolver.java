package com.example.Paint.Services.methods;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class JacobiSolver {
    private double[][] systemMatrix;
    private double[] rhsVector;
    private double[] initialGuess;
    private double absRelativeError;
    private int maxIterations;
    private int precision;

    public JacobiSolver(double[][] systemMatrix, double[] rhsVector, double[] initialGuess, Double absRelativeError, Integer maxIterations, Integer precision) {
        this.systemMatrix = systemMatrix;
        this.rhsVector = rhsVector;
        this.initialGuess = initialGuess;
        this.absRelativeError = absRelativeError != null ? absRelativeError : 1e-10;
        this.maxIterations = maxIterations != null ? maxIterations : 100;
        this.precision = precision != null ? precision : 4;
    }

    public ArrayList<double[]> solve() {
        int numEquations = systemMatrix.length;
        int numVariables = systemMatrix[0].length;
        double[] x = Arrays.copyOf(initialGuess, initialGuess.length);
        ArrayList<double[]> xValues = new ArrayList<>();

        int iteration = 0;
        boolean converged = false;

        while (iteration < maxIterations && !converged) {
            double[] xPrev = Arrays.copyOf(x, x.length);
            double[] xNew = new double[numVariables];

            for (int i = 0; i < numEquations; i++) {
                double sigma = 0;
                for (int j = 0; j < numVariables; j++) {
                    if (j != i) {
                        sigma += systemMatrix[i][j] * xPrev[j];
                    }
                }
                xNew[i] = (rhsVector[i] - sigma) / systemMatrix[i][i];
            }

            double[] roundedX = sigFigs(xNew, precision);
            xValues.add(roundedX);

            double maxError = 0;
            for (int i = 0; i < numVariables; i++) {
                double error = Math.abs((xNew[i] - xPrev[i]) / xNew[i]);
                if (error > maxError) {
                    maxError = error;
                }
            }

            if (maxError < absRelativeError) {
                System.out.println("Jacobi method converged in " + (iteration + 1) + " iterations.");
                converged = true;
            }

            x = Arrays.copyOf(xNew, xNew.length);
            iteration++;
        }

        if (!converged) {
            System.out.println("Jacobi method did not converge within the specified iterations.");
        }

        return xValues;
    }

    private double[] sigFigs(double[] values, int n) {
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = sigFigs(values[i], n);
        }
        return result;
    }

    private double sigFigs(double x, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of significant figures must be positive.");
        }
        if (x == 0) {
            return 0.0;
        }

        double roundedX = Math.round(x * Math.pow(10, n - (int) Math.ceil(Math.log10(Math.abs(x))))) / Math.pow(10, n - (int) Math.ceil(Math.log10(Math.abs(x))));
        return roundedX;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of equations: ");
        int numEquations = scanner.nextInt();
        System.out.println("Enter the number of variables: ");
        int numVariables = scanner.nextInt();

        double[][] coefficients = new double[numEquations][numVariables];
        double[] constants = new double[numEquations];
        double[] initialGuess = new double[numVariables];

        System.out.println("Enter the coefficients:");
        for (int i = 0; i < numEquations; i++) {
            for (int j = 0; j < numVariables; j++) {
                System.out.println("Enter coefficient A[" + i + "][" + j + "]: ");
                coefficients[i][j] = scanner.nextDouble();
            }
        }

        System.out.println("Enter the constants:");
        for (int i = 0; i < numEquations; i++) {
            System.out.println("Enter constant b[" + i + "]: ");
            constants[i] = scanner.nextDouble();
        }

        System.out.println("Enter the initial guess:");
        for (int i = 0; i < numVariables; i++) {
            System.out.println("Enter initial guess x[" + i + "]: ");
            initialGuess[i] = scanner.nextDouble();
        }

        System.out.println("Enter the maximum number of iterations: ");
        int maxIterations = scanner.nextInt();
        System.out.println("Enter the number of significant figures: ");
        int sigFigures = scanner.nextInt();
        System.out.println("Enter the absolute relative error: ");
        double absRelativeError = scanner.nextDouble();

        JacobiSolver solver = new JacobiSolver(coefficients, constants, initialGuess, absRelativeError, maxIterations, sigFigures);
        ArrayList<double[]> xValues = solver.solve();

        for (int i = 0; i < xValues.size(); i++) {
            double[] roundedX = solver.sigFigs(xValues.get(i), sigFigures);
            System.out.println("Iteration " + (i + 1) + ": " + Arrays.toString(roundedX));
        }

        scanner.close();
    }
}



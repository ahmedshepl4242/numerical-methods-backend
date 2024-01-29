package com.example.Paint.Services.methods;

import java.util.Scanner;
import java.util.Arrays;

public class GaussSeidelSolver {

    private double[][] systemMatrix;
    private double[] rhsVector;
    private double[] initialGuess;
    private double absRelativeError;
    private int maxIterations;
    private int precision;

    public GaussSeidelSolver(double[][] systemMatrix, double[] rhsVector, double[] initialGuess, Double absRelativeError, Integer maxIterations, Integer precision) {
        this.systemMatrix = systemMatrix;
        this.rhsVector = rhsVector;
        this.initialGuess = initialGuess;
        this.absRelativeError = absRelativeError != null ? absRelativeError : 1e-10;
        this.maxIterations = maxIterations != null ? maxIterations : 100;
        this.precision = precision != null ? precision : 5;
    }

    public double[] solve() {
        int numEquations = systemMatrix.length;
        double[] x = Arrays.copyOf(initialGuess, initialGuess.length);
        double[] xPrev = new double[numEquations];
        boolean isDominant = isDiagonallyDominant(systemMatrix);

        if (isDominant) {
            System.out.println("The matrix is diagonally dominant. Gauss-Seidel method is guaranteed to converge.");
        } else {
            System.out.println("The matrix is not diagonally dominant. Gauss-Seidel method is not guaranteed to converge.");
        }

        int iteration = 0;
        while (iteration < maxIterations) {
            System.arraycopy(x, 0, xPrev, 0, numEquations);

            for (int i = 0; i < numEquations; i++) {
                double sigma = 0;
                for (int j = 0; j < numEquations; j++) {
                    if (j != i) {
                        sigma += systemMatrix[i][j] * x[j];
                    }
                }
                x[i] = (rhsVector[i] - sigma) / systemMatrix[i][i];
            }

            double[] roundedX = sigFigs(x, precision);
            System.out.println("Iteration " + (iteration + 1) + ": " + Arrays.toString(roundedX));

            double maxError = 0;
            for (int i = 0; i < numEquations; i++) {
                double error = Math.abs((x[i] - xPrev[i]) / x[i]);
                if (error > maxError) {
                    maxError = error;
                }
            }

            if (maxError < absRelativeError) {
                System.out.println("Gauss-Seidel method converged in " + (iteration + 1) + " iterations.");
                break;
            }

            iteration++;
        }

        if (iteration == maxIterations) {
            System.out.println("Gauss-Seidel method did not converge within the specified iterations.");
        }

        return x;
    }

    private boolean isDiagonallyDominant(double[][] coefficients) {
        int n = coefficients.length;
        boolean atLeastOneGreater = false;
        for (int i = 0; i < n; i++) {
            double diagonalValue = Math.abs(coefficients[i][i]);
            double rowSum = 0;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    rowSum += Math.abs(coefficients[i][j]);
                }
            }
            if (diagonalValue < rowSum) {
                return false;
            }
            if (diagonalValue > rowSum) {
                atLeastOneGreater = true;
            }
        }
        return atLeastOneGreater;
    }

    private double[] sigFigs(double[] x, int n) {
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            result[i] = sigFigs(x[i], n);
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
        double[][] coefficients = new double[numEquations][numEquations];
        double[] constants = new double[numEquations];
        double[] initialGuess = new double[numEquations];

        System.out.println("Enter the coefficients:");
        for (int i = 0; i < numEquations; i++) {
            for (int j = 0; j < numEquations; j++) {
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
        for (int i = 0; i < numEquations; i++) {
            System.out.println("Enter initial guess x[" + i + "]: ");
            initialGuess[i] = scanner.nextDouble();
        }

        System.out.println("Enter the maximum number of iterations: ");
        int maxIterations = scanner.nextInt();

        System.out.println("Enter the number of significant figures: ");
        int sigFigures = scanner.nextInt();

        System.out.println("Enter the absolute relative error: ");
        double absRelativeError = scanner.nextDouble();

        GaussSeidelSolver solver = new GaussSeidelSolver(coefficients, constants, initialGuess, absRelativeError, maxIterations, sigFigures);
        solver.solve();
    }
}



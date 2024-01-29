package com.example.Paint.Services.methods;

import java.util.ArrayList;
import java.util.List;

public class GuassJordan {
    private static final double EPSILON = 1e-8;
    private List<double[][]> listArray;
    private double[][] array;
    private boolean flag;
    public double[] res;
    private static final int PRECISION = 3;
    private double timeTaken;
    private double timeStart;
    private double timeEnd;
    private double[] mxEle;


    public GuassJordan() {
        this.flag = true;
        this.listArray = new ArrayList<>();
        this.res = new double[0];
        this.array = new double[0][0];
    }

    public void calculate(double[][] arr) {
        this.timeStart = System.currentTimeMillis();
        this.array = arr;
        this.mxEle = new double[this.array.length];
        this.res = new double[this.array.length];
        for (int indRow = 0; indRow < this.array.length; indRow++) {
            mxEle[indRow] = Math.abs(this.array[indRow][0]);
            for (int indCol = 2; indCol < this.array.length; indCol++) {
                if (Math.abs(this.array[indRow][indCol]) > mxEle[indRow]) {
                    mxEle[indRow] = Math.abs(this.array[indRow][indCol]);
                }
            }
        }
//        if (!this.check()) {
//            this.timeEnd = System.currentTimeMillis();
//            this.timeTaken = this.timeEnd - this.timeStart;
//            return;
//        }
        this.forwardElimination();
        System.out.println();
        if (!this.flag) {
            System.out.println("No unique solution (system is overdetermined)");
            return;
        }
        this.backElimination();
        int length = this.array.length;
        for (int i = 0; i < this.array.length; i++) {
            this.res[i] = this.formatFloat(this.array[i][length] / this.array[i][i]);
        }
        System.out.println(this.res);
        System.out.println();
        for (double[][] list : this.listArray) {
            for (double[] doubles : list) {
                for (int k = 0; k < doubles.length; k++) {
                    doubles[k] = this.formatFloat(doubles[k]);
                }
            }
        }
        this.timeEnd = System.currentTimeMillis();
        this.timeTaken = this.timeEnd - this.timeStart;
    }

    private void forwardElimination() {
        int len = this.array.length;
        len -= 1;
        for (int k = 0; k < this.array.length; k++) {
            this.flag = this.partialPivoting(k);
            if (!this.flag) {
                return;
            }
            for (int i = k + 1; i < this.array.length; i++) {
                double factor = this.array[i][k] / this.array[k][k];
                for (int j = k; j < this.array.length; j++) {
                    this.array[i][j] = this.array[i][j] - factor * this.array[k][j];
                }
                this.array[i][len + 1] = this.array[i][len + 1] - factor * this.array[k][len + 1];
                this.listArray.add(copyArray(this.array));
            }
        }
    }

    private void backElimination() {
        int len = this.array.length;
        len -= 1;
        for (int k = len; k >= 0; k--) {
            for (int i = k - 1; i >= 0; i--) {
                double factor = this.array[i][k] / this.array[k][k];
                for (int j = k - 1; j < this.array.length; j++) {
                    this.array[i][j] = this.array[i][j] - factor * this.array[k][j];
                }
                this.array[i][len + 1] = this.array[i][len + 1] - factor * this.array[k][len + 1];
                this.listArray.add(copyArray(this.array));
            }
        }
    }

    private boolean partialPivoting(int k) {
        int len = this.array.length - 1;
        int p = k;
        double mx = Math.abs(this.array[k][k] / this.mxEle[k]);
        for (int indRow = k + 1; indRow < this.array.length; indRow++) {
            double temp = Math.abs(this.array[indRow][k] / this.mxEle[indRow]);
            if (temp > mx) {
                mx = temp;
                p = indRow;
            }
        }
        if (p != k) {
            for (int indCol = k; indCol < this.array.length; indCol++) {
                double temp = this.array[p][indCol];
                this.array[p][indCol] = this.array[k][indCol];
                this.array[k][indCol] = temp;
            }
            double tm = this.array[k][len + 1];
            this.array[k][len + 1] = this.array[p][len + 1];
            this.array[p][len + 1] = tm;
            tm = this.mxEle[p];
            this.mxEle[p] = this.mxEle[k];
            this.mxEle[k] = tm;
        }
        if (Math.abs(this.array[p][p]) <= EPSILON) {
            this.flag = false;
            return false;
        } else {
            return true;
        }
    }

    private double formatFloat(double number) {
        return Double.parseDouble(String.format("%." + PRECISION + "g", number));
    }
//
//    private boolean check() {
//        double[][] arrCofficient = new double[this.array.length][this.array[0].length - 1];
//        for (int i = 0; i < this.array.length; i++) {
//            System.arraycopy(this.array[i], 0, arrCofficient[i], 0, this.array[i].length - 1);
//        }
//
//        int rank1 = new Array2DRowRealMatrix(this.array).getRank();
//        System.out.println("rank of augmented matrix is: " + rank1);
//
//        int rank2 = new Array2DRowRealMatrix(arrCofficient).getRank();
//        System.out.println("rank of array except last column is: " + rank2);
//        System.out.println();
//        System.out.println();
//        if (rank1 == rank2) {
//            if (rank1 < this.array.length) {
//                System.out.println("Infinite solutions  (system is consistent)");
//                double[][] b = new double[this.array.length][1];
//                for (int i = 0; i < this.array.length; i++) {
//                    b[i][0] = this.array[i][this.array[i].length - 1];
//                }
//                RealMatrix coefficients = new Array2DRowRealMatrix(arrCofficient, false);
//                RealMatrix constants = new Array2DRowRealMatrix(b, false);
//                DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
//                RealMatrix solution = solver.solve(constants);
//                this.res = new double[this.array.length];
//                for (int i = 0; i < this.array.length; i++) {
//                    this.res[i] = solution.getEntry(i, 0);
//                }
//                for (double i : this.res) {
//                    System.out.println(i);
//                }
//                return false;
//            } else {
//                System.out.println("unique solution (system is determined)");
//                return true;
//            }
//        } else {
//            System.out.println("No solution (system is inconsistent)");
//            return false;
//        }
//    }

    private double[][] copyArray(double[][] array) {
        double[][] copy = new double[array.length][];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i].clone();
        }
        return copy;
    }
}



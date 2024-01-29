package com.example.Paint.Services.methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GaussElimination {
    private static final double EPSILON = 1e-8;
    private boolean flag;
    private List<double[][]> listArray;
    private double timeTaken;
    private double timeStart;
    private double timeEnd;
    private String status;

    public double res[];

    public GaussElimination() {
        this.flag = true;
        this.listArray = new ArrayList<>();
        this.timeTaken = 0;
        this.timeStart = 0;
        this.timeEnd = 0;
        this.status = "";
    }

    public void calculate(double[][] arr) {
        this.timeStart = System.currentTimeMillis();
        double[][] array = arr;
        double[] mxEle = new double[array.length];

        for (int indRow = 0; indRow < array.length; indRow++) {
            mxEle[indRow] = Math.abs(array[indRow][0]);
            for (int indCol = 2; indCol < array[indRow].length; indCol++) {
                if (Math.abs(array[indRow][indCol]) > mxEle[indRow]) {
                    mxEle[indRow] = Math.abs(array[indRow][indCol]);
                }
            }
        }

//        if (!check(array)) {
//            this.timeEnd = System.currentTimeMillis();
//            this.timeTaken = this.timeEnd - this.timeStart;
//            return;
//        }

        eleminate(array, mxEle);
        substitute(array);

        System.out.println("Results:");
        for (int i = 0; i < this.res.length; i++) {
            this.res[i] = formatFloat(this.res[i]);
        }
        System.out.println(Arrays.toString(this.res));
        System.out.println();

//        for (int i = 0; i < this.listArray.size(); i++) {
//            for (int j = 0; j < this.listArray.get(i).length; j++) {
//                for (int k = 0; k < this.listArray.get(i)[j].; k++) {
//                    this.listArray.get(i)[j][k] = formatFloat(this.listArray.get(i)[j][k]);
//                }
//            }
//        }

        this.timeEnd = System.currentTimeMillis();
        this.timeTaken = this.timeEnd - this.timeStart;
    }

    private void eleminate(double[][] array, double[] mxEle) {
        int len = array.length;
        len -= 1;

        for (int k = 0; k < array.length; k++) {
            this.flag = partialPivoting(array, mxEle, k);
            if (!this.flag) {
                return;
            }

            for (int i = k + 1; i < array.length; i++) {
                double factor = array[i][k] / array[k][k];
                for (int j = k; j < array.length; j++) {
                    array[i][j] = array[i][j] - factor * array[k][j];
                }
                array[i][len + 1] = array[i][len + 1] - factor * array[k][len + 1];
                this.listArray.add(copyArray(array));
            }
        }
    }

    private void substitute(double[][] array) {
        int len = array.length;
        this.res = new double[len];
        len -= 1;
        this.res[len] = array[len][len + 1] / array[len][len];

        for (int indRow = array.length - 2; indRow >= 0; indRow--) {
            double sum = 0;
            for (int indCol = indRow + 1; indCol < array.length; indCol++) {
                sum = sum + array[indRow][indCol] * this.res[indCol];
            }

            if (array[indRow][indRow] == 0) {
                this.res[indRow] = 0;
            } else {
                this.res[indRow] = (array[indRow][len + 1] - sum) / array[indRow][indRow];
            }
        }
    }

    private boolean partialPivoting(double[][] array, double[] mxEle, int k) {
        int len = array.length - 1;
        int p = k;
        double mx = Math.abs(array[k][k] / mxEle[k]);

        for (int indRow = k + 1; indRow < array.length; indRow++) {
            double temp = Math.abs(array[indRow][k] / mxEle[indRow]);
            if (temp > mx) {
                mx = temp;
                p = indRow;
            }
        }

        if (p != k) {
            for (int indCol = k; indCol < array.length; indCol++) {
                double temp = array[p][indCol];
                array[p][indCol] = array[k][indCol];
                array[k][indCol] = temp;
            }

            double tm = array[k][len + 1];
            array[k][len + 1] = array[p][len + 1];
            array[p][len + 1] = tm;

            tm = mxEle[p];
            mxEle[p] = mxEle[k];
            mxEle[k] = tm;
        }

        if (Math.abs(array[p][p]) <= EPSILON) {
            this.flag = false;
            return false;
        } else {
            return true;
        }
    }

    private double[][] copyArray(double[][] array) {
        double[][] copy = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return copy;
    }

    private double formatFloat(double number) {
        double floatNumber = number;
        return Double.parseDouble(String.format("%.3g", floatNumber));
    }

//    private boolean check(double[][] array) {
//        double[][] arrCofficient = Arrays.copyOf(array, array.length);
//        for (int i = 0; i < array.length; i++) {
//            arrCofficient[i] = Arrays.copyOf(array[i], array[i].length - 1);
//        }
//
//        int rank1 = np.linalg.matrix_rank(array);
//        System.out.println("rank of augmented matrix is: " + rank1);
//
//        int rank2 = np.linalg.matrix_rank(arrCofficient);
//        System.out.println("rank of array except last column is: " + rank2);
//        System.out.println();
//        System.out.println();
//
//        if (rank1 == rank2) {
//            if (rank1 < array.length) {
//                this.status = "Infinite solutions and one of the solutions is (system is consistent)";
//                double[][] b = new double[array.length][1];
//                for (int i = 0; i < array.length; i++) {
//                    b[i][0] = array[i][array[i].length - 1];
//                }
//                double[][] x = lstsq(arrCofficient, b);
//
//                this.res = new double[array.length];
//                for (int i = 0; i < array.length; i++) {
//                    this.res[i] = x[0][i][0];
//                }
//                for (int i = 0; i < array.length; i++) {
//                    this.res[i] = formatFloat(this.res[i]);
//                }
//                for (double i : this.res) {
//                    System.out.println(i);
//                }
//                return false;
//            } else {
//                this.status = "unique solution (system is determined)";
//                return true;
//            }
//        } else {
//            this.status = "No solution (system is inconsistent)";
//            return false;
//        }
//    }


}



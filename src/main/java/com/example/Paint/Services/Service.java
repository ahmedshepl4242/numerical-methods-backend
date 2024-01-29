package com.example.Paint.Services;

import com.example.Paint.Services.methods.GaussElimination;
import com.example.Paint.Services.methods.GuassJordan;

@org.springframework.stereotype.Service

public class Service {

    private String type = "guassJordan";

    GaussElimination GE = new GaussElimination();
    GuassJordan GJ = new GuassJordan();

//    public Double[] ChooseMethod(Double[][] arr) {

//        if (type.equalsIgnoreCase("guassElimination")) GE.calculate(arr);
//        if (type.equalsIgnoreCase("guassJordan")) {
//            GJ.calculate(arr);
////
////            System.out.println((GJ.res.toString()));
//            return GJ.res;
//        }
//        return null;
//    }



}

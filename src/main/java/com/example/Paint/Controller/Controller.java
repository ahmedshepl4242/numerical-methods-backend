package com.example.Paint.Controller;

import com.example.Paint.Services.Service;
import com.example.Paint.Services.methods.GaussElimination;
import com.example.Paint.Services.methods.GuassJordan;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class Controller {
    Service service = new Service();


//    List<String>  getAction() {
//        return service.getData();
//    }


    @PostMapping("/cal")
    double[] SolveByGuassJordan(@RequestBody Double[][] arr) {

        double[][] array = new double[arr.length][arr[0].length];


        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                array[i][j] = arr[i][j];
            }
        }
        GuassJordan g = new GuassJordan();
        g.calculate(array);
        for (int i = 0; i < g.res.length; i++) {
            System.out.println(g.res[i] + " ");
        }
        return g.res;
    }

    double[] solveByGaussElimination(@RequestBody Double[][] arr) {

        double[][] array = new double[arr.length][arr[0].length];


        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                array[i][j] = arr[i][j];
            }
        }
        GaussElimination g = new GaussElimination();
        g.calculate(array);
        for (int i = 0; i < g.res.length; i++) {
            System.out.println(g.res[i] + " ");
        }
        return g.res;
    }
}

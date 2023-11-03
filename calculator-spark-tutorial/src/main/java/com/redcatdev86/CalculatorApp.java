package com.redcatdev86;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;

public class CalculatorApp {

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);
        SparkConf calculatorSparkConf = new SparkConf().setAppName("CalculatorSpark").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(calculatorSparkConf);
        JavaRDD<Double> calcRdd = jsc.parallelize(randomList());
        JavaRDD<Integer> intMapRdd = calcRdd.map(value -> value.intValue());
        int result = intMapRdd.reduce((val1,val2) -> val1 + val2);
        System.out.println("Result value: " + Math.sqrt(result));
        //Calculate num of element in a rdd
        JavaRDD<Long> numOfElementRDD = calcRdd.map(value -> 1L);
        Long numOfElement = numOfElementRDD.reduce((val1,val2) -> val1 + val2);
        System.out.println("Number of element in rdd: " + numOfElement);
        jsc.close();
    }

    public static List<Double> randomList(){
        List<Double> elements = new ArrayList<>();
        for (int i = 0; i<10000;i++){
            elements.add(2d);
        }
        return elements;
    }
}

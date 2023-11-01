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
        JavaRDD<Integer> calcRdd = jsc.parallelize(randomList());
        Integer result = calcRdd.reduce((v1,v2)-> v1+v2);
        System.out.println("Result value: " + result);
    }

    public static List<Integer> randomList(){
        List<Integer> elements = new ArrayList<>();
        for (int i = 0; i<10000;i++){
            elements.add(2);
        }
        return elements;
    }
}

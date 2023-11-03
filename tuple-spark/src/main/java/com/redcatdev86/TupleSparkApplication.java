package com.redcatdev86;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TupleSparkApplication
{
    public static void main( String[] args ) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);
        List<Double> elements = randomList();
        SparkConf sparkConf = new SparkConf().setAppName("TupleSparkApp").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        JavaRDD<Double> originalInt = jsc.parallelize(elements);
        JavaRDD<Tuple2<Double, Double>> tupleRDD = originalInt.map(val -> new Tuple2<>(val, Math.sqrt(val)));
        tupleRDD.collect().forEach(val -> System.out.println(val));
        jsc.close();
    }

    public static List<Double> randomList(){
        List<Double> list = new ArrayList<>();
        for (int i = 0; i<10000;i++){
            list.add(Math.random() * ((10 - 0) + 0));
        }
        return list;
    }
}

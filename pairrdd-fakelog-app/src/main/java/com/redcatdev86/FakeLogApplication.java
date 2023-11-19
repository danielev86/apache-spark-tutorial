package com.redcatdev86;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.redcatdev86.utility.LogUtility.generateFakeLog;
import static com.redcatdev86.utility.LogUtility.splitLog;
import static org.apache.log4j.Level.WARN;

/**
 * Hello world!
 *
 */
public class FakeLogApplication {

    public static void main(String[] args) throws ParseException {
        List<String> logs = generateFakeLog();
        Map<String,Long> mapLogs = calculateMapLogInstance(logs);

        Logger.getLogger("org.apache").setLevel(WARN);

        SparkConf sparkConf = new SparkConf().setAppName("fakeLogSpark").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);


        JavaPairRDD<String,Long> logReduceRDD = sc.parallelize(logs).mapToPair(rawValue ->{
            String[] logData = splitLog(rawValue);
            return new Tuple2<>(logData[0], 1L);
        }).reduceByKey((val1, val2) -> val1 + val2);

        logReduceRDD.foreach(item -> {
            String logLevel = item._1;
            Long numEleNoSpark = mapLogs.get(logLevel);
            System.out.println("Level log: " + logLevel + " Spark Ele Size: " + item._2 + " NO Spark Ele Size: " + numEleNoSpark);
        });

        System.out.println("Print ordered data");

        logReduceRDD.sortByKey(true).collect().forEach(item -> {
            String logLevel = item._1;
            Long numEleNoSpark = mapLogs.get(logLevel);
            System.out.println("Level log: " + logLevel + " Spark Ele Size: " + item._2 + " NO Spark Ele Size: " + numEleNoSpark);
        });

        sc.close();
    }

    private static Map<String, Long> calculateMapLogInstance(List<String> logs){
        Map<String, Long> mapData = new HashMap<>();
        for (String log : logs){
            String[] logArray = splitLog(log);
            String level = logArray[0];
            long data = mapData.get(level) != null ? mapData.get(level) + 1 : 1L;
            mapData.put(level, data);
        }
        return mapData;
    }
}

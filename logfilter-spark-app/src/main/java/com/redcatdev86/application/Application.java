package com.redcatdev86.application;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class Application {

    public static final List<String> HTTP_CODES = Arrays.asList("GET", "POST", "PUT", "HEAD");

    public static final String RESOURCE_PATH = "src/main/resources";

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);
        if (System.getProperty("os.name").contains("Windows")){
            String hadoopPath = System.getProperty("user.dir") + "/" + RESOURCE_PATH + "/hadoop";
            System.setProperty("hadoop.home.dir", hadoopPath);
        }
        SparkConf sparkConf = new SparkConf().setAppName("logfilter").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        JavaRDD<String> lines = jsc.textFile(RESOURCE_PATH + "/log/access.log");
        lines
                .mapToPair(line -> {
                    String[] ipAddress = line.split("-");
                    return new Tuple2<>(ipAddress[0].trim(), 1L);
                })
                .reduceByKey((val1, val2) -> val1+val2)
                .sortByKey()
                .collect()
                .forEach(line -> System.out.println(line._1 + " - " + line._2));

        lines
                .flatMap(line -> Arrays.asList(line.split("\"")).iterator())
                .filter(line -> line.contains("GET") || line.contains("POST"))
                .mapToPair(line -> new Tuple2<>(line.split(" ")[0], 1L))
                .reduceByKey((val1, val2) -> val1 + val2)
                .collect()
                .forEach(line -> System.out.println(line._1 + " - " + line._2));

        jsc.close();
    }

}

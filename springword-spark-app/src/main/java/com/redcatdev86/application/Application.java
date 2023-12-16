package com.redcatdev86.application;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {

    private static final String RESOURCE_PATH = "src/main/resources/";

    public static void main(String[] args) {
        List<String> borrowingList = getBorrowsList();

        if (System.getProperty("os.name").contains("Windows")){
            String hadoopPath = System.getProperty("user.dir") + "/" + RESOURCE_PATH + "winutils-extra/hadoop";
            System.setProperty("hadoop.home.dir", hadoopPath);
        }

        Logger.getLogger("org.apache").setLevel(Level.WARN);
        SparkConf sparkConf = new SparkConf().setAppName("springReader").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        JavaRDD<String> lines = jsc.textFile(RESOURCE_PATH + "/data/input-spring.txt");
        JavaRDD<String> filteredLines =
                lines
                .map(line -> line.replaceAll("[^a-aA-Z\\s]", "").toLowerCase())
                .filter(line -> isBlankString(line))
                .flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                .filter(line -> !(borrowingList.contains(line)))
                .filter(line -> isBlankString(line));
        JavaPairRDD<String, Long> pairWord = filteredLines
                .mapToPair(line -> new Tuple2<>(line, 1L))
                .reduceByKey((val1, val2) -> val1+val2);

        JavaPairRDD<Long,String> orderedPairWord = pairWord
                .mapToPair(val -> new Tuple2<>(val._2, val._1))
                .sortByKey(false);

        orderedPairWord
                .take(10)
                .forEach(data -> System.out.println(data._2 + "(" + data._1 + ")"));
        jsc.close();
    }

    public static List<String> getBorrowsList(){
        List<String> words = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Application.class.getResourceAsStream("/data/boringwords.txt")));
        reader.lines().forEach(value -> words.add(value));
        return words;
    }

    public static boolean isBlankString(String rawValue){
        return rawValue.trim().length() > 0;
    }
}

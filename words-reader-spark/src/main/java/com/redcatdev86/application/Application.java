package com.redcatdev86.application;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class Application {

    private static final String RESOURCE_PATH = "src/main/resources/";

    public static void main(String[] args) {

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        if (System.getProperty("os.name").contains("Windows")){
            String hadoopPath = System.getProperty("user.dir") + "/" + RESOURCE_PATH + "winutils-extra/hadoop/bin";
            System.setProperty("hadoop.home.dir", hadoopPath);
        }

        SparkConf sparkConf = new SparkConf().setAppName("wordReader").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        JavaRDD<String> originalTxt = jsc.textFile(RESOURCE_PATH+ "input/words.txt");
        JavaRDD<String> words = originalTxt.flatMap(rawLine ->  splitData(rawLine).iterator());
        words.foreach(word -> System.out.println(word));

        long element = words.map(data -> 1L).reduce((val1,val2) -> val1 + val2);
        System.out.println("Num of words: " + element);

        jsc.close();

    }

    private static List<String> splitData(String data){
        String rawLine = data.replaceAll("," , "")
                .replaceAll(".", "")
                .replaceAll(";", "")
                .replaceAll(";", "");
        return Arrays.asList(rawLine.split(" "));
    }

}

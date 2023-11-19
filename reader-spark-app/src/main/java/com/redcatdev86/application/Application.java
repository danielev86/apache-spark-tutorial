package com.redcatdev86.application;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import static java.util.Arrays.asList;

public class Application {

    public static void main(String[] args) {

        //System.setProperty("hadoop.home.dir", "full path to the folder with winutils");
        if (System.getProperty("os.name").contains("Windows")){
            String hadoopPath = System.getProperty("user.dir") + "src/main/resources/winutils/hadoop/bin";
            System.out.println("Setting temp hadoop home dir " + hadoopPath);
            System.setProperty("hadoop.home.dir", hadoopPath);
        }

        Logger.getLogger("org.apache").setLevel(Level.WARN);
        SparkConf sparkConf = new SparkConf().setAppName("readerTxtFile").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        JavaRDD<String> fileTxt = jsc.textFile("src/main/resources/subtitles/input.txt");
        JavaRDD<String> words =fileTxt.flatMap(rawData -> asList(rawData.split(" ")).iterator());

        System.out.println("Print all words");
        words.foreach(data -> System.out.println(data));

        long numOfLine = words.map(word -> 1L).reduce((val1, val2) -> val1 + val2);
        System.out.println("Calculate num of words: " + numOfLine);

        jsc.close();
    }

}

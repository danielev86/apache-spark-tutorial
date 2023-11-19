package com.redcated86.application;

import com.redcated86.application.bean.Country;
import com.redcated86.application.utility.CountryUtility;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class Application {

    private static final List<String> COUNTRY_CODES = Arrays.asList("ITA", "ESP");

    public static void main(String[] args) {
        List<Country> countries = CountryUtility.generateCountries();
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkConf sparkConf = new SparkConf().setAppName("filterApp").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        jsc.parallelize(countries)
                .filter(country -> COUNTRY_CODES.contains(country.getCountryCode()))
                .mapToPair(country -> new Tuple2<>(country.getCountryName(), 1L))
                .reduceByKey((val1, val2) -> val1 + val2)
                .sortByKey(true)
                .collect()
                .forEach(tuple -> System.out.println(tuple._1 + " num of instance: " + tuple._2));

        jsc.close();
    }

}

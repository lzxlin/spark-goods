package com.twq;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.File;
import java.util.Arrays;

/**
 * Created by tangweiqun on 2017/9/15.
 */
public class WordCountJava8 {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("WordCountJava8");

        String defaultPath = "src/main/resources";
        String dataPath = conf.get("spark.wordcount.dataPath", defaultPath);

        boolean isLocal = false;
        if (dataPath.equals(defaultPath)) {
            conf.setMaster("local");
            isLocal = true;
        }

        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> inputRDD = sc.textFile(dataPath + "/test.txt");

        JavaRDD<String> wordsRDD = inputRDD.flatMap(input -> Arrays.asList(input.split(" ")).iterator());

        JavaPairRDD<String, Integer> keyValueWordsRDD
                = wordsRDD.mapToPair(word -> new Tuple2<String, Integer>(word, 1));

        JavaPairRDD<String, Integer> wordCountRDD = keyValueWordsRDD.reduceByKey((a, b) -> a + b);

        if (isLocal) {
            File outputFile = new File(dataPath + "/wordcount");
            if (outputFile.exists()) {
                File[] files = outputFile.listFiles();
                for(File file: files) {
                    file.delete();
                }
                outputFile.delete();
            }
        }

        wordCountRDD.saveAsTextFile(dataPath + "/wordcount");

        System.out.println(wordCountRDD.collect());
    }
}

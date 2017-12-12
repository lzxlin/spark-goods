package com.twq;

import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by tangweiqun on 2017/9/15.
 */
public class WordCountJava7 {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("WordCountJava7");
        String defaultPath = "src/main/resources";
        String dataPath = conf.get("spark.wordcount.dataPath", defaultPath);

        boolean isLocal = false;
        if (dataPath.equals(defaultPath)) {
            conf.setMaster("local");
            isLocal = true;
        }

        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> inputRDD = sc.textFile(dataPath + "/test.txt");

        JavaRDD<String> wordsRDD = inputRDD.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(" ")).iterator();
            }
        });

        JavaPairRDD<String, Integer> keyValueWordsRDD
                = wordsRDD.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<String, Integer>(s, 1);
            }
        });

        JavaPairRDD<String, Integer> wordCountRDD =
                keyValueWordsRDD.reduceByKey(new HashPartitioner(2),
                        new Function2<Integer, Integer, Integer>() {
                            @Override
                            public Integer call(Integer a, Integer b) throws Exception {
                                return a + b;
                            }
                        });

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

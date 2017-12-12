package com.twq

import java.io.File

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by tangweiqun on 2017/9/10.
  */
object WordCountScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WordCountScala")
    val defaultPath = "src/main/resources"
    val dataPath = conf.get("spark.wordcount.dataPath", defaultPath)

    var isLocal = false
    if (dataPath == defaultPath) {
      conf.setMaster("local")
      isLocal = true
    }

    val sc = new SparkContext(conf)

    val sourceDataRDD = sc.textFile(dataPath + "/test.txt")

    val wordsRDD = sourceDataRDD.flatMap(line => line.split(" "))

    val keyValueWordsRDD = wordsRDD.map(word => (word, 1))

    val wordCountRDD = keyValueWordsRDD.reduceByKey((x, y) => x + y)

    if (isLocal) {
      val outputFile = new File(dataPath + "/wordcount")
      if (outputFile.exists()) {
        val listFile = outputFile.listFiles()
        listFile.foreach(_.delete())
        outputFile.delete()
      }
    }

    wordCountRDD.saveAsTextFile(dataPath + "/wordcount")

    wordCountRDD.collect().foreach(println)

  }
}

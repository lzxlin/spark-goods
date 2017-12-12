package com.twq

import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext}

object productDataToFpg {

  def productData: (SparkContext,RDD[String]) = {
    val driver="com.mysql.jdbc.Driver"
    //url指向要访问的数据库名spark
    val url="jdbc:mysql://127.0.0.1:3306/spark"
    val user="root"
    val password="shenmiaomi"
    val table="(select userID,itemID from rawdata where buy_num >0 order by userID) as fpgData"

    val sc=new SparkContext(new SparkConf().setAppName("productDataToFpg").setMaster("local[4]"))
    val sqlContext=new SQLContext(sc)
    val jdbc=sqlContext.read.format("jdbc").options(Map(
      "url" -> url,
      "driver" -> driver,
      "dbtable" -> table,
      "user" -> user,
      "password" -> password
    )).load()
    val sqlRDD=jdbc.rdd.map{
      case Row(userID:Int,itemID:Int) =>(userID.toString,itemID.toString)
    }
    val combine=sqlRDD.reduceByKey((x,y) => x+" "+y).map(f => f._2)
    return (sc,combine)
  }
}

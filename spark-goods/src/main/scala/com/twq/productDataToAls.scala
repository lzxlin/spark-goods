package com.twq

import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.{SparkConf, SparkContext, sql}
import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import org.apache.spark.rdd._

object productDataToAls {

  //从rawData获取user,item,score，在经过combineByKey处理成RDD[Rating]
  def productData: (SparkContext,RDD[Rating]) = {
    val driver="com.mysql.jdbc.Driver"
    //url指向要访问的数据库名spark
    val url="jdbc:mysql://127.0.0.1:3306/spark"
    val user="root"
    val password="shenmiaomi"
    val table="(select userID,itemID,scores from rawData) as alsData"

    val sc=new SparkContext(new SparkConf().setAppName("testMysql").setMaster("local[4]"))
    val sqlContext=new SQLContext(sc)
    val jdbc=sqlContext.read.format("jdbc").options(Map(
      "url" -> url,
      "driver" -> driver,
      "dbtable" -> table,
      "user" -> user,
      "password" -> password
    )).load()
    type MVType=(Int,Double)
    //将dataframe转成rdd，在合并相同user,item的数据，使用combinebykey求平均值
    val sqlRDD=jdbc.rdd.map{
      case Row(user:Int,item:Int,score:Double) =>
        ((user,item),score)
    }.combineByKey(
      mean => (1,mean),
      (c1:MVType,newMean) => (c1._1+1,c1._2+newMean),
      (c1:MVType,c2:MVType) => (c1._1+c2._1,c1._2+c2._2)
    ).map{
      case ((user,item),(num,mean)) => ((user,item),mean/num)
    }.map{
      case ((user,item),mean) => Rating(user,item,mean)
    }
    return (sc,sqlRDD)
  }
}

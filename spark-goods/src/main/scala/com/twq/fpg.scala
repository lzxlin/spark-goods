package com.twq

import java.sql.DriverManager
import java.sql.Connection

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.fpm.{FPGrowth, FPGrowthModel}
import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.{DateTime, Duration}

object fpg {

  def main(args: Array[String]): Unit = {
    SetLogger
    /*val conf=new SparkConf().setAppName("fpg").setMaster("local[2]")
    val sc=new SparkContext(conf)
    //读取样本数据
    val path="testData/fpg.txt"
    val data=sc.textFile(path)*/
    println("从数据库中获取数据并进行处理......")
    val startTime1 = new DateTime()
    val (sc,data)=productDataToFpg.productData
    val endTime1 = new DateTime()
    val duration1 = new Duration(startTime1, endTime1)
    println("数据处理完毕......处理时间为："+duration1.getMillis()+" ms")
    val examples=data.map(_.split(" ").distinct).cache()
    //data.foreach(println)
    //建立模型
    val minSupport=0.2
    val minConfidence=0.8
    val numPartition=2
    println("开始训练......")
    val startTime = new DateTime()
    val model=new FPGrowth().setMinSupport(minSupport).
      setNumPartitions(numPartition).
      run(examples)
    val endTime = new DateTime()
    val duration = new Duration(startTime, endTime)
    println("训练完成，训练时间为："+duration.getMillis()+" ms")
    //打印结果
    println(s"频繁项数目:${model.freqItemsets.count()}")
    /*model.freqItemsets.collect().foreach({
      itemset =>
        println(itemset.items.mkString("[",",","]")+", "+itemset.freq)
    })*/
    //连接数据库
    var conn = ConnectionPool.getConnection()
    try {
      //清空表
      val pstd=conn.prepareStatement("truncate table fpg")
      pstd.executeUpdate()

      val sql="insert into fpg(antecedent,consequent,confidence) values(?,?,?)"
      val pst=conn.prepareStatement(sql)
      //通过置信度帅选出推荐规则
      model.generateAssociationRules(minConfidence).collect().foreach(rule =>{
        pst.setString(1,rule.antecedent.mkString(" "))
        pst.setString(2,rule.consequent.mkString(" "))
        pst.setDouble(3,rule.confidence)
        pst.executeUpdate()
        //println(rule.antecedent.mkString(" ")+"-->"+rule.consequent.mkString(" ")+"-->"+rule.confidence)
      })
      pst.close()
      //查看规则生成数量
      println("规则数目："+model.generateAssociationRules(minConfidence).collect().length)
    } catch {
      case e => e.printStackTrace
      //case _: Throwable => println("ERROR")
    }
    conn.close()
  }

  def SetLogger = {
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("com").setLevel(Level.OFF)
    System.setProperty("spark.ui.showConsoleProgress", "false")
    Logger.getRootLogger().setLevel(Level.OFF)
  }
}

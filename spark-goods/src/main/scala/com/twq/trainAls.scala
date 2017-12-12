package com.twq

import com.twq.testMLlib.saveModelToSql
import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd._
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.joda.time.{DateTime, Duration}

import scala.io.StdIn


object trainAls {

  def main(args: Array[String]): Unit = {
    SetLogger
    println("==================数据准备准备阶段==================")
    val ratings=PrepareData()
    println("==================训练阶段==========================")
    print("开始使用 " + ratings.count() + "条评比数据进行训练模型... ")
    val startTime = new DateTime()
    val par=getBestModel.bestModel().split(" ")
    //println(par(0)+" "+par(1)+" "+par(2))
    val model = ALS.train(ratings,par(0).toInt,par(1).toInt,par(2).toDouble)
    val endTime = new DateTime()
    val duration = new Duration(startTime, endTime)
    println("训练完成!"+ "训练需要时间" + duration.getMillis + "毫秒")
    println("==========保存阶段===============")
    val startTime1 = new DateTime()
    saveModelToSql(model)
    val endTime1 = new DateTime()
    val duration1 = new Duration(startTime1, endTime1)
    println("保存完成!"+ "保存需要时间" + duration1.getMillis + "毫秒")
    //println("==========推荐阶段===============")
    //recommend(model)
    //println("完成")
  }

  def saveModelToSql(model:MatrixFactorizationModel)={
    val userFeatures=model.userFeatures
    val itemFeatures=model.productFeatures
    val users=userFeatures.map(r => r._1).collect()
    val items=itemFeatures.map(r => r._1).collect()
    val delUsertb="truncate table userRecommend"
    val delItemtb="truncate table itemRecommend"
    val userSql="insert into userRecommend values(?,?,?)"
    val itemSql="insert into itemRecommend values(?,?,?)"
    val num=3 //保存条数
    val conn=null
    try{
      val conn=ConnectionPool.getConnection()
      //清空表
      val pst1=conn.prepareStatement(delUsertb)
      pst1.executeUpdate()
      //println("清空user成功")
      val pst2=conn.prepareStatement(delItemtb)
      pst2.executeUpdate()
      pst1.close();pst2.close()
      //println("清空item成功")
      val pstu = conn.prepareStatement(userSql)
      val psti=conn.prepareStatement(itemSql)
      users.foreach{user =>{
        val arrays=model.recommendProducts(user,num)
        //println("用户："+user)
        arrays.foreach{array =>{
          pstu.setInt(1,user)
          pstu.setInt(2,array.product)
          pstu.setDouble(3,array.rating)
          pstu.executeUpdate()
          //println("添加成功")
        }}
      }}
      items.foreach(item =>{
        val arrays=model.recommendUsers(item,num)
        //println("用户："+item)
        arrays.foreach{array =>{
          psti.setInt(1,item)
          psti.setInt(2,array.user)
          psti.setDouble(3,array.rating)
          psti.executeUpdate()
          //println("添加成功")
        }}
      })
      psti.close()
      pstu.close()
    }finally {
      ConnectionPool.returnConnection(conn)
    }
  }

  def recommend(model: MatrixFactorizationModel)={
    var choose=""
    while(choose!="3"){
      print("请选择要推荐类型  1.针对用户推荐商品 2.针对商品推荐感兴趣的用户 3.离开?")
      choose=StdIn.readLine()
      if (choose == "1") { //如果输入1.针对用户推荐电影
        print("请输入用户id?")
        val inputUserID = StdIn.readLine() //读取用户ID
        RecommendItems(model, inputUserID.toInt) //针对此用户推荐电影
      } else if (choose == "2") { //如果输入2.针对电影推荐感兴趣的用户
        print("请输入商品的 id?")
        val inputMovieID =StdIn.readLine()//读取MovieID
        RecommendUsers(model,inputMovieID.toInt) //针对此电影推荐用户
      }
    }
  }

  def SetLogger={
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("com").setLevel(Level.OFF)
    System.setProperty("spark.ui.showConsoleProgress","false")
    Logger.getRootLogger.setLevel(Level.OFF)
  }

  def PrepareData():(RDD[Rating])={
    println("从数据库取出数据并进行处理......")
    //获取数据
    val (sc,ratingsRDD)=productDataToAls.productData
    println("处理完毕......共计：" + ratingsRDD.count().toString()+ "条")
    //----------------------3.显示数据记录数-------------
    val numRatings=ratingsRDD.count()
    val numUsers = ratingsRDD.map(_.user).distinct().count()
    val numItems = ratingsRDD.map(_.product).distinct().count()
    println("共计：ratings: " + numRatings + " User " + numUsers + " item " + numItems)
    //sc.stop()
    return ratingsRDD
  }
  def RecommendItems(model: MatrixFactorizationModel, inputUserID: Int) = {
    val RecommendMovie = model.recommendProducts(inputUserID, 10)
    var i = 1
    println("针对用户id" + inputUserID + "推荐下列商品:")
    RecommendMovie.foreach { r =>
      println(i.toString() + "."  + r.product+"  评分:" + r.rating.toString())
      i += 1
    }
  }

  def RecommendUsers(model: MatrixFactorizationModel, inputMovieID: Int) = {
    val RecommendUser = model.recommendUsers(inputMovieID, 10)
    var i = 1
    println("针对商品 id" + "推荐下列用户id:")
    RecommendUser.foreach { r =>
      println(i.toString + "用户id:" + r.user + "   评分:" + r.rating)
      i = i + 1
    }
  }
}

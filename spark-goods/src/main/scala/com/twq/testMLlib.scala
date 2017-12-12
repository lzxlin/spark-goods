package com.twq

import java.sql.PreparedStatement

import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd._
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.streaming.Seconds
import org.joda.time.{DateTime, Duration}

import scala.io.StdIn


object testMLlib {

  def main(args: Array[String]): Unit = {
    SetLogger
    println("==================数据准备准备阶段==================")
    val (ratings,movieTitle)=PrepareData()
    println("==========训练阶段===============")
    print("开始使用 " + ratings.count() + "条评比数据进行训练模型... ")
    val model = ALS.train(ratings, 5, 20, 0.1)
    println("训练完成!")
    val startTime = new DateTime()
    saveModelToSql(model)
    val endTime = new DateTime()
    val duration = new Duration(startTime, endTime)
    println("保存完成!"+ "保存需要时间" + duration.getMillis + "毫秒")
    println("==========推荐阶段===============")
    recommend(model, movieTitle)
    println("完成")
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
    val num=10  //保存条数
    val conn=null
    try{
      val conn=ConnectionPool.getConnection()
      //清空表
      val pst1=conn.prepareStatement(delUsertb)
      pst1.executeUpdate()
      println("清空user成功")
      val pst2=conn.prepareStatement(delItemtb)
      pst2.executeUpdate()
      pst1.close();pst2.close()
      println("清空item成功")
      val pstu = conn.prepareStatement(userSql)
      val psti=conn.prepareStatement(itemSql)
      users.foreach{user =>{
        val arrays=model.recommendProducts(user,num)
        println("用户："+user)
        arrays.foreach{array =>{
          pstu.setInt(1,user)
          pstu.setInt(2,array.product)
          pstu.setDouble(3,array.rating)
          pstu.executeUpdate()
          println("添加成功")
        }}
      }}
      items.foreach(item =>{
        val arrays=model.recommendProducts(item,num)
        //println("用户："+item)
        arrays.foreach{array =>{
          pstu.setInt(1,item)
          pstu.setInt(2,array.product)
          pstu.setDouble(3,array.rating)
          pstu.executeUpdate()
          //println("添加成功")
        }}
      })
      psti.close()
      pstu.close()
    }finally {
      ConnectionPool.returnConnection(conn)
    }
  }

  def recommend(model: MatrixFactorizationModel, movieTitle:Map[Int,String])={
    var choose=""
    while(choose!="3"){
      print("请选择要推荐类型  1.针对用户推荐电影 2.针对电影推荐感兴趣的用户 3.离开?")
      choose=StdIn.readLine()
      if (choose == "1") { //如果输入1.针对用户推荐电影
        print("请输入用户id?")
        val inputUserID = StdIn.readLine() //读取用户ID
        RecommendMovies(model, movieTitle, inputUserID.toInt) //针对此用户推荐电影
      } else if (choose == "2") { //如果输入2.针对电影推荐感兴趣的用户
        print("请输入电影的 id?")
        val inputMovieID =StdIn.readLine()//读取MovieID
        RecommendUsers(model, movieTitle, inputMovieID.toInt) //针对此电影推荐用户
      }
    }
  }

  def SetLogger={
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("com").setLevel(Level.OFF)
    System.setProperty("spark.ui.showConsoleProgress","false")
    Logger.getRootLogger.setLevel(Level.OFF)
  }

  def PrepareData():(RDD[Rating],Map[Int,String])={
    val sc=new SparkContext(new SparkConf().setAppName("RDF").setMaster("local[4]"))
    print("开始读取用户评分数据中...")
    val rawUserData=sc.textFile("testData/u.data")
    val rawRatings=rawUserData.map(_.split("\t").take(3))
    val ratingsRDD=rawRatings.map{
      case Array(user,movie,rating) => Rating(user.toInt,movie.toInt,rating.toDouble)
    }
    println("共计：" + ratingsRDD.count().toString()+ "条ratings")
    //----------------------2.创建电影ID与名称对照表-------------
    print("开始读取电影数据中...")
    val itemRDD=sc.textFile("testData/u.item")
    val movieTitle=itemRDD.map(line => line.split("\\|").take(2)).
      map(array => (array(0).toInt,array(1))).collectAsMap()
    //----------------------3.显示数据记录数-------------
    val numRatings=ratingsRDD.count()
    val numUsers = ratingsRDD.map(_.user).distinct().count()
    val numMovies = ratingsRDD.map(_.product).distinct().count()
    println("共计：ratings: " + numRatings + " User " + numUsers + " Movie " + numMovies)
    return (ratingsRDD, movieTitle.toMap)

  }
  def RecommendMovies(model: MatrixFactorizationModel, movieTitle: Map[Int, String], inputUserID: Int) = {
    val RecommendMovie = model.recommendProducts(inputUserID, 10)
    var i = 1
    println("针对用户id" + inputUserID + "推荐下列电影:")
    RecommendMovie.foreach { r =>
      println(i.toString() + "." + movieTitle(r.product) + "评分:" + r.rating.toString())
      i += 1
    }
  }

  def RecommendUsers(model: MatrixFactorizationModel, movieTitle: Map[Int, String], inputMovieID: Int) = {
    val RecommendUser = model.recommendUsers(inputMovieID, 10)
    var i = 1
    println("针对电影 id" + inputMovieID + "电影名:" + movieTitle(inputMovieID.toInt) + "推荐下列用户id:")
    RecommendUser.foreach { r =>
      println(i.toString + "用户id:" + r.user + "   评分:" + r.rating)
      i = i + 1
    }
  }
}

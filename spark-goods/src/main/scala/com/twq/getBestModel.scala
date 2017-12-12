package com.twq
import java.sql.{ResultSet, DriverManager}

import scala.util.parsing.json.{JSONFormat, JSONObject, JSONArray, JSON}

object getBestModel {

  def bestModel(): (String) ={

    val dbc = "jdbc:mysql://localhost:3306/spark?user=root&password=shenmiaomi"
    classOf[com.mysql.jdbc.Driver]
    val conn = DriverManager.getConnection(dbc)
    var rank=10
    var iterations=10
    var lambda=0.1

    try {
      val sql="select * from evaluation order by rmse limit 1"

      val prep = conn.createStatement()
      val rs=prep.executeQuery(sql)
      while(rs.next){
        rank=rs.getInt("rank")
        iterations=rs.getInt("iterations")
        lambda=rs.getDouble("lambda")
        //println(rs.getInt("rank")+" "+rs.getInt("iterations")+" "+rs.getDouble("lambda")+" "+rs.getDouble("rmse"))
      }
    }
    finally {
      conn.close
    }
    return rank+" "+iterations+" "+lambda
  }

  def main(args: Array[String]) {
    // create database connection
    bestModel()

  }
}
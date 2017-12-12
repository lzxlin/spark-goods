package com.twq;

import java.sql.*;
import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

public class testMysql {

    /**
     * 数据库连接
     */
    public static final String driver="com.mysql.jdbc.Driver";
    //url指向要访问的数据库名spark
    public static final String url="jdbc:mysql://127.0.0.1:3306/spark";
    public static final String user="root";
    public static final String password="shenmiaomi";

    public static void main(String[] args){
        Connection conn=null;
        Statement stmt=null;

        try{
            //conn=DriverManager.getConnection(url,user,password);
            conn=ConnectionPool.getConnection();
            String insertSql="insert into test(userID,num) values(?,?) on duplicate key update num=?";
            //String updateSql="update test set num=? where userID=?";
            PreparedStatement pst=conn.prepareStatement(insertSql);
            Random r=new Random();
            String id="1267e8bbb22d4792ae33182e353e33d7";
            int num=50;
            pst.setString(1,id);
            pst.setInt(2,num);
            pst.setInt(3,num);
            pst.executeUpdate();
            for(int i=0;i<20;i++){
                id=getRandomUUID();
                num=r.nextInt(100);
                pst.setString(1,id);
                pst.setInt(2,num);
                pst.setInt(3,num);
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getRandomUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

}

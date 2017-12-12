package com.twq;

import java.sql.*;
import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

public class  testGroupby {

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
            String sql="select userID,itemID,buy_num from rawdata where buy_num >0 order by userID";
            //String updateSql="update test set num=? where userID=?";
            stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery(sql);
            int i=1;
            while(rs.next()){
                System.out.print(i+":  ");i++;
                System.out.print(rs.getInt(1)+"\t");
                System.out.print(rs.getInt(2)+"\t");
                System.out.print(rs.getInt(3)+"\t");
                System.out.println();
            }
            rs.close();
            stmt.close();
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

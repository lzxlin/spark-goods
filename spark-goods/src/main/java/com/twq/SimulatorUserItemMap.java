package com.twq;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Random;
import java.util.UUID;


public class SimulatorUserItemMap {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        UserItemDataSimulator.run();

    }
}

class UserItemDataSimulator {

    public static final String driver = "com.mysql.jdbc.Driver";
    //url指向要访问的数据库名spark
    public static final String url = "jdbc:mysql://127.0.0.1:3306/spark";
    public static final String user = "root";
    public static final String password = "shenmiaomi";

    private static int GOOD_NUM = 3000;    //商品数
    private static int USER_NUM = 3000;    //用户数

    public static void run() {
        Random r = new Random();
        System.out.println("运行用户商品对照表模拟生成器！！！");
        //连接数据库
        Connection conn = null;
        Statement stmt = null;
        try {
            //创建连接数据库
            conn = DriverManager.getConnection(url, user, password);
            String UserSql = "insert into userMap values(?,?)";
            String ItemSql = "insert into itemMap values(?,?)";
            PreparedStatement pstu = conn.prepareStatement(UserSql);
            PreparedStatement psti = conn.prepareStatement(ItemSql);
            for (int i = 0; i < USER_NUM; i++) {
                int userID = i;
                String userName = getRandomUUID("User-");
                //写进数据库
                pstu.setInt(1, userID);
                pstu.setString(2, userName);
                pstu.executeUpdate();
                System.out.println("User"+i+" :写进数据库成功");
            }
            for (int i = 0; i < GOOD_NUM; i++) {
                int itemID = i;
                String itemName = getRandomUUID("Item-");
                psti.setInt(1, itemID);
                psti.setString(2, itemName);
                psti.executeUpdate();
                System.out.println("Item"+i+" :写进数据库成功");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static String getRandomUUID(String prex) {
        return prex + UUID.randomUUID().toString().replace("-", "").substring(15);
    }

}

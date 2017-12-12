package com.twq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/***
 *
 * 线程池
 */
public class ConnectionPool {

    private static LinkedList<Connection> connectionQueue;

    static{
        try{
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized static Connection getConnection(){
        try{
            if(connectionQueue==null){
                connectionQueue=new LinkedList<Connection>();
                for(int i=0;i<5;i++){
                    Connection conn= DriverManager.getConnection(
                            "jdbc:mysql://127.0.0.1:3306/spark",
                            "root",
                            "shenmiaomi"
                    );
                    connectionQueue.push(conn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connectionQueue.poll();
    }
    public static void returnConnection(Connection conn){
        connectionQueue.push(conn);
    }
}

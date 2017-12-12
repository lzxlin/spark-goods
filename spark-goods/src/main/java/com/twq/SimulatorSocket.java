package com.twq;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Random;


public class SimulatorSocket {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new Thread(new DataSimulator()).start();

	}
}

class DataSimulator implements Runnable{

	public static final String driver="com.mysql.jdbc.Driver";
	//url指向要访问的数据库名spark
	public static final String url="jdbc:mysql://127.0.0.1:3306/spark";
	public static final String user="root";
	public static final String password="shenmiaomi";

	private int GOOD_NUM=50;    //商品数
	private int USER_NUM=50;    //用户数
	private int MSG_NUM=200;     //每次产生的消息数
	private int BROWSE_NUM=5;   //浏览次数
	private int STAY_TIME=10;    //停留时间(分钟)
	private int SCORE=6;    //评分
	int[] COLLECTION=new int[] {-1,0,1};    //是否收藏,-1为差评
	private int[] BUY_NUM=new int[] {0,1,0,2,0,1,3,1,0,3,0,0,2,1,0,1,0,2,1,4,0};     //购买件数
	
	public void run() {
		Random r=new Random();
		
		try {
			ServerSocket serSocket=new ServerSocket(9999);
			System.out.println("打开模拟生成器，现在运行streaming程序!");
			//连接数据库
			Connection conn=null;
			Statement stmt=null;
			try{
				//创建连接数据库
				conn= DriverManager.getConnection(url,user,password);
				String sql="insert into rawData(userID,itemID,browser_num,stay_time,collect,buy_num,scores) values(?,?,?,?,?,?,?)";
				PreparedStatement pst=conn.prepareStatement(sql);
				int time=10;
				while(time!=0) {
					time--;
					//random msg num
					int msgNum=r.nextInt(MSG_NUM)+1;
					//start listen
					Socket socket=serSocket.accept();
					//create outputstream
					OutputStream os=socket.getOutputStream();
					//pack outputstream
					PrintWriter pw=new PrintWriter(os);
					for(int i=0;i<msgNum;i++) {
						//msg format: id::browse time::stay time::coll:buy num
						StringBuffer sb=new StringBuffer();
						int itemID=(r.nextInt(GOOD_NUM)+1);
						int userID=(r.nextInt(USER_NUM)+1);
						int browser_num=r.nextInt(BROWSE_NUM)+1;
						float stay_time=r.nextInt(STAY_TIME)+r.nextFloat();
						int collection=COLLECTION[r.nextInt(3)];
						int buy_num=BUY_NUM[r.nextInt(21)];
						if(collection==-1) {   //如果收藏数为0，则购买数为0
							buy_num=0;
						}
						int score=r.nextInt(SCORE);  //评分0-5
						if(collection==-1){
							score=0;
						}
						//写进数据库
						pst.setInt(1,userID);
						pst.setInt(2,itemID);
						pst.setInt(3,browser_num);
						pst.setFloat(4,stay_time);
						pst.setInt(5,collection);
						pst.setInt(6,buy_num);
						pst.setDouble(7,score);
						pst.executeUpdate();
						System.out.println("写进数据库成功");
						//合并数据
						sb.append(userID);
						sb.append("::");
						sb.append(itemID);
						sb.append("::");
						sb.append(browser_num);
						sb.append("::");
						sb.append(stay_time);
						sb.append("::");
						sb.append(collection);
						sb.append("::");
						sb.append(buy_num);
						sb.append("::");
						sb.append(score);
						System.out.println(sb.toString());
						pw.write(sb.toString()+"\n");
					}
					System.out.println("完成第"+time+"一轮=================================================");
					pw.flush();
					pw.close();
					try {
						Thread.sleep(5000);
					}catch(InterruptedException e) {
						System.out.println("thread sleep failed");
					}
				}
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("conn close failed");
				}
			}
		}catch(IOException ex) {
			System.out.println("port used");
		}
	}
}

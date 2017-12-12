package com.twq;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.api.java.Optional;

import scala.Tuple2;
//import com.google.common.base.Optional;


public class StreamingGoods implements Serializable{
	//define file for saving pre rdd data
	public static String checkpointDir="checkDir";
	public static boolean flag=false;
	
	public static void main(String[] args) throws InterruptedException {
		//setLogger
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("com").setLevel(Level.OFF);
		System.setProperty("spark.ui.showConsoleProgress","false");
		Logger.getRootLogger().setLevel(Level.OFF);

		//setmaster>=2
		SparkConf sparkconf=new SparkConf().setAppName("StreamingGoods").setMaster("local[2]");
		//Spark Streaming需要指定处理数据的时间间隔
		JavaStreamingContext jsc=new JavaStreamingContext(sparkconf,new Duration(5000));
		//恢复点
		jsc.checkpoint(checkpointDir);
		JavaReceiverInputDStream<String> jds=jsc.socketTextStream("127.0.0.1", 9999);
		//获取数据
		JavaDStream<String> mess=jds.map(new Function<String,String>(){
			private static final long serialVersionUID=1L;
			public String call(String arg0) throws Exception{
				if(arg0.isEmpty()){
					System.out.println("没有数据流进来。。。。。。");
				}
				return arg0;
			}
		});
		//mess.print(30);  //打印接收到的数据
		//分割数据以及计算关注度
		JavaPairDStream<String,Double> splitmess=jds.mapToPair(new PairFunction<String,String,Double>(){
			private static final long serialVersionUID=1L;
			public Tuple2<String,Double> call(String line) throws Exception{
				String[] lineSplit=line.toString().split("::");
				if(lineSplit.length==0){
					flag=true;
				}
				//权重计算：浏览次数0.15  停留时间0.15  收藏0.2  购买件数0.2  评分0.3  合为1
				Double followValue=Double.parseDouble(lineSplit[2])*0.15+Double.parseDouble(lineSplit[3])*0.15+
						Double.parseDouble(lineSplit[4])*0.2+Double.parseDouble(lineSplit[5])*0.2+
						Double.parseDouble(lineSplit[4])*0.3;
				return new Tuple2<String,Double>(lineSplit[1],followValue);
			}
		});
		//更新关注度
		JavaPairDStream<String,Double> updateFollowValue=splitmess.updateStateByKey(
				new Function2<List<Double>,Optional<Double>,Optional<Double>>(){
					public Optional<Double> call(List<Double> newValues,Optional<Double> startValue) throws Exception{
						Double updateValue=startValue.or(0.0);
						for(Double values : newValues) {
							updateValue+=values;
						}
						return Optional.of(updateValue);
					}
				});
		//将updateFollowValue写进数据库
		updateFollowValue.foreachRDD(new VoidFunction<JavaPairRDD<String, Double>>() {
			@Override
			public void call(JavaPairRDD<String, Double> stringDoubleJavaPairRDD) throws Exception {

				if(stringDoubleJavaPairRDD.isEmpty()||flag){
					System.out.println("没有数据流入！！！");
				}else{
					JavaPairRDD<String,Double> printdata=stringDoubleJavaPairRDD.mapToPair(new PairFunction<Tuple2<String, Double>, String, Double>() {
						@Override
						public Tuple2<String, Double> call(Tuple2<String, Double> stringDoubleTuple2) throws Exception {
							//System.out.println(stringDoubleTuple2._1+"    "+stringDoubleTuple2._2);
							return new Tuple2<String, Double>(stringDoubleTuple2._1,stringDoubleTuple2._2);
						}
					});
					printdata.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Double>>>() {
						@Override
						public void call(Iterator<Tuple2<String, Double>> tuple2Iterator) throws Exception {
							Connection connection=null;
							try{
								connection=ConnectionPool.getConnection();
								String sql="insert into followData(itemID,followValue) values(?,?) on duplicate key update followValue=?";
								PreparedStatement pst=connection.prepareStatement(sql);
								System.out.println();
								while(tuple2Iterator.hasNext()){
									Tuple2<String,Double> t2=tuple2Iterator.next();
									//打印结果
									System.out.println(t2._1+"    "+t2._2);
									pst.setInt(1,Integer.parseInt(t2._1));
									pst.setDouble(2,t2._2);
									pst.setDouble(3,t2._2);
									pst.executeUpdate();
								}
								pst.close();
							}finally {
								ConnectionPool.returnConnection(connection);
							}
						}
					});
				/*printdata.foreach(new VoidFunction<Tuple2<String, Double>>() {
					@Override
					public void call(Tuple2<String, Double> stringDoubleTuple2) throws Exception {
						System.out.println(stringDoubleTuple2._1+"    "+stringDoubleTuple2._2);
					}
				});*/

				}
			}
		});

		//滑动窗口
		JavaPairDStream<String,Double> windowFollowValue=splitmess.window(new Duration(10000),new Duration(5000));
		windowFollowValue.foreachRDD(new VoidFunction<JavaPairRDD<String, Double>>() {
			@Override
			public void call(JavaPairRDD<String, Double> stringDoubleJavaPairRDD) throws Exception {

				if(stringDoubleJavaPairRDD.isEmpty()||flag){
					System.out.println("滑动窗口未满");
				}else{
					JavaPairRDD<String,Double> printdata=stringDoubleJavaPairRDD.mapToPair(new PairFunction<Tuple2<String, Double>, String, Double>() {
						@Override
						public Tuple2<String, Double> call(Tuple2<String, Double> stringDoubleTuple2) throws Exception {
							//System.out.println(stringDoubleTuple2._1+"    "+stringDoubleTuple2._2);
							return new Tuple2<String, Double>(stringDoubleTuple2._1,stringDoubleTuple2._2);
						}
					});
					printdata.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Double>>>() {
						@Override
						public void call(Iterator<Tuple2<String, Double>> tuple2Iterator) throws Exception {
							Connection connection=null;
							try{
								connection=ConnectionPool.getConnection();
								String sql="insert into windowFollowData(itemID,followValue) values(?,?) on duplicate key update followValue=?";
								String singeSql="insert into singleFollowValue(windowFollowValue) values(?)";
								PreparedStatement pst=connection.prepareStatement(sql);
								PreparedStatement pst2=connection.prepareStatement(singeSql);
								System.out.println();
								boolean has=false;
								while(tuple2Iterator.hasNext()){
									Tuple2<String,Double> t2=tuple2Iterator.next();
									//打印结果
									System.out.println(t2._1+"    "+t2._2);
									pst.setInt(1,Integer.parseInt(t2._1));
									pst.setDouble(2,t2._2);
									pst.setDouble(3,t2._2);
									pst.executeUpdate();
									if(Integer.parseInt(t2._1)==1){
										pst2.setDouble(1,t2._2);
										pst2.executeUpdate();
										has=true;
									}
								}
								if(has==false){
									pst2.setDouble(1,0);
									pst2.executeUpdate();
								}
								pst.close();
								pst2.close();
							}finally {
								ConnectionPool.returnConnection(connection);
							}
						}
					});
				/*printdata.foreach(new VoidFunction<Tuple2<String, Double>>() {
					@Override
					public void call(Tuple2<String, Double> stringDoubleTuple2) throws Exception {
						System.out.println(stringDoubleTuple2._1+"    "+stringDoubleTuple2._2);
					}
				});*/

				}
			}
		});
		/*输出关注度
		updateFollowValue.foreachRDD(new VoidFunction<JavaPairRDD<String,Double>>(){
			private static final long serialVersionUID=1L;
			public void call(JavaPairRDD<String,Double> followValue) throws Exception{
				//没有数据
				if(followValue.isEmpty()){
					System.out.println("没有数据输入！！！");
				}else{
					JavaPairRDD<Double,String> followValueSort=followValue.mapToPair(new PairFunction<Tuple2<String,Double>,Double,String>(){

						public Tuple2<Double,String> call(Tuple2<String,Double> valueToKey) throws Exception{
							return new Tuple2<Double,String>(valueToKey._2,valueToKey._1);
						}
					}).sortByKey(false);
					//将排序结果输出到文件中
					//followValueSort.count();

					List<Tuple2<String,Double>> list;
					list = followValueSort.mapToPair(new PairFunction<Tuple2<Double,String>,String,Double>(){

						public Tuple2<String,Double> call(Tuple2<Double,String> arg0) throws Exception{
							return new Tuple2<String,Double>(arg0._2,arg0._1);
						}
					}).collect();   //输出所有值
					for(Tuple2<String,Double> tu : list) {
						System.out.println("商品ID: "+tu._1+"  关注度: "+tu._2);
					}
				}
			}
		});*/
		jsc.start();
		jsc.awaitTermination();
		jsc.stop(true);
		jsc.close();
			
	}

}

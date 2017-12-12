package bigdata.utils;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    public static DataSource dataSource;
    static {
        try{
            Properties prop = new Properties();
            InputStream in = JDBCUtils.class.getClassLoader().getResourceAsStream("db.properties");
            prop.load(in);
            BasicDataSourceFactory factory = new BasicDataSourceFactory();
            dataSource = factory.createDataSource(prop);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static int testHaveData(String sql){
        Connection con = null;
        try{
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(1)+"  "+rs.getDouble(2));
            }
            rs.close();
            //rs.last();
            //System.out.println("sql: "+sql+" count:"+rs.getInt(1));
            return 0;
        }catch ( Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(con != null){
                    con.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static Properties getProperties(){
        Properties prop = new Properties();
        try{
            InputStream in = JDBCUtils.class.getClassLoader().getResourceAsStream("/db.properties");
            prop.load(in);
            return prop;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    /*public static void main(String[] args){
        String sql="select * from followdata limit 10";
        System.out.println(testHaveData(sql));
    }*/
}

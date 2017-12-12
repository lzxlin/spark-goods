package bigdata.Dao;

import bigdata.domain.Follow;
import bigdata.domain.rawData;
import bigdata.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class rawDao {
    public List<rawData> getDatas(){
        try{
            String sql = "select userID,itemID,browser_num,stay_time,collect,buy_num,scores from rawdata order by id desc limit 50";
            System.out.println("sql: "+sql);
            QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
            List<rawData> beanList = runner.query(sql,new BeanListHandler<rawData>(rawData.class));

            return  beanList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

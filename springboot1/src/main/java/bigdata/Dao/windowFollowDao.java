package bigdata.Dao;

import bigdata.domain.Follow;
import bigdata.domain.windowFollow;
import bigdata.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class windowFollowDao {
    public List<windowFollow> getDatas(){
        try{
            String sql = "select * from windowFollowData order by followValue desc limit 10";
            System.out.println("sql: "+sql);
            QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
            List<windowFollow> beanList = runner.query(sql,new BeanListHandler<windowFollow>(windowFollow.class));

            return  beanList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

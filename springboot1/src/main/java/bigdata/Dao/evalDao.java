package bigdata.Dao;

import bigdata.domain.Follow;
import bigdata.domain.eval;
import bigdata.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class evalDao {
    public List<eval> getDatas(){
        try{
            String sql = "select rank,iterations,lambda,rmse,time from evaluation";
            System.out.println("sql: "+sql);
            QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
            List<eval> beanList = runner.query(sql,new BeanListHandler<eval>(eval.class));

            return  beanList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

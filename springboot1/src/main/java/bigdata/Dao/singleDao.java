package bigdata.Dao;

import bigdata.domain.Follow;
import bigdata.domain.single;
import bigdata.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class singleDao {
    public List<single> getDatas(){
        try{
            String sql = "select windowFollowValue from singleFollowValue";
            System.out.println("sql: "+sql);
            QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
            List<single> beanList = runner.query(sql,new BeanListHandler<single>(single.class));

            return  beanList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

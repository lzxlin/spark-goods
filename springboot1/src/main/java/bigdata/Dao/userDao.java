package bigdata.Dao;

import bigdata.domain.userR;
import bigdata.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class userDao {
    public List<userR> getDatas(){
        try{
            String sql = "select userID,itemID,scores from userrecommend";
            System.out.println("sql: "+sql);
            QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
            List<userR> beanList = runner.query(sql,new BeanListHandler<userR>(userR.class));
            /*for(int i=0;i<beanList.size();i++){
                System.out.println(beanList.get(i).toString());
            }*/
            return  beanList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

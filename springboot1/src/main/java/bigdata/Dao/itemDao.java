package bigdata.Dao;

import bigdata.domain.itemR;
import bigdata.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class itemDao {
    public List<itemR> getDatas(){
        try{
            String sql = "select itemID,userID,scores from itemrecommend";
            System.out.println("sql: "+sql);
            QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
            List<itemR> beanList = runner.query(sql,new BeanListHandler<itemR>(itemR.class));
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

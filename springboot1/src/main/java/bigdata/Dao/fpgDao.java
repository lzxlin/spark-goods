package bigdata.Dao;

import bigdata.domain.Follow;
import bigdata.domain.eval;
import bigdata.domain.fpg;
import bigdata.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class fpgDao {
    public List<fpg> getDatas(){
        try{
            String sql = "select antecedent,consequent,confidence from fpg";
            System.out.println("sql: "+sql);
            QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
            List<fpg> beanList = runner.query(sql,new BeanListHandler<fpg>(fpg.class));
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

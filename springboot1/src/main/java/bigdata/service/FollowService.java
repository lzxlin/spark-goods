package bigdata.service;

import bigdata.Dao.FollowDao;
import bigdata.Dao.allFollowDao;
import bigdata.domain.Follow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class FollowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowService.class);

    @Autowired
    private FollowDao followDao;
    @Autowired
    private allFollowDao allFollowDao;

    public List<Follow> getData(){
        List<Follow> datas = followDao.getDatas();
        //List<Beans> datas = new ArrayList<Beans>();
       /* int key = 0;
        int value = 0;
        for(int i=0;i<50;i++){
            key = i + 1;
            value = 50+new Random().nextInt(50);
            Beans beans = new Beans();
            beans.setKey_(key);
            beans.setValue_(value);
            datas.add(beans);
        }*/
        return datas;
    }

    public List<Follow> getAllData(){
        List<Follow> datas = allFollowDao.getDatas();
        return datas;
    }
}
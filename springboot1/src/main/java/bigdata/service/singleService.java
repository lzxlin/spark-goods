package bigdata.service;

import bigdata.Dao.FollowDao;
import bigdata.Dao.allFollowDao;
import bigdata.Dao.singleDao;
import bigdata.domain.Follow;
import bigdata.domain.single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class singleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(singleService.class);

    @Autowired
    private singleDao singleDao;

    public List<single> getData(){
        List<single> datas = singleDao.getDatas();
        return datas;
    }
}
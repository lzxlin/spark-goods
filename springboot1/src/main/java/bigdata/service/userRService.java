package bigdata.service;

import bigdata.Dao.FollowDao;
import bigdata.Dao.allFollowDao;
import bigdata.Dao.userDao;
import bigdata.domain.Follow;
import bigdata.domain.userR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class userRService {
    private static final Logger LOGGER = LoggerFactory.getLogger(userRService.class);

    @Autowired
    private userDao userDao;

    public List<userR> getData(){
        List<userR> datas = userDao.getDatas();
        return datas;
    }
}
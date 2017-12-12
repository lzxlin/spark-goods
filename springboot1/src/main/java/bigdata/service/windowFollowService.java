package bigdata.service;

import bigdata.Dao.FollowDao;
import bigdata.Dao.allFollowDao;
import bigdata.Dao.windowFollowDao;
import bigdata.domain.Follow;
import bigdata.domain.windowFollow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class windowFollowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(windowFollowService.class);

    @Autowired
    private windowFollowDao windowFollowDao;

    public List<windowFollow> getData(){
        List<windowFollow> datas = windowFollowDao.getDatas();
        return datas;
    }
}
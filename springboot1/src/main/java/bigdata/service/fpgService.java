package bigdata.service;

import bigdata.Dao.FollowDao;
import bigdata.Dao.allFollowDao;
import bigdata.Dao.fpgDao;
import bigdata.domain.Follow;
import bigdata.domain.fpg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class fpgService {
    private static final Logger LOGGER = LoggerFactory.getLogger(fpgService.class);

    @Autowired
    private fpgDao fpgDao;
    public List<fpg> getData(){
        List<fpg> datas = fpgDao.getDatas();
        return datas;
    }
}
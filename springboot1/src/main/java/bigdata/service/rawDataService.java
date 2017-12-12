package bigdata.service;

import bigdata.Dao.rawDao;
import bigdata.domain.rawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class rawDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(rawDataService.class);

    @Autowired
    private rawDao rawDao;

    public List<rawData> getData(){
        List<rawData> datas = rawDao.getDatas();
        return datas;
    }
}
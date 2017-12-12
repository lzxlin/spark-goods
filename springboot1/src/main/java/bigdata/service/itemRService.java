package bigdata.service;


import bigdata.Dao.itemDao;
import bigdata.domain.itemR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class itemRService {
    private static final Logger LOGGER = LoggerFactory.getLogger(itemRService.class);

    @Autowired
    private itemDao itemDao;

    public List<itemR> getData(){
        List<itemR> datas = itemDao.getDatas();
        return datas;
    }
}
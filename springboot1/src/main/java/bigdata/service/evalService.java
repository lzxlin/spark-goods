package bigdata.service;

import bigdata.Dao.evalDao;
import bigdata.domain.eval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class evalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(evalService.class);

    @Autowired
    private evalDao evalDao;
    public List<eval> getData(){
        List<eval> datas = evalDao.getDatas();
        return datas;
    }
}
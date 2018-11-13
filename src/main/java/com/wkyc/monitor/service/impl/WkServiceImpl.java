package com.wkyc.monitor.service.impl;

import com.github.myth.annotation.Myth;
import com.wkyc.monitor.dao.TccFailureDao;
import com.wkyc.monitor.dao.TccTransactionMailDao;
import com.wkyc.monitor.domain.TccTransactionMail;
import com.wkyc.monitor.service.TccFailureService;
import com.wkyc.monitor.service.WkService;
import com.wkyc.wkcoin.api.service.WkcoinTransactionService;
import com.zjlp.face.util.json.JsonUtil;
import com.zjlp.face.util.mail.MailUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *  tcc事务失败服务
 * @author LiuXingHai
 * @date 2018/7/4
 */
@Service("wkService")
public class WkServiceImpl implements WkService{

    @Resource
    private WkcoinTransactionService wkcoinTransactionService;


    @Override
    @Myth
    public void failureTransactionSendMail() {
        wkcoinTransactionService.increaseStandBy(118L,1L,1,2,"2","2","remark");
    }
}

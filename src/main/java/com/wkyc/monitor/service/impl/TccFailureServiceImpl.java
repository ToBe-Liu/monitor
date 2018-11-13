package com.wkyc.monitor.service.impl;

import com.wkyc.monitor.dao.TccFailureDao;
import com.wkyc.monitor.dao.TccTransactionMailDao;
import com.wkyc.monitor.domain.TccTransactionMail;
import com.wkyc.monitor.service.TccFailureService;
import com.zjlp.face.util.json.JsonUtil;
import com.zjlp.face.util.mail.MailUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  tcc事务失败服务
 * @author LiuXingHai
 * @date 2018/7/4
 */
@Service("tccFailureService")
public class TccFailureServiceImpl implements TccFailureService{

    @Autowired
    private TccFailureDao tccFailureDao;
    @Autowired
    private TccTransactionMailDao tccTransactionMailDao;


    @Override
    public void failureTransactionSendMail() {
        List<String> transactions = tccFailureDao.selectFailureTransaction();
        if (CollectionUtils.isNotEmpty(transactions)){
            MailUtil.sendMail("事务失败提醒！","以下事务失败啦："+ JsonUtil.fromCollection(transactions));
            //记录已经发送邮件的失败事务
            List<TccTransactionMail> tccTransactionMails = new ArrayList<>(transactions.size());
            transactions.forEach(transaction -> tccTransactionMails.add(new TccTransactionMail(transaction)));
            tccTransactionMailDao.insertBatch(tccTransactionMails);
        }
    }
}

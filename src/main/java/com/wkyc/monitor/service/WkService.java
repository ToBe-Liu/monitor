package com.wkyc.monitor.service;

import com.github.myth.annotation.Myth;

/**
 *  tcc事务失败服务
 * @author LiuXingHai
 * @date 2018/7/4
 */
public interface WkService {

    /**
     *  失败的tcc事务 发送邮件
     */
    void  failureTransactionSendMail();
}

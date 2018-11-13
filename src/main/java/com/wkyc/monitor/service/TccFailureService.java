package com.wkyc.monitor.service;

/**
 *  tcc事务失败服务
 * @author LiuXingHai
 * @date 2018/7/4
 */
public interface TccFailureService {

    /**
     *  失败的tcc事务 发送邮件
     */
    void  failureTransactionSendMail();
}

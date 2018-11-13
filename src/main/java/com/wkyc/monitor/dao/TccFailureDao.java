package com.wkyc.monitor.dao;

import java.util.List;

/**
 *  tcc事务失败服务
 * @author LiuXingHai
 * @date 2018/7/4
 */
public interface TccFailureDao {


    /**
     *  查询所有失败的tcc事务
     */
    List<String> selectFailureTransaction();
}

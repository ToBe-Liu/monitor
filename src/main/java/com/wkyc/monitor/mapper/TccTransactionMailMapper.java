package com.wkyc.monitor.mapper;


import com.wkyc.monitor.domain.TccTransactionMail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TccTransactionMailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TccTransactionMail record);

    int insertSelective(TccTransactionMail record);

    TccTransactionMail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TccTransactionMail record);

    int updateByPrimaryKeyWithBLOBs(TccTransactionMail record);

    int updateByPrimaryKey(TccTransactionMail record);

    List<TccTransactionMail> selectAll();

    int deleteBatch(@Param("globalTxIds") List<String> globalTxIds);

    int insertBatch(@Param("tccTransactionMails") List<TccTransactionMail> tccTransactionMails);
}
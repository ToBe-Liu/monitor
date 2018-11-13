package com.wkyc.monitor.dao;

import com.wkyc.monitor.aspect.ParamsValidate;
import com.wkyc.monitor.domain.TccTransactionMail;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *  tcc失败事务发送邮件关联表DAO
 * @author LiuXingHai
 * @date 2018/7/4
 */
@ParamsValidate
public interface TccTransactionMailDao {


    /**
     *  查询所有已经发送邮件的失败的tcc事务
     */
    List<TccTransactionMail> selectAll();

    /**
     *  批量删除已经发送邮件的失败的tcc事务
     */
    int deleteBatch(@NotNull List<String> globalTxIds,@NotNull String error);
    /**
     *  删除已经发送邮件的失败的tcc事务
     */
    int delete(@Validated TccTransactionMail tccTransactionMail);
    /**
     *  批量添加已经发送邮件的失败的tcc事务
     */
    int insertBatch(List<TccTransactionMail> tccTransactionMails);
}

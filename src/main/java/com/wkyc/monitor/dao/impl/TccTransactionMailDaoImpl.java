package com.wkyc.monitor.dao.impl;

import com.wkyc.monitor.aspect.BeanValidate;
import com.wkyc.monitor.aspect.Groups;
import com.wkyc.monitor.aspect.ParamsValidate;
import com.wkyc.monitor.dao.TccTransactionMailDao;
import com.wkyc.monitor.domain.TccTransactionMail;
import com.wkyc.monitor.mapper.TccTransactionMailMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  tcc事务失败服务
 * @author LiuXingHai
 * @date 2018/7/4
 */
@Repository("tccTransactionMailDao")
public class TccTransactionMailDaoImpl implements TccTransactionMailDao {

    @Autowired
    @Qualifier("sqlSession")
    private SqlSession sqlSession;

    @Override
    public List<TccTransactionMail> selectAll() {
        return sqlSession.getMapper(TccTransactionMailMapper.class).selectAll();
    }

    @Override
    @ParamsValidate
    public int deleteBatch(List<String> globalTxIds,String e) {
        return sqlSession.getMapper(TccTransactionMailMapper.class).deleteBatch(globalTxIds);
    }

    @Override
    @BeanValidate(groups = {Groups.GroupA.class})
    public int delete(TccTransactionMail tccTransactionMail) {
        return 0;
    }

    @Override
    public int insertBatch(List<TccTransactionMail> tccTransactionMails) {
        return sqlSession.getMapper(TccTransactionMailMapper.class).insertBatch(tccTransactionMails);
    }
}

package com.wkyc.monitor.dao.impl;

import com.wkyc.monitor.dao.TccFailureDao;
import com.wkyc.monitor.dao.TccTransactionMailDao;
import com.wkyc.monitor.domain.TccTransactionMail;
import com.wkyc.monitor.utils.JdbcUtil;
import com.zjlp.face.util.file.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  tcc事务失败服务
 * @author LiuXingHai
 * @date 2018/7/4
 */
@Repository("tccFailureDao")
public class TccFailureDaoImpl implements TccFailureDao{
    private static final Logger logger = LoggerFactory.getLogger(TccFailureDaoImpl.class);
    /** 排除的表名 */
    private static final String EXCLUDE_TABLE_NAME="tcc_transaction_mail";
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    private TccTransactionMailDao tccTransactionMailDao;


    @Override
    public List<String> selectFailureTransaction() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        List<String> transactions = new ArrayList<>();
        try {
            //加载所有的tcc事务表
            List<String> tables = JdbcUtil.getTables(connection);
            //排除事务邮件关联表
            List<String> collect = tables.stream()
                    .filter(table -> !EXCLUDE_TABLE_NAME.equals(table))
                    .collect(Collectors.toList());
            //查询所有失败的事务
            for (String table : collect) {
                List<String> transaction = doFindAllUnmodifiedSince(connection, table);
                transactions.addAll(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //过滤已经发送过的邮件的失败事务
        List<TccTransactionMail> tccTransactionMails = tccTransactionMailDao.selectAll();
        List<String> collect = transactions.stream().filter(transaction ->
                tccTransactionMails.stream().noneMatch(tccTransactionMail -> tccTransactionMail.getGlobalTxId().equals(transaction))
        ).collect(Collectors.toList());
        return collect;
    }

    protected List<String> doFindAllUnmodifiedSince(Connection conn,String tableName) {
        List<String> transactionXids = new ArrayList<>();
        PreparedStatement stmt = null;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT a.TRANSACTION_ID, a.DOMAIN");
            builder.append("  FROM ").append(tableName).append(" a ")
                    .append(" WHERE a.RETRIED_COUNT > ? ");

            logger.debug(builder.toString());
            stmt = conn.prepareStatement(builder.toString());
            Integer retriedCount = Integer.valueOf(PropertiesUtil.getContexrtParam("RETRIED_COUNT"));
            logger.debug("当前查询失败事务的失败次数：{}",retriedCount);
            stmt.setInt(1, retriedCount);

            ResultSet resultSet = stmt.executeQuery();

            this.constructTransactions(resultSet, transactionXids);
        } catch (Throwable e) {
            logger.error("查询失败事务出错：{}",e);
        } finally {
            closeStatement(stmt);
        }

        return transactionXids;
    }

    protected void constructTransactions(ResultSet resultSet, List<String> transactionXids) throws SQLException {
        while (resultSet.next()) {
            Long transactionId = resultSet.getLong(1);
            logger.debug("transactionId:"+transactionId);
            String domain = resultSet.getString(2);
            logger.debug("domain:"+domain);
            transactionXids.add(transactionId+":"+domain);
        }
    }
    private void closeStatement(Statement stmt) {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (Exception ex) {
            logger.error("关闭连接失败：{}",ex);
        }
    }
}

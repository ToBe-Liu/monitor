package com.wkyc.monitor.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Jdbc 工具类
 * @author LiuXingHai
 * @date 2018/7/4
 */
public class JdbcUtil {
    /**获取数据库中所有表名称
     * @param conn
     * @return
     * @throws SQLException
     */
    public static List<String> getTables(Connection conn) throws SQLException {
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        ResultSet tables = databaseMetaData.getTables(null, null, "%", null);
        ArrayList<String> tablesList = new ArrayList<String>();
        while (tables.next()) {
            tablesList.add(tables.getString("TABLE_NAME"));
        }
        return tablesList;
    }
}

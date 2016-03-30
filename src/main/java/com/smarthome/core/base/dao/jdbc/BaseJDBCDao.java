package com.smarthome.core.base.dao.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
/**
 * JDBC 操作基类
 * @author RM
 *
 */
public abstract  class BaseJDBCDao{
	private static Logger log = Logger.getLogger(BaseJDBCDao.class);
	/**
	 * 关闭JDBC连接
	 * @param connection
	 * @param pstmt
	 * @param rs
	 */
	public static void closeAllConnection(Connection connection,Statement pstmt,ResultSet rs) {
		try {
			if(rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}finally{
				try {
					if(connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					log.error(e.getMessage());
				}
			}
		}
	}
	

}

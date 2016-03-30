package com.smarthome.platform.monitor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.smarthome.core.base.dao.jdbc.BaseJDBCDao;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.platform.monitor.bean.GpsInfo;

@Repository
public class HostJDBCDao extends BaseJDBCDao{

	private static Logger log = Logger.getLogger(HostJDBCDao.class);
	
	private static DataSource dataSource;

	public static void setDataSource(DataSource dataSource) {
		HostJDBCDao.dataSource = dataSource;
	}

	/**
	 * 保存位置信息
	 * @param gi
	 */
	public static void savePosition(GpsInfo gi){
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("call Prc_SavePosition(?,?,?,?,?,?,?,?,?,?,?,?)");
			//call Prc_SavePosition(#{deviceid},#{time},#{latitude},#{ns},#{longitude},#{ew},#{speed},#{direction},#{angle},#{angledirection},#{satellites},#{elevation}
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1 , gi.getDeviceid());
			pstmt.setString(2 , gi.getTime());
			pstmt.setString(3 , gi.getLatitude());
			pstmt.setString(4 , gi.getNs());
			pstmt.setString(5 , gi.getLongitude());
			pstmt.setString(6 , gi.getEw());
			pstmt.setString(7 , gi.getSpeed());
			pstmt.setString(8 , gi.getDirection());
			pstmt.setString(9 , gi.getAngle());
			pstmt.setString(10 , gi.getAngledirection());
			pstmt.setString(11 , gi.getSatellites());
			pstmt.setString(12 , gi.getElevation());
			rs = pstmt.executeQuery();
//			while(rs.next()){
//				resultRoleStringList.add(rs.getString("role_id"));
//			}
		}catch(Exception e){
			log.error(e.getMessage() , e);
			log.error("保存位置信息失败:" + JsonUtils.getJAVABeanJSON(gi));
		}finally {
			closeAllConnection(connection, pstmt, rs);
		}
	}
	
	/**
	 * 设备离线
	 * @param id
	 */
	public static void offline (String id){
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("call Prc_Offline(?)");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1 , id);
			rs = pstmt.executeQuery();
		}catch(Exception e){
			log.error(e.getMessage() , e);
			log.error("离线设备失败:" + id);
		}finally {
			closeAllConnection(connection, pstmt, rs);
		}
	}
	
	/**
	 * 设备心跳
	 * @param id
	 */
	public static void heartbeat (String id){
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("call Prc_Heartbead(?)");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1 , id);
			rs = pstmt.executeQuery();
		}catch(Exception e){
			log.error(e.getMessage() , e);
			log.error("保存设备心跳失败:" + id);
		}finally {
			closeAllConnection(connection, pstmt, rs);
		}
	}
}

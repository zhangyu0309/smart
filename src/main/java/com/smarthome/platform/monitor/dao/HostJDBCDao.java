package com.smarthome.platform.monitor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.smarthome.core.base.dao.jdbc.BaseJDBCDao;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.platform.monitor.bean.Command;
import com.smarthome.platform.monitor.bean.SensorData;

@Repository
public class HostJDBCDao extends BaseJDBCDao{

	private static Logger log = Logger.getLogger(HostJDBCDao.class);
	
	private static DataSource dataSource;

	public static void setDataSource(DataSource dataSource) {
		HostJDBCDao.dataSource = dataSource;
	}

	/**
	 * 保存信息
	 */
	public static void saveData(SensorData s){
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("insert into device_data(device_id,temp1,wet1,temp2,wet2,light) values");
			tempSql.append(" (?,?,?,?,?,?)");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, s.getDevice_id());
			pstmt.setString(2, s.getTemp1());
			pstmt.setString(3, s.getWet1());
			pstmt.setString(4, s.getTemp2());
			pstmt.setString(5, s.getWet2());
			pstmt.setString(6, s.getLight());
			if (!pstmt.execute()){
				log.info("error insert : " + JsonUtils.toJson(s));
			}
			
			pstmt.close();
			pstmt = null;
			StringBuilder tempSql1 = new StringBuilder("update device_info set temp1=?,wet1=?,temp2=?,wet2=?,light=?,data_time=CURRENT_TIMESTAMP,online=1 where device_id=?");
			pstmt = connection.prepareStatement(tempSql1.toString());
			
			pstmt.setString(1, s.getTemp1());
			pstmt.setString(2, s.getWet1());
			pstmt.setString(3, s.getTemp2());
			pstmt.setString(4, s.getWet2());
			pstmt.setString(5, s.getLight());
			pstmt.setString(6, s.getDevice_id());
			if (!pstmt.execute()){
				//log.info("error update : " + JsonUtils.toJson(s));
			}
		}catch(Exception e){
			log.error(e.getMessage() , e);
		}finally {
			closeAllConnection(connection, pstmt, rs);
		}
	}
	
	
	/**
	 * 上下线
	 */
	public static void online(int online, String device_id){
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("update device_info set online=? where device_id= ? or parent_id=?");
			if (online == 1){
				tempSql = new StringBuilder("update device_info set online=? where device_id= ?");
			}
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setInt(1, online);
			pstmt.setString(2, device_id);
			if (online == 0){
				pstmt.setString(3, device_id);
			}
			pstmt.execute();
		}catch(Exception e){
			log.error(e.getMessage() , e);
		}finally {
			closeAllConnection(connection, pstmt, rs);
		}
	}

	/**
	 * 获取一条开关设备的指令
	 * @param device_id
	 * @return
	 */
	public static Command getCommand(String device_id) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("select * from command ");
			tempSql.append(" where device_id like ");
			tempSql.append(" ? order by cid limit 0,1");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, device_id + "%");
			rs = pstmt.executeQuery();
			while(rs.next()){
				return new Command(rs.getInt("cid"), rs.getString("device_id"), rs.getInt("operation"));
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}finally {
			closeAllConnection(connection, pstmt, rs);
		}
		return null;
	}

	/**
	 * 开关设备的指令处理完成 删除指令信息
	 * @param cid
	 */
	public static void deleteCommand(int cid) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("delete from command where cid=?");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setInt(1, cid);
			pstmt.execute();
		}catch(Exception e){
			log.error(e.getMessage() , e);
		}finally {
			closeAllConnection(connection, pstmt, rs);
		}
	}
}

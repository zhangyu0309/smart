package com.smarthome.platform.monitor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.smarthome.core.base.dao.jdbc.BaseJDBCDao;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.platform.monitor.bean.Command;
import com.smarthome.platform.monitor.bean.DeviceBoard;
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
				return new Command(rs.getInt("cid"), rs.getString("device_id"), rs.getInt("operation"), 
						rs.getInt("board_id"), rs.getInt("key_id"), rs.getString("content"));
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

	public static void updateDeviceKey(DeviceBoard deviceBoard) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("update device_key set value1=?, value2=? where device_id=? and board_id=? and key_id=?");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, deviceBoard.getValue1());
			pstmt.setString(2, deviceBoard.getValue2());
			pstmt.setString(3, deviceBoard.getDeviceId());
			pstmt.setString(4, deviceBoard.getBoardId());
			pstmt.setString(5, deviceBoard.getKeyId());
			pstmt.execute();
		}catch(Exception e){
			log.error(e.getMessage() , e);
		}finally {
			closeAllConnection(connection, pstmt, rs);
		}
	}

	public static DeviceBoard getDeviceBoard(String device_id, int board_id,
			int key_id) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("select * from device_key ");
			tempSql.append(" where device_id = ");
			tempSql.append(" ? and board_id=? and key_id=? limit 0,1");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, device_id);
			pstmt.setInt(2, board_id);
			pstmt.setInt(3, key_id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				return new DeviceBoard(rs.getString("device_id"), rs.getString("board_id"), rs.getString("key_id"),
						rs.getString("value1"), rs.getString("value2"));
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
	 * 更新开关状态 仅限子设备
	 * @param deviceId
	 * @param onoff
	 */
	public static void updateSubDeviceStatus(String deviceId, int onoff) {
		if (deviceId.contains("-") && !deviceId.endsWith("-")){
			Connection connection = null;
			PreparedStatement pstmt = null;
			try{
				connection = dataSource.getConnection();
				StringBuilder tempSql = new StringBuilder("update device_info ");
				tempSql.append("set onoff=? where device_id = ?");
				pstmt = connection.prepareStatement(tempSql.toString());
				pstmt.setInt(1, onoff);
				pstmt.setString(2, deviceId);
				pstmt.execute();
				log.info("device:" + deviceId + "-->" + onoff);
			}catch(Exception e){
				e.printStackTrace();
				log.error(e.getMessage());
			}finally {
				closeAllConnection(connection, pstmt, null);
			}
		}
	}

	public static void setKeyUpdated(Command command, int i) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		System.out.println(JsonUtils.getJAVABeanJSON(command));
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("update device_key ");
			tempSql.append("set updated=? where device_id = ? and board_id=? and key_id=?");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setInt(1, i);
			pstmt.setString(2, command.getDevice_id());
			pstmt.setInt(3, command.getBoard_id());
			pstmt.setInt(4, command.getKey_id());
			pstmt.execute();
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}finally {
			closeAllConnection(connection, pstmt, null);
		}
	}

	/**
	 * 查询当前定时任务
	 * @param device_id
	 * @return
	 */
	public static List<com.smarthome.platform.monitor.bean.Timer> getTimerList(
			String device_id) {
		List<com.smarthome.platform.monitor.bean.Timer> result = new ArrayList<com.smarthome.platform.monitor.bean.Timer>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
//			select id,device_id,type,action_time,weeks,week_time,action from device_timer where 
//			device_id like '1988606%' and ((type=0 and DATEDIFF(CURRENT_TIMESTAMP,action_time)=1) or 
//			(type=1 and weeks like '%7%'))
//			and `status`=1 order by add_time desc limit 0,7
			StringBuilder tempSql = new StringBuilder("select id,device_id,type,action_time,weeks,week_time,action from device_timer ");
			tempSql.append(" where device_id like ? ");
			tempSql.append(" and ((type=0 and DATEDIFF(CURRENT_TIMESTAMP,action_time)=0) or ");
			tempSql.append(" (type=1 and weeks like ?)) ");
			tempSql.append(" and `status`=1 order by week_time");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, device_id + "%");
			pstmt.setString(2, "%" + getWeek() + "%");
			rs = pstmt.executeQuery();
			while(rs.next()){
				com.smarthome.platform.monitor.bean.Timer tempTimer = new com.smarthome.platform.monitor.bean.Timer();
				tempTimer.setId(rs.getString("id"));
				tempTimer.setDevice_id(rs.getString("device_id"));
				tempTimer.setType(rs.getString("type"));
				tempTimer.setAction_time(rs.getString("action_time"));
				tempTimer.setWeek_time(rs.getString("week_time"));
				tempTimer.setAction(rs.getString("action"));
				tempTimer.setWeeks(rs.getString("weeks"));
				result.add(tempTimer);
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}finally {
			closeAllConnection(connection, pstmt, rs);
		}
		return result;
	}
	
	/**
	 * 当前周几
	 * @return
	 */
	private static String getWeek(){
		String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
	    int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
//	    if (week < 0){
//	    	week = 0;
//	    }
//	    week ++;
	    return weekDays[week];
	}
}

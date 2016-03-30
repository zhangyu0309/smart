package com.smarthome.platform.authority.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.smarthome.core.base.dao.jdbc.BaseJDBCDao;

@Repository
public class UserJDBCDao extends BaseJDBCDao{

	private static Logger log = Logger.getLogger(UserJDBCDao.class);
	@Autowired
	private DataSource dataSource;
	/**
	 *  根据用户Id获取该用户下的角色Id号队列
	 * @param userId
	 * @return
	 */
	public List<String> getRoleStringsByUserId(String userId){
		List<String> resultRoleStringList = new ArrayList<String>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("select role_id from auth_user_role where user_id= ?");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				resultRoleStringList.add(rs.getString("role_id"));
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("根据用户Id号获取该用户所属的角色队列数组失败，具体原因:"+e.getMessage());
		}finally {
			this.closeAllConnection(connection, pstmt, rs);
		}
		return resultRoleStringList;
	}
}

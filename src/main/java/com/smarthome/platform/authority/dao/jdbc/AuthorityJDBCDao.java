package com.smarthome.platform.authority.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.smarthome.core.base.dao.jdbc.BaseJDBCDao;
import com.smarthome.platform.authority.bean.Authority;
@Repository
public class AuthorityJDBCDao extends BaseJDBCDao {
	
	private static Logger log = Logger.getLogger(AuthorityJDBCDao.class);
	@Autowired
	private DataSource dataSource;
	/**
	 * 根据用户获取该用户的菜单权限
	 * @param userId
	 * @return
	 */
	public List<Authority> getMenuAuthorityByUserId(String userId){
		List<Authority> resultList = new ArrayList<Authority>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("select distinct authority_id,authority_name,parent_id,menu_url,icon_cls,data_index " +
					" from auth_authority au where au.enabled =1 and type=1 and au.authority_id in");
			tempSql.append(" (select ra.authority_id from auth_role_authority ra where ra.role_id in");
			tempSql.append(" (select r.role_id from auth_role r where r.role_id in ");
			tempSql.append(" (select ur.role_id from auth_user_role ur where ur.user_id=?))) order by data_index asc");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Authority tempBean = new Authority();
				tempBean.setAuthorityId(rs.getString("authority_id"));
				tempBean.setAuthorityName(rs.getString("authority_name"));
				tempBean.setIconCls(rs.getString("icon_cls"));
				tempBean.setMenuUrl(rs.getString("menu_url"));
				tempBean.setParentId(rs.getString("parent_id"));
				resultList.add(tempBean);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("获取用户菜单权限时出错，具体原因:"+e.getMessage());
		}finally {
			this.closeAllConnection(connection, pstmt, rs);
		}
		return resultList;
	}
	/**
	 * 根据用户获取该用户的菜单权限及其对应的操作权限
	 * @param userId
	 * @return
	 */
	public Map<String,Authority> getOpAuthorityByUserId(String userId){
		Map<String,Authority> resultMap = new  HashMap<String,Authority>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("select distinct authority_id,authority_name,parent_id,menu_url,type,icon_cls,data_index " +
					" from auth_authority au where au.enabled =1  and au.parent_id is not null and au.parent_id <>'' and au.authority_id in");
			tempSql.append(" (select ra.authority_id from auth_role_authority ra where ra.role_id in");
			tempSql.append(" (select r.role_id from auth_role r where r.role_id in ");
			tempSql.append(" (select ur.role_id from auth_user_role ur where ur.user_id=?))) order by data_index asc");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Authority tempBean = new Authority();
				tempBean.setAuthorityId(rs.getString("authority_id"));
				tempBean.setAuthorityName(rs.getString("authority_name"));
				tempBean.setIconCls(rs.getString("icon_cls"));
				tempBean.setMenuUrl(rs.getString("menu_url"));
				tempBean.setParentId(rs.getString("parent_id"));
				tempBean.setDataIndex(rs.getInt("data_index"));
				tempBean.setType(rs.getInt("type"));
				resultMap.put(tempBean.getAuthorityId(), tempBean);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("获取用户菜单权限时出错，具体原因:"+e.getMessage());
		}finally {
			this.closeAllConnection(connection, pstmt, rs);
		}
		return resultMap;
	}
	/**
	 * 获取所有可用功能菜单选项
	 * 
	 * @return
	 */
	public List<Authority> getMenuAuthority(){
		List<Authority> resultList = new ArrayList<Authority>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("select distinct authority_id,authority_name,parent_id,menu_url,icon_cls,enabled,type,data_index,description " +
					" from auth_authority au order by data_index asc");
			pstmt = connection.prepareStatement(tempSql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				Authority tempBean = new Authority();
				tempBean.setAuthorityId(rs.getString("authority_id"));
				tempBean.setAuthorityName(rs.getString("authority_name"));
				tempBean.setIconCls(rs.getString("icon_cls"));
				tempBean.setMenuUrl(rs.getString("menu_url"));
				tempBean.setParentId(rs.getString("parent_id"));
				tempBean.setDescription(rs.getString("description"));
				tempBean.setEnabled(rs.getInt("enabled"));
				tempBean.setType(rs.getInt("type"));
				tempBean.setDataIndex(rs.getInt("data_index"));
				resultList.add(tempBean);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("获取功能菜单权限时出错，具体原因:"+e.getMessage());
		}finally {
			this.closeAllConnection(connection, pstmt, rs);
		}
		return resultList;
	}
	/**
	 * 获取所有父级功能菜单选项
	 * 
	 * @return
	 */
	public List<Authority> getAllParentMenu(){
		List<Authority> resultList = new ArrayList<Authority>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("select distinct authority_id,authority_name,parent_id,menu_url,icon_cls,enabled,type,data_index,description " +
					" from auth_authority au  where type = 1 order by data_index asc");
			pstmt = connection.prepareStatement(tempSql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				Authority tempBean = new Authority();
				tempBean.setAuthorityId(rs.getString("authority_id"));
				tempBean.setAuthorityName(rs.getString("authority_name"));
				tempBean.setIconCls(rs.getString("icon_cls"));
				tempBean.setMenuUrl(rs.getString("menu_url"));
				tempBean.setParentId(rs.getString("parent_id"));
				tempBean.setEnabled(rs.getInt("enabled"));
				tempBean.setType(rs.getInt("type"));
				tempBean.setDescription(rs.getString("description"));
				resultList.add(tempBean);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("获取菜单权限时出错，具体原因:"+e.getMessage());
		}finally {
			this.closeAllConnection(connection, pstmt, rs);
		}
		return resultList;
	} 
	/**
	 * 根据Id号获取菜单对象
	 * @param authorityId
	 * @return
	 */
	public Authority getMenuById(String authorityId){
		Authority tempBean = new Authority();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("select distinct authority_id,authority_name,parent_id,menu_url,icon_cls,enabled,type,data_index,description " +
					" from auth_authority au  where authority_id = ? order by data_index asc");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, authorityId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				tempBean.setAuthorityId(rs.getString("authority_id"));
				tempBean.setAuthorityName(rs.getString("authority_name"));
				tempBean.setIconCls(rs.getString("icon_cls"));
				tempBean.setMenuUrl(rs.getString("menu_url"));
				tempBean.setParentId(rs.getString("parent_id"));
				tempBean.setEnabled(rs.getInt("enabled"));
				tempBean.setType(rs.getInt("type"));
				tempBean.setDataIndex(rs.getInt("data_index"));
				tempBean.setDescription(rs.getString("description"));
				break;
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("根据Id号获取菜单对象时出错，具体原因:"+e.getMessage());
		}finally {
			this.closeAllConnection(connection, pstmt, rs);
		}
		return tempBean;
	} 
	/**
	 * 根据Id号删除功能菜单Menu
	 * 
	 * @return
	 */
	public boolean delMenuById(String authorityId){
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("DELETE FROM auth_authority WHERE authority_id = ? OR parent_id= ? OR authority_id IN (SELECT a.authority_id FROM (SELECT b.authority_id FROM auth_authority b WHERE b.parent_id IN (SELECT c.authority_id FROM (SELECT d.authority_id FROM auth_authority d WHERE d.parent_id=?) c )) a)");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, authorityId);
			pstmt.setString(2, authorityId);
			pstmt.setString(3, authorityId);
			pstmt.execute();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			log.error("删除菜单权限时出错，具体原因:"+e.getMessage());
			return false;
		}finally {
			this.closeAllConnection(connection, pstmt, rs);
		}
	} 
	/**
	 * 查找摸一个父亲节点最后一个还在节点
	 * @param parentId
	 * @return
	 */
	public Authority getMenuByParentId(String parentId){
		Authority tempBean = new Authority();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("select * from auth_authority where parent_id = ? order by data_index desc");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, parentId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				tempBean.setAuthorityId(rs.getString("authority_id"));
				tempBean.setAuthorityName(rs.getString("authority_name"));
				tempBean.setIconCls(rs.getString("icon_cls"));
				tempBean.setMenuUrl(rs.getString("menu_url"));
				tempBean.setParentId(rs.getString("parent_id"));
				tempBean.setEnabled(rs.getInt("enabled"));
				tempBean.setType(rs.getInt("type"));
				tempBean.setDataIndex(rs.getInt("data_index"));
				tempBean.setDescription(rs.getString("description"));
				break;
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("根据parentId号获取菜单对象时出错，具体原因:"+e.getMessage());
		}finally {
			this.closeAllConnection(connection, pstmt, rs);
		}
		return tempBean;
	}
	/**
	 * 获取角色下的权限列表
	 * @param roleId 角色Id号
	 * @return
	 */
	public Map<String, Authority> getAuthorityByRoleId(String roleId) {
		Map<String,Authority> resultMap = new HashMap<String,Authority>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("select distinct a.* from auth_authority a left join auth_role_authority ra on (a.authority_id=ra.authority_id) where ra.role_id = ? order by a.type desc");
			pstmt = connection.prepareStatement(tempSql.toString());
			pstmt.setString(1, roleId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Authority tempBean = new Authority();
				tempBean.setAuthorityId(rs.getString("authority_id"));
				tempBean.setAuthorityName(rs.getString("authority_name"));
				tempBean.setIconCls(rs.getString("icon_cls"));
				tempBean.setMenuUrl(rs.getString("menu_url"));
				tempBean.setParentId(rs.getString("parent_id"));
				tempBean.setEnabled(rs.getInt("enabled"));
				tempBean.setType(rs.getInt("type"));
				tempBean.setDataIndex(rs.getInt("data_index"));
				tempBean.setDescription(rs.getString("description"));
				resultMap.put(tempBean.getAuthorityId(), tempBean);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("根据parentId号获取菜单对象时出错，具体原因:"+e.getMessage());
		}finally {
			this.closeAllConnection(connection, pstmt, rs);
		}
		return resultMap;
	} 
	/**
	 * 获取所有克勇的权限
	 * @param roleId 角色Id号
	 * @return
	 */
	public Map<String, Authority> getlAllAuthorityForMap() {
		Map<String,Authority> resultMap = new HashMap<String,Authority>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			
			connection = dataSource.getConnection();
			StringBuilder tempSql = new StringBuilder("select distinct a.* from auth_authority a  where a.enabled = 1 order by a.data_index asc");
			pstmt = connection.prepareStatement(tempSql.toString());
			rs = pstmt.executeQuery();
			while(rs.next()){
				Authority tempBean = new Authority();
				tempBean.setAuthorityId(rs.getString("authority_id"));
				tempBean.setAuthorityName(rs.getString("authority_name"));
				tempBean.setIconCls(rs.getString("icon_cls"));
				tempBean.setMenuUrl(rs.getString("menu_url"));
				tempBean.setParentId(rs.getString("parent_id"));
				tempBean.setEnabled(rs.getInt("enabled"));
				tempBean.setType(rs.getInt("type"));
				tempBean.setDataIndex(rs.getInt("data_index"));
				tempBean.setDescription(rs.getString("description"));
				resultMap.put(tempBean.getAuthorityId(), tempBean);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("根据parentId号获取菜单对象时出错，具体原因:"+e.getMessage());
		}finally {
			this.closeAllConnection(connection, pstmt, rs);
		}
		return resultMap;
	} 
}

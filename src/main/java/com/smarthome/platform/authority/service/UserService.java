package com.smarthome.platform.authority.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.smarthome.core.util.MD5;
import com.smarthome.core.util.ObjectConvertUtil;
import com.smarthome.core.util.PageBean;
import com.smarthome.platform.authority.bean.Admin;
import com.smarthome.platform.authority.bean.Sms;
import com.smarthome.platform.authority.dao.jdbc.UserJDBCDao;
import com.smarthome.platform.authority.dao.mysql.AdminDao;
/**
 * 主要用于管理员人员的管理
 * @author apple
 *
 */
@Service
public class UserService {
	@Resource
	private AdminDao userDao;
	@Resource
	private UserJDBCDao userJdbcDao;
	
	/**
	 * 根据条件分页获取符合条件的数据
	 * @param admin  查询条件对象
	 * @param pageBean 分页实体
	 * @return
	 */
	public List<Admin> getUserPageByParams(Admin admin ,PageBean pageBean){
		List<Admin> resultList;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("start", pageBean.getStart());
		map.put("limit", pageBean.getLimit());
		//将对象级别的对象转换成Map对象级别的对象
		if(admin!=null){
			map.putAll(ObjectConvertUtil.beanToMap(admin));
		}
		//获取符合条件的用户总数
		pageBean.setTotalItems(this.userDao.getUserCountByParams(map));
		resultList = this.userDao.getUserPageByParams(map);
		if(resultList==null){
			resultList = new ArrayList<Admin>();
		}
		return resultList;
	}
	/**
	 * 根据用户Id号获取用户对象
	 * @param userId
	 * @return
	 */
	public Admin getUserId(String userId){
		
		return this.userDao.getAdminById(userId);
	}

	/**
	 * 根据用户Id号删除用户
	 * @param id 用户Id号
	 * @return
	 */
	public boolean delUser(String userId) {
		if(this.userDao.deleteById(userId)==1){
			//删除用户对应的用户角色表
			this.userDao.deleteUserRoleById(userId);
			return true;
		}
		return false;
	}
	/**
	 * 冻结及其取消冻结用户
	 * @param id 用户Id号
	 * @return
	 */
	public boolean updateUser(Admin admin) {
		if(this.userDao.updateAdminByBean(admin)==1){
			return true;
		}
		return false;
	}
	/**
	 * 根据用户Id号获取该用户对应的角色信息
	 * @param userId
	 * @return
	 */
	public List<String> getUserRoleListByUserId(String userId){
		return this.userJdbcDao.getRoleStringsByUserId(userId);
	}
	/**
	 * 添加管理员用户
	 * @param admin
	 * @return
	 */
	public boolean addUser(Admin admin) {
		admin.setPassword(MD5.GetMD5Code("123456a?"));
		if(this.userDao.addAdmin(admin)==1){
		   if(admin.getRoles()!=null){
			   String role[] = admin.getRoles().split(",");
			   for(int i=0;i<role.length;i++){
				   Map<String,Object> map = new HashMap<String,Object>();
				   map.put("userId", admin.getUserId());
				   map.put("roleId", role[i]);
				   try {
					this.userDao.addAdminWithRole(map);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }
		   }	
		   return true;
		}
		return false;
	}
	/**
	 * 编辑管理用户
	 * @param admin
	 * @return
	 */
	public boolean eidtUser(Admin admin) {
		if(this.userDao.updateAdminByBean(admin)==1){
			 //删除该用户下的所有角色
			this.userDao.deleteUserRoleById(admin.getUserId());
			//更新该用户下的所有角色
			   if(admin.getRoles()!=null){
				   String role[] = admin.getRoles().split(",");
				   for(int i=0;i<role.length;i++){
					   Map<String,Object> map = new HashMap<String,Object>();
					   map.put("userId", admin.getUserId());
					   map.put("roleId", role[i]);
					   try {
						this.userDao.addAdminWithRole(map);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				   }
			   }	
			   return true;
			}
		return false;
	}

}

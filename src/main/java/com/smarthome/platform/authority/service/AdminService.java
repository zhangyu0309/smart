package com.smarthome.platform.authority.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.smarthome.core.util.MD5;
import com.smarthome.platform.authority.bean.Admin;
import com.smarthome.platform.authority.bean.Authority;
import com.smarthome.platform.authority.bean.Sms;
import com.smarthome.platform.authority.dao.jdbc.AuthorityJDBCDao;
import com.smarthome.platform.authority.dao.mysql.AdminDao;

/**
 * 管理员用户的管理
 * 
 * @author RM
 * 
 */
@Service
public class AdminService {

	@Resource
	private AdminDao adminDao;
	@Resource
	private AuthorityJDBCDao authorityJdbcDao;

	/**
	 * 用户登录
	 * 
	 * @param userId
	 *            管理员账号9
	 * @param passwd
	 *            管理员密码
	 * @return
	 */
	public Map<String, Object> isLoginSuccess(String userId, String passwd) {
		Map<String, Object> map = new HashMap<String, Object>();
		Admin tempAdmin = this.adminDao.getAdminById(userId);
		if (tempAdmin != null) {
			if (tempAdmin.getPassword().equals(MD5.GetMD5Code(passwd))) {
				if(tempAdmin.getEnable()==1){
					map.put("flag", true);
					map.put("msg", "登录成功！");
					map.put("Admin", tempAdmin);
					// 获取用户权限
				}else{
					map.put("flag", false);
					map.put("msg", "该用户已被冻结，无法登录！");
					map.put("Admin", tempAdmin);
				}
			} else {
				map.put("flag", false);
				map.put("msg", "密码输入不正确");
			}
		} else {
			map.put("flag", false);
			map.put("msg", "该账号不存在");
		}
		return map;
	}
	/**
	 * 获取用户的各个菜单选项的操作权限
	 * @param userId
	 * @return
	 */
	public Map<String,List<Authority>> getOPAuthorityList(String userId){
		Map<String,List<Authority>> resultMap = new HashMap<String,List<Authority>>();
		// 获取所有的操作权限列表
		Map<String,Authority> tempMap = this.authorityJdbcDao.getOpAuthorityByUserId(userId);
		//第一遍获取所有的菜单菜单权限
		for (String key : tempMap.keySet()) {
      	  Authority tempBean = tempMap.get(key);
      	  if(tempBean.getType()==1){
      		if(resultMap.get(tempBean.getMenuUrl())==null){
      			resultMap.put(tempBean.getMenuUrl(), new ArrayList<Authority>());
      		}
      	  }
      	  //为各个菜单配置具体的功能操作权限
      	  if(tempBean.getType()==2){
      		  //获取父亲节点的Key值
      		  Authority parentAuthority = tempMap.get(tempBean.getParentId());
      		  //获取父亲节点最终在权限列表中的值
      		  if(resultMap.get(parentAuthority.getMenuUrl())!=null){
      			resultMap.get(parentAuthority.getMenuUrl()).add(tempBean);
      		  }else{
      			  resultMap.put(parentAuthority.getMenuUrl(), new ArrayList<Authority>());
      			  resultMap.get(parentAuthority.getMenuUrl()).add(tempBean);
      		  }
      	  }
        }
		//权限排序 保持前台按钮的顺序
		for (String key : resultMap.keySet()) {
			Collections.sort(resultMap.get(key));
		}
		return resultMap;
	}
	/**
	 * 密码修改
	 * 
	 * @param passwd
	 *            密码
	 * @return
	 */
	public boolean changePasswd(String passwd, String userId) {
		Admin tempAdmin = new Admin();
		tempAdmin.setUserId(userId);
		tempAdmin.setPassword(MD5.GetMD5Code(passwd));
		if (this.adminDao.updateAdminByBean(tempAdmin) == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据手机号获取该用户信息
	 * @param phone
	 * @return
	 */
	public Admin getUserByPhone(String phone) {
		return this.adminDao.getUserByPhone(phone);
	}
	/**
	 * 保存短信发送的记录
	 * @param sms
	 */
	public void saveSms(Sms sms) {
		this.adminDao.saveSms(sms);
	}
	
	/**
	 * 查询手机验证信息
	 * @param authkey
	 * @return
	 */
	public Sms getSmsInfo(String authkey) {
		return this.adminDao.getSmsInfo(authkey);
	}
	/**
	 * 重置用户密码
	 * @param tempAdmin
	 */
	public boolean resetPasswd(Admin tempAdmin) {
		if (this.adminDao.updateAdminByBean(tempAdmin) == 1) {
			return true;
		} else {
			return false;
		}
	}
}

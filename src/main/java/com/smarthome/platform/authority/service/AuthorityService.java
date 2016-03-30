package com.smarthome.platform.authority.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.smarthome.core.util.JsonUtils;
import com.smarthome.platform.authority.bean.Authority;
import com.smarthome.platform.authority.dao.jdbc.AuthorityJDBCDao;
import com.smarthome.platform.authority.dao.mysql.AuthorityDao;

/**
 * 权限操作类
 * 
 * @author RM
 * 
 */
@Service
public class AuthorityService {
	private static Logger log = Logger.getLogger(AuthorityService.class);
	@Resource
	private AuthorityJDBCDao authorityJDBCDao;
	@Resource
	private AuthorityDao authorityDao;

	/**
	 * 获取登录用户的菜单权限
	 * 
	 * @param userId
	 *            用户账号
	 * @return 菜单权限队列
	 */
	public List<Authority> getMenuAuthorityByUserId(String userId) {
		return this.authorityJDBCDao.getMenuAuthorityByUserId(userId);
	}

	/**
	 * 获取素有的有孩子节点的菜单
	 * 
	 * @return
	 */
	public List<Authority> getAllParentMenuAuthority() {
		return this.authorityJDBCDao.getAllParentMenu();
	}

	/**
	 * 获取所有的功能菜单权限项
	 * 
	 * @return
	 */
	public List<Authority> getMenuAuthority() {
		return this.authorityJDBCDao.getMenuAuthority();
	}

	/**
	 * 添加权限菜单选项
	 * 
	 * @param bean
	 *            添加对象
	 * @return
	 */
	public boolean addAuthority(Authority bean) {
		try {
			if(bean.getParentId()==null||bean.getParentId().trim().equals("")){
				bean.setParentId(" ");
			}
			// 查询当前排列序号
			Authority tempBean = this.authorityJDBCDao.getMenuByParentId(bean
					.getParentId());
			System.out.println("当前节点的最近的兄弟节点对象信息："+JsonUtils.getJAVABeanJSON(tempBean));
			if (tempBean.getDataIndex() == null) {
				bean.setDataIndex(0);
			} else {
				bean.setDataIndex(tempBean.getDataIndex() + 1);
			}
			if (this.authorityDao.addAuthority(bean) == 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("添加权限菜单出错，具体原因:" + e.getMessage());
			return false;
		}
	}

	/**
	 * 更新
	 * 
	 * @param bean
	 * @return
	 */
	public boolean updateAuthority(Authority bean) {
		try {
			if (this.authorityDao.updateAuthorityById(bean) == 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("更新权限菜单出错，具体原因:" + e.getMessage());
			return false;
		}
	}

	/**
	 * 根据Id号删除功能菜单
	 * 
	 * @param bean
	 * @return
	 */
	public boolean delAuthorityById(Authority bean) {
		return authorityJDBCDao.delMenuById(bean.getAuthorityId());
	}

	/**
	 * 根据Id号获取功能菜单对象
	 * 
	 * @param id
	 * @return
	 */
	public Authority getAuthorityById(String id) {
		return this.authorityJDBCDao.getMenuById(id);
	}

	/**
	 * 获取角色下的权限
	 * 
	 * @return
	 */
	public List<Authority> getAuthorityByRoleId(String roleId){
		List<Authority> resultList = new ArrayList<Authority>();
		Map<String,Authority> allMap = this.authorityJDBCDao.getlAllAuthorityForMap();
        if(roleId!=null&&!roleId.trim().equals("")){
        	//对比数值
			Map<String,Authority> map = this.authorityJDBCDao.getAuthorityByRoleId(roleId);
			for (String key : map.keySet()) {
	        	  Authority tempBean = map.get(key);
	        	  if(tempBean.getType()==2){
	        		  allMap.get(tempBean.getAuthorityId()).setChecked(true);
	        		  Authority tempAuthorityBean = allMap.get(tempBean.getParentId());
	        		  if(tempAuthorityBean!=null){
	        			  allMap.get(tempBean.getParentId()).setHasChildren(true);
	        		  }
	        	  }else if(tempBean.getType()==1){
	        		  if(tempBean.getParentId()!=null&&!tempBean.getParentId().trim().equals("")){
	        			  if(tempBean.isHasChildren()){
	        				  allMap.get(tempBean.getAuthorityId()).setChecked(false);
	        			  }else{
	        				  Authority tempB = allMap.get(tempBean.getAuthorityId());
	        				  if(tempB!=null){
	        					  allMap.get(tempBean.getAuthorityId()).setChecked(true);
	        				  }
	        				 
	        			  }
	        		  }else {
	        			  allMap.get(tempBean.getAuthorityId()).setChecked(false);
	        		  }
	        	  }
	        }
			
		}
		//构造好权限树
        for (String key : allMap.keySet()) {
        	  Authority tempBeanAuthority = allMap.get(key);
        	  if(tempBeanAuthority.isHasChildren()&&tempBeanAuthority.getParentId()!=null){
        		  tempBeanAuthority.setChecked(false);
        	  }
        	  resultList.add(tempBeanAuthority);
        }
		return resultList;
	}
}

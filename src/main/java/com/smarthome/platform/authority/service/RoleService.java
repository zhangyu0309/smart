package com.smarthome.platform.authority.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.smarthome.core.util.PageBean;
import com.smarthome.platform.authority.bean.Role;
import com.smarthome.platform.authority.dao.mysql.RoleDao;

/**
 * 角色维护
 * 
 * @author RM
 * 
 */
@Service
public class RoleService {
	private static Logger log = Logger.getLogger(RoleService.class);
	@Resource
	private RoleDao roleDao;

	/**
	 * 分页获取角色列表
	 * 
	 * @param pageBean
	 *            分页信息对象
	 * @return
	 */
	public List<Role> getRoleByPage(PageBean pageBean) {
		List<Role> resultList;
		// 构造查询条件及其分页信息
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("start", pageBean.getStart());
		tempMap.put("limit", pageBean.getLimit());
		// 获取符合条件的总数目
		pageBean.setTotalItems(this.roleDao.getRoleCountByParams(tempMap));
		// 获取符合条件的数据对象
		resultList = this.roleDao.getRoleByPage(tempMap);
		if (resultList == null) {
			resultList = new ArrayList<Role>();
		} 
		return resultList;
	}

	/**
	 * 根据角色Id号删除角色对象
	 * 
	 * @param role
	 *            角色对象
	 * @return
	 */
	public boolean delRoleByRId(Role role) {
		// 删除角色
		if (this.roleDao.deleteRoleById(role.getRoleId()) == 1) {
			// 删除该角色权限表中的内容
			this.roleDao.deleteRoleAuthorityByRoleId(role.getRoleId());
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 根据角色Id号获取对应的角色及其该角色下的权限详细信息
	 * 
	 * @param roleId
	 *            角色Id号
	 * @return
	 */
	public Role getRoleWithoutAuthorityById(String roleId) {
		// this 获取角色的详细信息
		return this.roleDao.getRoleWithAuthorityById(roleId);
	}

	/**
	 * 添加角色
	 * 
	 * @param role
	 *            角色对象
	 * @param ids
	 *            角色对应的权限Id号队列
	 * @return
	 */
	public boolean addRole(Role role, String ids) {
		if (this.roleDao.add(role) == 1) {
			// 添加角色对应的权限问题
			Map<String, Object> map = new HashMap<String, Object>();
			
			
			if (ids != null && ids.length() > 0) {
				String tempIds[] = ids.substring(0, ids.length()-1).split(",");
				for (int i = 0; i < tempIds.length; i++) {
					map.put("roleId", role.getRoleId());
					map.put("authorityId", tempIds[i]);
					try {
						this.roleDao.addRoleWithAuthority(map);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.error(role.getRoleName() + " 添加" + tempIds[i] + "权限失败"
								+ e.getMessage());
					}
				}
				
			}
			return true;
		}
		return false;
	}

	public static void main(String[] args) {

	}
	/**
	 * 编辑角色
	 * @param role 角色对象
	 * @param ids  角色对应的权限值
	 * @return
	 */
	public boolean editRole(Role role, String ids) {
		// 删除角色以前对应的权限值
		if(this.roleDao.updateRole(role)==1){
			this.roleDao.deleteRoleAuthorityByRoleId(role.getRoleId());
			Map<String, Object> map = new HashMap<String, Object>();
			String tempIds[] = ids.substring(0, ids.length()-1).split(",");
			for (int i = 0; i < tempIds.length; i++) {
				map.put("roleId", role.getRoleId());
				map.put("authorityId", tempIds[i]);
				try {
					this.roleDao.addRoleWithAuthority(map);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error(role.getRoleName() + " 添加" + tempIds[i] + "权限失败"
							+ e.getMessage());
				}
			}
			return true;
		}
		
		return false;
	}

}

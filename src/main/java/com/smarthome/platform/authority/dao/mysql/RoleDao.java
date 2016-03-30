package com.smarthome.platform.authority.dao.mysql;

import java.util.List;
import java.util.Map;

import com.smarthome.platform.authority.bean.Role;

public interface RoleDao {
	/**
	 * 删除角色表
	 * @param roleId
	 * @return
	 */
    int deleteRoleById(String roleId);
    /**
     * 删除角色权限对应关系表
     * @param roleId
     * @return
     */
    int deleteRoleAuthorityByRoleId(String roleId);
    /**
     * 添加角色对应的权限信息
     * @param map
     * @return
     */
    int addRoleWithAuthority(Map<String,Object> map);
    /**
     * 添加角色信息
     * @param record 角色信息对象
     * @return
     */
    int add(Role record);
    /**
     * 根据角色ID号获取该角色下的所有权限
     * @param roleId 角色Id号
     * @return
     */
    Role getRoleWithAuthorityById(String roleId);
    /**
     * 分页获取角色分类
     * @param map 条件及其分页信息
     * @return
     */
    List<Role> getRoleByPage(Map<String,Object> map);
    /**
     * 获取符合条件角色的总数目
     * @param map 条件对象
     * @return
     */
    int  getRoleCountByParams(Map<String,Object> map);
    /**
     * 删除角色对应的所有权限
     * @param roleId
     * @return
     */
    int deleteRoleAuthorityById(String roleId);

    /**
     * 更新角色信息
     * @param record
     * @return
     */
    int updateRole(Role record);

    int updateByPrimaryKey(Role record);
}
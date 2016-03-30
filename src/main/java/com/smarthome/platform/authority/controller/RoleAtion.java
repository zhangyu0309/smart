package com.smarthome.platform.authority.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.smarthome.core.base.action.BaseAction;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.core.util.PageBean;
import com.smarthome.core.util.UUIDGenerator;
import com.smarthome.platform.authority.bean.Authority;
import com.smarthome.platform.authority.bean.Role;
import com.smarthome.platform.authority.service.AuthorityService;
import com.smarthome.platform.authority.service.RoleService;

/**
 * 角色维护接口类
 * 
 * @author RM
 * 
 */
public class RoleAtion extends BaseAction {

	@Resource
	private RoleService roleService;
	@Resource
	private AuthorityService authorityService;
	/**
	 * 当前页 默认第一页
	 */
	private int page = 1;
	/**
	 * 每页数据数目 默认25条
	 */
	private int rows = 25;
	/**
	 * 参数对象（前台传值）
	 */
    private Role role ;
    private String ids;
    /**
	 * 编辑的类型 0 代表添加 1 代表更新
	 */
	private int editType;
	/**
	 * 如果是添加则是 新添加对象的父亲ID号；如果是更新则是自身的ID号。
	 */
	private String id;
	/**
	 * 获取所有的角色列表
	 * 
	 * @return
	 */
	public String getAll() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 创建分页结构
		PageBean pageBean = new PageBean(this.page, this.rows);
		List<Role> resultList = this.roleService.getRoleByPage(pageBean);
		map.put("rows", resultList);
		map.put("total", pageBean.getTotalItems());
		map.put("totalPage", pageBean.getTotalPage());
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取某一个角色下的权限列表
	 * @return
	 */
	public String getAllAuthorityWith(){
		List<Authority> allAuthroityList = this.authorityService.getAuthorityByRoleId(this.id);
		System.out.println("resultList="+JsonUtils.getJAVABeanJSON(allAuthroityList));
		this.jsonString = JsonUtils.getJAVABeanJSON(allAuthroityList);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 开始添加或者更新功能菜单
	 * @return
	 */
	public String beginAddOrUpdate(){
		
		if(this.editType==0){
			return "add_method";
		}else{
			//根据ID号获取对象值
			this.role = this.roleService.getRoleWithoutAuthorityById(this.id);
//			System.out.println("当前查询出的对象:"+JsonUtils.getJAVABeanJSON(this.role));
			return "update_method";
		}
		
	}
	/**
	 * 添加角色接口
	 * @return
	 */
	public String add(){
		Map<String,Object> map = new HashMap<String,Object>();
		System.out.println("IDS="+this.ids);
		if(this.role!=null&&this.role.getRoleName()!=null){
			this.role.setRoleId(UUIDGenerator.getUUID());
			//开启添加
			if(this.roleService.addRole(this.role,ids)){
				map.put("flag", true);
				map.put("msg", "添加成功!");
			}else{
				map.put("flag", false);
				map.put("msg", "添加失败，请检查数据库连接问题！");
			}
		}else{
			map.put("flag", false);
			map.put("msg", "传递的参数为空");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 添加角色接口
	 * @return
	 */
	public String edit(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.role!=null&&this.role.getRoleId()!=null){
			if(this.roleService.editRole(this.role,ids)){
				map.put("flag", true);
				map.put("msg", "更新成功!");
			}else{
				map.put("flag", false);
				map.put("msg", "更新失败，请检查数据库连接问题！");
			}
		}else{
			map.put("flag", false);
			map.put("msg", "传递的参数为空");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据角色Id号删除角色对象
	 * @return
	 */
	public String delete(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.role!=null&&this.role.getRoleId()!=null){
			//开启删除操作
			if(this.roleService.delRoleByRId(this.role)){
				map.put("flag", true);
				map.put("msg", "删除成功!");
			}else{
				map.put("flag", false);
				map.put("msg", "删除失败，数据库问题！");
			}
		}else{
			map.put("flag", false);
			map.put("msg", "传递的参数为空");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 
	 * 一下方法只提供Struts内部封装使用
	 * 
	 */
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
    public void setEditType(int editType) {
		this.editType = editType;
	}
    
    public int getEditType() {
		return editType;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

	
	

}

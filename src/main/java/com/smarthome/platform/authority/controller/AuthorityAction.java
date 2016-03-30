package com.smarthome.platform.authority.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.smarthome.core.base.action.BaseAction;
import com.smarthome.core.common.AuthorityCommon;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.core.util.UUIDGenerator;
import com.smarthome.platform.authority.bean.Admin;
import com.smarthome.platform.authority.bean.Authority;
import com.smarthome.platform.authority.service.AuthorityService;

/**
 * 权限操作接口
 * @author RM
 *
 */
public class AuthorityAction extends BaseAction{

	private static Logger log = Logger.getLogger(AuthorityAction.class);
	private static final long serialVersionUID = 1L;
	@Resource
	private AuthorityService authorityService;
	/**
	 * 权限参数对象
	 */
	private Authority authority;
	/**
	 * 编辑的类型 0 代表添加 1 代表更新
	 */
	private int editType;
	/**
	 * 如果是添加则是 新添加对象的父亲ID号；如果是更新则是自身的ID号。
	 */
	private String id;
	/**
	 * 用于权限上下移动，权限1的ID
	 */
	private String authorityId1;
	/**
	 * 用于权限上下移动，权限2的ID
	 */
	private String authorityId2;
	
	/**
	 * 用于权限上下移动，权限1的INDEX
	 */
	private int index1;
	/**
	 * 用于权限上下移动，权限2的INDEX
	 */
	private int index2;
	/**
	 * 获取功能菜单接口
	 * @return
	 */
	public String getMenu(){
		Admin sessionAdmin = (Admin) this.getSession(AuthorityCommon.ADMIN_SESSION);
		this.jsonString = JsonUtils.getJAVABeanJSON(this.authorityService.getMenuAuthorityByUserId(sessionAdmin.getUserId()));
		try {
			this.responseWriter(jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}
	/**
	 * 获取有孩子节点菜单的接口
	 * @return
	 */
	public String getAllParentMenu(){
		this.jsonString = JsonUtils.getJAVABeanJSON(this.authorityService.getAllParentMenuAuthority());
		try {
			this.responseWriter(jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}
	/**
	 * 获取所有的功能菜单权限列表( 菜单和菜单中的按钮 )
	 * @return
	 */
	public String getAllData(){
		//获取所有的可用的功能选项
		this.jsonString = JsonUtils.getJAVABeanJSON(this.authorityService.getMenuAuthority());
		try {
			this.responseWriter(jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
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
			this.authority = this.authorityService.getAuthorityById(this.id);
//			System.out.println("当前查询出的对象:"+JsonUtils.getJAVABeanJSON(this.authority));
			return "update_method";
		}
		
	}
	/**
	 * 添加权限菜单
	 * @return
	 */
	public String add(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.authority!=null){
			this.authority.setAuthorityId(UUIDGenerator.getUUID());
			//开启添加
			if(this.authorityService.addAuthority(this.authority)){
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
	 * 添加权限菜单
	 * @return
	 */
	public String edit(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.authority!=null&&this.authority.getAuthorityId()!=null){
			//开启添加
			if(this.authorityService.updateAuthority(this.authority)){
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
	 * 删除功能菜单选项
	 * @return
	 */
	public String delete(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.authority!=null&&this.authority.getAuthorityId()!=null){
			//开启删除操作
			if(this.authorityService.delAuthorityById(this.authority)){
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
	 * 权限项上下移动
	 * @return
	 */
	public String move(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.authorityId1 != null && !this.authorityId1.equals("") && 
				this.authorityId2 != null && !this.authorityId2.equals("")){
			Authority auth = new Authority();
			auth.setAuthorityId(this.authorityId1);
			auth.setDataIndex(this.index2);
			//开启移动操作,交换两个权限的dataindex
			if(this.authorityService.updateAuthority(auth)){
				auth.setAuthorityId(this.authorityId2);
				auth.setDataIndex(this.index1);
				if(this.authorityService.updateAuthority(auth)){
					map.put("flag", true);
					map.put("msg", "移动权限位置成功!");
				}else{
					auth.setAuthorityId(this.authorityId1);
					auth.setDataIndex(this.index1);
					this.authorityService.updateAuthority(auth);
					map.put("flag", false);
					map.put("msg", "移动失败，数据库问题！");
				}
			}else{
				map.put("flag", false);
				map.put("msg", "移动失败，数据库问题！");
			}
		}else{
			map.put("flag", false);
			map.put("msg", "传递的参数为空");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(this.jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 以下的方法只提供Struts2调用
	 */

	public Authority getAuthority() {
		return authority;
	}
	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
	public int getEditType() {
		return editType;
	}
	public void setEditType(int editType) {
		this.editType = editType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setAuthorityId1(String authorityId1) {
		this.authorityId1 = authorityId1;
	}
	public void setAuthorityId2(String authorityId2) {
		this.authorityId2 = authorityId2;
	}
	public void setIndex1(int index1) {
		this.index1 = index1;
	}
	public void setIndex2(int index2) {
		this.index2 = index2;
	}
	
}

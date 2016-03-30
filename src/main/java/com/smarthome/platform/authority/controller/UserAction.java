package com.smarthome.platform.authority.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.smarthome.core.base.action.BaseAction;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.core.util.PageBean;
import com.smarthome.platform.authority.bean.Admin;
import com.smarthome.platform.authority.service.UserService;

/**
 * 管理员用户管理接口类
 * 
 * @author RM
 * 
 */
@Controller
public class UserAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource
	private UserService userService;
	/**
	 * 当前页 默认第一页
	 */
	private int page = 1;
	/**
	 * 每页数据数目 默认25条
	 */
	private int rows = 25;
	/**
	 * 编辑的类型 0 代表添加 1 代表更新
	 */
	private int editType;
	/**
	 * 如果是添加则是 新添加对象的父亲ID号；如果是更新则是自身的ID号。
	 */
	private String id;
	/**
	 * 定义角色队列
	 */
	private List<String> roles;
	/**
	 * 属性对象，接受前台参数及其往前台返回数据使用
	 */
	private Admin admin;

	/**
	 * 分页获取符合条件的用户 方法
	 * 
	 * @return
	 */
	public String getAll() {
		Map<String, Object> map = new HashMap<String, Object>();
		PageBean pageBean = new PageBean(this.page, this.rows);
		map.put("rows", this.userService.getUserPageByParams(admin, pageBean));
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
	 * 根据用户Id获取该用户所属的用户角色
	 * 
	 * @return
	 */
	public String getRoleById() {
		this.roles = this.userService.getUserRoleListByUserId(this.id);
		this.jsonString = JsonUtils.getJAVABeanJSON(this.roles);
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
	 * 
	 * @return
	 */
	public String beginAddOrUpdate() {

		if (this.editType == 0) {
			return "add_method";
		} else if (this.editType == 1)  {
			// 根据ID号获取对象值
			this.admin = this.userService.getUserId(this.id);

			// System.out.println("当前查询出的对象:"+JsonUtils.getJAVABeanJSON(this.admin));
			return "update_method";
		} else if (this.editType == 100)  {
			//修改密码
			return "changepass_method";
		}
		return null;

	}

	/**
	 * 添加用户
	 * @return
	 */
	public String add() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.admin != null && this.admin.getUserId() != null) {
			//验证邮箱
			String mailReg = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
			//验证电话号码
			//String phoneReg = "^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$";
			//验证手机号码
			String moReg = "^1[3-8]+\\d{9}$";
			if (!Pattern.matches(mailReg, this.admin.getEmail())){
				map.put("flag", false);
				map.put("msg", "添加失败，邮箱无效！");
			}else if (this.admin.getPhone() != null && !this.admin.getPhone().equals("") && !Pattern.matches(moReg, this.admin.getPhone())){
				map.put("flag", false);
				map.put("msg", "添加失败，电话号码无效！");
			}else 			
			// 开启添加
			if (this.userService.addUser(this.admin)) {
				map.put("flag", true);
				map.put("msg", "添加成功!");
			} else {
				map.put("flag", false);
				map.put("msg", "添加失败，请检查数据库连接问题！");
			}
		} else {
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
	 * 编辑用户
	 * @return
	 */
	public String edit() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.admin != null && this.admin.getUserId() != null) {

			// 开启添加
			if (this.userService.eidtUser(this.admin)) {
				map.put("flag", true);
				map.put("msg", "更新成功!");
			} else {
				map.put("flag", false);
				map.put("msg", "更新失败，请检查数据库连接问题！");
			}
		} else {
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
	 * 冻结管理员用户
	 * 
	 * @return
	 */
	public String freeze() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.admin != null && !this.admin.getUserId().equals("")) {
			// 开启删除操作
			if (this.userService.updateUser(admin)) {
				map.put("flag", true);
				map.put("msg", "冻结成功!");
			} else {
				map.put("flag", false);
				map.put("msg", "冻结失败，数据库问题！");
			}
		} else {
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
	 * 取消冻结管理员用户
	 * 
	 * @return
	 */
	public String cancelFreeze() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.admin != null && !this.admin.getUserId().equals("")) {
			// 开启删除操作
			if (this.userService.updateUser(admin)) {
				map.put("flag", true);
				map.put("msg", "解除冻结成功!");
			} else {
				map.put("flag", false);
				map.put("msg", "冻结失败，数据库问题！");
			}
		} else {
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
	 * 删除管理员用户
	 * 
	 * @return
	 */
	public String delete() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id != null && !this.id.equals("")) {
			// 开启删除操作
			if (this.userService.delUser(this.id)) {
				map.put("flag", true);
				map.put("msg", "删除成功!");
			} else {
				map.put("flag", false);
				map.put("msg", "删除失败，数据库问题！");
			}
		} else {
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
	 * 一下方法只提供给Struts2使用
	 */
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

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

}

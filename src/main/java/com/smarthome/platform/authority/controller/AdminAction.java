package com.smarthome.platform.authority.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.smarthome.core.base.action.BaseAction;
import com.smarthome.core.common.AuthorityCommon;
import com.smarthome.core.util.EncryptUtil;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.core.util.MD5;
import com.smarthome.core.util.SmsUtil;
import com.smarthome.core.util.UUIDGenerator;
import com.smarthome.platform.authority.bean.Admin;
import com.smarthome.platform.authority.bean.Sms;
import com.smarthome.platform.authority.service.AdminService;
import com.smarthome.platform.monitor.common.Constant;

/**
 * 管理员操作接口类
 * @author RM
 *
 */
public class AdminAction extends BaseAction{
	
	private static final long serialVersionUID = 1L;
	@Resource
	private AdminService adminService;
	/**
	 * 用户Id参数
	 */
	private String userId;
	/**
	 * 用户密码参数
	 */
	private String passwd;
	/**
	 * 用户重复
	 */
	private String rePasswd;
	/**
	 * 登录成功与否的标记信息
	 */
	private String loginMsg;
	/**
	 * 管理员用户
	 */
	private Admin admin;
	
	/**
	 * 手机号，找回密码是使用。
	 */
	private String phone;
	
	/**
	 * 手机号，找回密码是使用。
	 */
	private String authkey;
	
	/**
	 * 验证码
	 */
	private String validcode;
	/**
     * 用户登录模块
     * @return
     */
	public String login(){
		//先判断当前是否已经登录
		Admin sessionAdmin = (Admin) this.getSession(AuthorityCommon.ADMIN_SESSION);
		try {
			if(sessionAdmin.getUserId()!=null){
				return AuthorityCommon.LOGIN_SUCCESS;
			}else{
				return AuthorityCommon.LOGIN_FAILED;
			}
		} catch (Exception e) {
			if(this.userId!=null&&this.passwd!=null){
				Map<String,Object> map = this.adminService.isLoginSuccess(userId, passwd);
				if((Boolean) map.get("flag")){
					//设置管理员用户Session
					Admin loginAdmin = (Admin) map.get("Admin");
//					this.admin = loginAdmin;
					this.getSession().setAttribute(AuthorityCommon.ADMIN_SESSION, loginAdmin);
					//查询所有管理员的权限（Map对象），并且把它放到Session中
					this.getSession().setAttribute(AuthorityCommon.ADMIN_AUTHORITY_SESSION, this.adminService.getOPAuthorityList(userId));
					System.out.println("CommonAuthoritySession :"+JsonUtils.getJAVABeanJSON(this.getSession(AuthorityCommon.ADMIN_AUTHORITY_SESSION)));
					return AuthorityCommon.LOGIN_SUCCESS;
				}else{
					//登录失败 返回失败原因
					this.loginMsg=(String) map.get("msg");
					return AuthorityCommon.LOGIN_FAILED;
				}
			}else{
				this.loginMsg = "用户名及其密码必填";
				return AuthorityCommon.LOGIN_FAILED;
			}
		}
		
		
	}
	/**
	 * 
	 * @return
	 */
	public String resetpass(){
		if (this.authkey == null || this.authkey.equals(""))
			return null;
		return "reset_pass";
	}
	/**
	 * 用户退出系统模块
	 * @return
	 */
	public String loginOut(){
		//移除Session对象
		this.getSession().removeAttribute(AuthorityCommon.ADMIN_SESSION);
		//移除管理员权限Session
		return AuthorityCommon.LOGIN_OUT;
	}
	/**
	 * 密码修改
	 * @return
	 */
	public String passwdChange(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.passwd!=null&&this.rePasswd!=null&&!this.passwd.trim().equals("")&&!this.rePasswd.trim().equals("")){
			Admin tempAdmin = (Admin) this.getSession(AuthorityCommon.ADMIN_SESSION);
			if(MD5.GetMD5Code(this.passwd).equals(tempAdmin.getPassword())){
				tempAdmin.setPassword(MD5.GetMD5Code(rePasswd));
				if(this.adminService.changePasswd(rePasswd, tempAdmin.getUserId())){
					map.put("flag", true);
					map.put("msg", "修改密码成功");
					//更新用户Session    
					this.getSession().setAttribute(AuthorityCommon.ADMIN_SESSION, tempAdmin);
				}else{
					map.put("flag", false);
					map.put("msg", "修改密码失败");
				}
			}else{
				map.put("flag", false);
				map.put("msg", "原密码输入不正确");
			}
		}else{
			map.put("flag", false);
			map.put("msg", "修改密码：传递参数不完整");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发送手机验证码
	 * 
	 * @return
	 */
	public String sendSms() {
		Map<String, Object> map = new HashMap<String, Object>();
		String moReg = "^1[3-8]+\\d{9}$";
		if (this.phone != null && !this.phone.equals("") &&
				Pattern.matches(moReg, this.phone)) {
			Admin admin = adminService.getUserByPhone(this.phone);
			// 开启删除操作
			if (admin != null) {
				Sms sms = SmsUtil.send(this.phone, admin.getUserId());
				if (sms == null){
					map.put("flag", false);
					map.put("msg", "短信发送失败");
				}else{
					adminService.saveSms(sms);
					map.put("flag", true);
					map.put("msg", "短信成功!");
					map.put("authkey", sms.getAuthkey());
				}
			} else {
				map.put("flag", false);
				map.put("msg", "该手机号未注册");
			}
		} else {
			map.put("flag", false);
			map.put("msg", "请填写正确的手机号码");
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
	 * 校验验证码
	 * @return
	 */
	public String checkValidcode(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.validcode!=null && !this.validcode.trim().equals("")
				&& !this.authkey.trim().equals("") && this.authkey != null && !this.authkey.trim().equals("")){
			Sms sms = this.adminService.getSmsInfo(this.authkey);
			if(sms.getValidcode().equals(this.validcode)){
				map.put("flag", true);
				map.put("msg", "success");
			}else{
				map.put("flag", false);
				map.put("msg", "验证码错误！");
			}
		}else{
			map.put("flag", false);
			map.put("msg", "密码不能为空或者为空字符串！");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 验证手机后，密码重置
	 * @return
	 */
	public String passwdReset(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(this.passwd!=null&&this.rePasswd!=null&&!this.passwd.trim().equals("")&&!this.rePasswd.trim().equals("")
				&& this.authkey != null && !this.authkey.trim().equals("")){
			if(this.passwd.equalsIgnoreCase(rePasswd)){
				Sms sms = this.adminService.getSmsInfo(this.authkey);
				if (sms != null && sms.getUserId() != null){
					Admin tempAdmin = new Admin();
					tempAdmin.setUserId(sms.getUserId());
					tempAdmin.setPassword(MD5.GetMD5Code(this.passwd));
					if(this.adminService.resetPasswd(tempAdmin)){
						map.put("flag", true);
						map.put("msg", "重置密码成功！");	
					}else{
						map.put("flag", false);
						map.put("msg", "重置密码失败！");
					}
					
				}else{
					map.put("flag", false);
					map.put("msg", "请先验证手机再重置密码！");
				}
			}else{
				map.put("flag", false);
				map.put("msg", "两次密码输入不一致！");
			}
		}else{
			map.put("flag", false);
			map.put("msg", "密码不能为空或者为空字符串！");
		}
		this.jsonString = JsonUtils.getJAVABeanJSON(map);
		try {
			this.responseWriter(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * 用户登录模块
     * @return
     */
	public String Auth(){
		Map<String,Object> rmap = new HashMap<String,Object>();
		if(this.userId!=null&&this.passwd!=null){
			try{
				Map<String,Object> map = this.adminService.isLoginSuccess(EncryptUtil.decrypt3Des(userId), EncryptUtil.decrypt3Des(passwd));
				if((Boolean) map.get("flag")){
					rmap.put("flag", true);
					rmap.put("msg", AuthorityCommon.LOGIN_SUCCESS);
					String token = UUIDGenerator.getUUID().replace("-", "");
					Constant.tokenMap.put(this.userId, token);
					rmap.put("token", token);
				}else{
					rmap.put("flag", false);
					rmap.put("msg", AuthorityCommon.LOGIN_FAILED);
				}
			}catch(Exception e){
				logger.error(e.getMessage(), e);
				rmap.put("flag", false);
				rmap.put("msg", AuthorityCommon.LOGIN_FAILED);
			}
		}else{
			rmap.put("flag", false);
			rmap.put("msg", "用户名及其密码必填");
		}
		try {
			this.responseWriter( JsonUtils.getJAVABeanJSON(rmap));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 一下方法只是提供给Struts2自己内部使用
	 */
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public void setRePasswd(String rePasswd) {
		this.rePasswd = rePasswd;
	}
    public String getLoginMsg() {
		return loginMsg;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	public String getUserId() {
		return userId;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setAuthkey(String authkey) {
		this.authkey = authkey;
	}
	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}
	public String getAuthkey() {
		return authkey;
	}
    
}

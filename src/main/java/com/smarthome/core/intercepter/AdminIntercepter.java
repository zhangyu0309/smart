/**
 * 
 */
package com.smarthome.core.intercepter;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.smarthome.core.common.AuthorityCommon;

/**
 * @author RM
 *
 */
public class AdminIntercepter extends AbstractInterceptor {
     
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionContext act = invocation.getInvocationContext();
		Map<String,Object> session = act.getSession();
		Object admin = (Object) session.get(AuthorityCommon.ADMIN_SESSION);
		if(admin!=null){
			return invocation.invoke();
		}else{
			return AuthorityCommon.LOGIN_FAILED;
		}
		
	}

}

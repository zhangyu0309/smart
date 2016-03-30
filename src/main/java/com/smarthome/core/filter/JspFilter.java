package com.smarthome.core.filter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.smarthome.core.common.AuthorityCommon;
import com.smarthome.core.util.JsonUtils;
import com.smarthome.platform.authority.bean.Authority;
/**
 * 主要是用于直接访问JSP页面的时候判断是否有用户的登录
 * @author RM
 *
 */
public class JspFilter implements Filter {

    /**
     * Default constructor. 
     */
    public JspFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		HttpSession session = request.getSession();
        String requestUrl = request.getRequestURI();
        String objectName = request.getContextPath();
        if(requestUrl.contains(".jsp")&&!requestUrl.contains(AuthorityCommon.NO_Filter_JSP)||requestUrl.contains(".html")){
        	
        	Object admin = session.getAttribute(AuthorityCommon.ADMIN_SESSION);
        	if(admin==null){
        		request.setAttribute("loginMsg", "用户没有登录,请登录后再访问！");
        		response.sendRedirect(request.getContextPath()+"/reLogin.jsp");
        	}else{
//        		System.out.println("real Path ："+requestUrl.replace(objectName, ""));
        		String jspAuthString = requestUrl.replace(objectName, "").substring(1);
        		
				Map<String,List<Authority>> allAuthSession = (Map<String, List<Authority>>) session.getAttribute(AuthorityCommon.ADMIN_AUTHORITY_SESSION);
        		if(allAuthSession!=null){
        			List<Authority> tempJspAuthList = allAuthSession.get(jspAuthString);
        			if(tempJspAuthList!=null){
        				session.removeAttribute(AuthorityCommon.JSP_AUHORITY_SESSION);
        				session.setAttribute(AuthorityCommon.JSP_AUHORITY_SESSION, tempJspAuthList);
//        				request.setAttribute("buttonList", tempJspAuthList);
//        				System.out.println(JsonUtils.getJAVABeanJSON(tempJspAuthList));
        			}else{
//        				System.out.println(" no power to visit "+jspAuthString+" page!");
        				//应该跳转到，无权访问的页面
        				session.setAttribute(AuthorityCommon.JSP_AUHORITY_SESSION, new ArrayList<Authority>());
        			}
        		}else{
//        			System.out.println(" Not login ,you can't visit "+jspAuthString+" page!");
        			//应该跳转到登陆页
        			session.setAttribute(AuthorityCommon.JSP_AUHORITY_SESSION, new ArrayList<Authority>());
        		}
        		
        		chain.doFilter(req, res);
        	}
        }else if(requestUrl.contains(AuthorityCommon.NO_Filter_JSP)){
        	Object admin = session.getAttribute(AuthorityCommon.ADMIN_SESSION);
        	if(admin!=null){
        		request.setAttribute("loginMsg", "用户已经登录！");
        		response.sendRedirect(request.getContextPath()+"/page/main.jsp");
        	}else{
        		 chain.doFilter(req, res);
        	}
        }else{
        	 chain.doFilter(req, res);
        }
	}
	/**
	 * 
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

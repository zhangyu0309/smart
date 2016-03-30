<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String contextPath = request.getContextPath()+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>权限管理模块</title>
<jsp:include page="../../common/commonjs.jsp"></jsp:include>
<script type="text/javascript" src="<%=contextPath %>js/admin/authority/authority.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div id="toolbar" style="display: none;">
		<table>
			
			<tr>
				<td>
					<table>
						<tr>
							 <s:iterator value="#session.JSP_AUHORITY_SESSION" id="button"  status="st">
							   <td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'${button.iconCls}',plain:true" onclick="${button.menuUrl}();">${button.authorityName}</a></td>
							   <s:if test="#st.index!=#session.JSP_AUHORITY_SESSION.size-1">
							   <td><div class="datagrid-btn-separator"></div></td>
							   </s:if>
                             </s:iterator>
						</tr>						
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="treeGrid"></table>
	</div>
</body>
</html>
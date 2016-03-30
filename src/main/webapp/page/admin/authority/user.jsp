<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理员用户管理</title>
<jsp:include page="../../common/commonjs.jsp"></jsp:include>
<script type="text/javascript" src="<%=contextPath%>/js/admin/authority/user.js" ></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<form id="searchForm">
						<table>
							<tr style="height:10px;"></tr>
							<tr>
								<td>登录名</td>
								<td><input name="admin.userId" class="easyui-textbox" style="width: 80px;" /></td>
								<td>姓名</td>
								<td><input name=admin.realName class="easyui-textbox" style="width: 80px;" /></td>
								<td>冻结</td>
								<td>
								<select name="admin.enable" class="easyui-combobox" style="width: 80px;" >
								        <option value=""></option>
										<option value="0">冻结</option>
										<option value="1">未冻结</option>
								</select>
								</td>
								<td>创建时间</td>
								<td><input id="beginTime" name="admin.startTime" class="easyui-datebox"  style="width: 120px;" />-<input id="endTime" name="admin.endTime" class="easyui-datebox" style="width: 120px;" /></td>
								<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom',plain:true" onclick="searchBegin();">过滤</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom_out',plain:true" onclick="$('#searchForm input').val('');grid.datagrid('load',{});">重置过滤</a></td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
			
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
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
	
	
</body>
</html>
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
<title>设备管理模块</title>
<jsp:include page="common/commonjs.jsp"></jsp:include>
<script type="text/javascript" src="<%=contextPath %>js/admin/monitor/viewdevice.js"></script>
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
							    <td>设备ID</td>
								<td><input name="device.device_id" class="easyui-textbox" style="width: 80px;" />
								<td>设备名称</td>
								<td><input name=device.device_name class="easyui-textbox" style="width: 80px;" /></td>
								<td>用户ID</td>
								<td><input name="device.user_id" class="easyui-textbox" style="width: 80px;" />
								<td>用户名称</td>
								<td><input name=device.real_name class="easyui-textbox" style="width: 80px;" /></td>
								<td>设备创建时间</td>
								<td><input id="beginTime" name="device.start_time" class="easyui-datebox"  style="width: 120px;" />-<input id="endTime" name="device.end_time" class="easyui-datebox" style="width: 120px;" /></td>
								<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom',plain:true" onclick="searchBegin();">过滤</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom_out',plain:true" onclick="$('#searchForm input').val('');grid.datagrid('load',{});">重置过滤</a></td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="treeGrid"></table>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title>系统</title>
<jsp:include page="common/commonjs.jsp"></jsp:include>
<script type="text/javascript" src="<%=contextPath%>/js/main.js"></script>
<link rel="stylesheet" href="<%=contextPath%>/css/main.css" type="text/css">
</head>
<body id="mainLayout" class="easyui-layout">
	<div data-options="region:'north'" style="height: 80px; overflow: hidden;">
		<div id="topbar" class="top-bar"  style="background:url('<%=contextPath%>/img/logo/title-bg.jpg');">
			<div class="top-bar-left">
				<h1 style="margin-left: 10px; margin-top: 10px;">
				</h1>
			</div>
            <div id="buttonbar" style="float:right;margin-right:5px;margin-top: 20px;">
                    <a id="user" class="easyui-splitbutton" data-options="menu:'#userMenu', iconCls: 'ext-icon-user'">${ADMIN_SESSION.realName}</a>
            </div>
		</div>
		 <div id="toolbar" class="panel-header panel-header-noborder top-toolbar">
         </div>
	</div>
	<div data-options="region:'west',href:'',split:true" title="导航"
		style="width: 200px; padding: 10px;">
		<ul id="mainMenu"></ul>
	</div>
	<div data-options="region:'center'" style="overflow: hidden;">
		<div id="mainTabs">
			<div title="系统桌面" data-options="iconCls:'ext-icon-computer'">
				<iframe src="<%=contextPath%>/page/monitor/welcomes.jsp" allowTransparency="true"
					style="border: 0; width: 100%; height: 99%;" frameBorder="0" name="self"></iframe>
			</div>
		</div>
	</div>
	<div
		data-options="region:'south',href:'<%=contextPath%>/page/south.jsp',border:false"
		style="background:url('<%=contextPath%>/img/logo/titl.jpg');height: 30px; overflow: hidden;" ></div>
	 <div id="userMenu" style="height:50px;">
        <div id="passwdChange" data-options="iconCls:'ext-icon-user_edit'">修改密码</div>
        <div id="loginOut" data-options="iconCls:'ext-icon-user_go'">退出系统</div>
     </div>
</body>
</html>
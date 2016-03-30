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
<title>安防</title>
<jsp:include page="../../common/commonjs.jsp"></jsp:include>
<script type="text/javascript" src="<%=contextPath%>/js/admin/monitor/security.js" ></script>
</head>

<style>
a{text-decoration:none;display:block; color:black;padding-top:5px;padding-left:70px}
div{font-size:25px;vertical-align:top;}
.panel{width:600px;height:150px;border:1px solid #000000}
.panel_button{width:600px;height:50px;}
.formatline{margin-left: 200px;margin-top: -44px;}
.panel1{width:130px;height:130px;margin-top: 15px;margin-left: 50px;position:absolute;}
.panel3{width:400px;height:30px;margin-top: 50px;margin-left: 200px;position:absolute;}
.format{margin-top: -20px;margin-left: 30px;position:absolute}
.bf{background:url('../../../img/app/protec_btn_do_bg.png') no-repeat;display:block;width:170px;height:40px;}
.cf{background:url('../../../img/app/protec_btn_disable_bg.png') no-repeat;display:block;width:170px;height:40px;}
</style>

</head>
<body>
<div class="panel_button">
<div><a href="javascript:void(0);" class="bf">布防</a></div>
<div class="formatline"><a href="javascript:void(0);" class="cf">撤防</a></div>
</div>
	<div class="panel">
    	<div class="panel1"><img width="100px" height="100px" src="../../../img/app/security_magnetic.png" 
    	onmouseover="this.src='../../../img/app/security_magnetic_focus.png'" 
    	onmouseout="this.src='../../../img/app/security_magnetic.png'"></img></br>
    	&nbsp;门&nbsp;&nbsp;磁</div>
    	<div class="panel3" ><img width="25px" height="25px" src="../../../img/app/icon_lock_on.png"></img>当前状态：打开</div>
    	</div>
    </div>
</body>
</html>

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
<title>灯光</title>
<jsp:include page="../../common/commonjs.jsp"></jsp:include>
<link rel="stylesheet" href="../../../css/bootstrap/bootstrap-switch.css" type="text/css">
<script type="text/javascript" src="<%=contextPath%>/js/admin/monitor/lighting.js" ></script>
</head>

<style>
body{font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;}
		.bs-docs-header h1{
			font-size: 1.5em;
			font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;
		}
		.bs-docs-header h1 span{
			display: block;
			font-size: 60%;
			font-weight: 400;
			padding: 0.8em 0 0.5em 0;
			color: #c3c8cd;
		}
.htmleaf-icon{color: #fff;}
div{font-size:25px;vertical-align:top;}
.panel2{font-size:15px;vertical-align:top;}
.panel{width:600px;height:300px;border:1px solid #000000}
.panel1{width:130px;height:130px;margin-top: 10px;margin-left: 50px;position:absolute;}
.panel2{width:400px;height:30px;margin-top: 20px;margin-left: 200px;position:absolute;}
.panel3{width:400px;height:30px;margin-top: 70px;margin-left: 200px;position:absolute;}
.panel4{width:400px;height:30px;margin-top: 120px;margin-left: 200px;position:absolute;display:inline-block;}
.panel5{width:400px;height:30px;margin-top: 160px;margin-left: 200px;position:absolute;display:inline-block;}
.colorspan{width:400px;}
.textbox {height: 22px;}
</style>

</head>
<body>
	<div class="panel">
    	<div class="panel1"><img width="100px" height="100px" src="../../../img/app/energy_wallplus.png" 
    	onmouseover="this.src='../../../img/app/energy_wallplus_focus.png'" 
    	onmouseout="this.src='../../../img/app/energy_wallplus.png'"></img></br>墙式插座</div>
    	<div class="panel2">插口一</br>电能消耗：0Kwh&nbsp;&nbsp;功率：10kw</div>
    	<div class="panel3" ><img width="25px" height="25px" src="../../../img/app/icon_switch.png"></img>
			<input id="switch-state" type="checkbox">关闭
		</div>
    	<div class="panel4"><img width="20px" height="20px" src="../../../img/app/icon_colors.png"></img>
    	<span class="colorspan" style="background-color:#D6F6FF;">&nbsp;&nbsp;&nbsp;
    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
    	</div>
    	<div class="panel5"><img width="20px" height="20px" src="../../../img/app/icon_clock.png"></img>
    	<input class="easyui-validatebox textbox" type="text"
						name="userInfo.user_name" value="8:30开启"
						data-options="required:true"></input>
						<input class="easyui-validatebox textbox" type="text"
						name="userInfo.user_name" value="19:55关闭"
						data-options="required:true"></input>
    	</div>
    </div>
    <script type="text/javascript" src="<%=contextPath%>/js/jquery.min.js" ></script>
    <script type="text/javascript" src="<%=contextPath%>/js/bootstrap/bootstrap.min.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/js/bootstrap/highlight.js" ></script>    
<script type="text/javascript" src="<%=contextPath%>/js/bootstrap/bootstrap-switch.js" ></script>    
    <script type="text/javascript" src="<%=contextPath%>/js/external/switchmain.js" ></script>




</body>
</html>

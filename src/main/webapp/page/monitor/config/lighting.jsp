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
<script type="text/javascript" src="<%=contextPath%>/js/admin/monitor/lighting.js" ></script>
</head>

<style>
div{font-size:25px;vertical-align:top;}
.panel{width:600px;height:300px;border:1px solid #000000}
.panel1{width:130px;height:130px;margin-top: 10px;margin-left: 50px;position:absolute;}
.panel4{width:400px;height:30px;margin-top: 20px;margin-left: 200px;position:absolute;display:inline-block;}
.panel5{width:400px;height:30px;margin-top: 120px;margin-left: 200px;position:absolute;display:inline-block;}
.colorspan{width:400px;}
.format{margin-top: -20px;margin-left: 30px;position:absolute}
</style>

</head>
<body>
	<div class="panel">
    	<div class="panel1"><img width="100px" height="100px" src="../../../img/app/light_navig_icon_led.png" 
    	onmouseover="this.src='../../../img/app/light_navig_icon_led_focus.png'" 
    	onmouseout="this.src='../../../img/app/light_navig_icon_led.png'"></img></br>&nbsp;LED&nbsp;灯</div>
    	<div class="panel4"><img width="20px" height="20px" src="../../../img/app/icon_colors.png"></img>
    	<span class="colorspan" style="background-color:#D6F6FF;">&nbsp;&nbsp;&nbsp;
    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
    	</div>
    	<div class="panel5"><img width="20px" height="20px" src="../../../img/app/icon_brightness.png"></img>
    	<div class="format"><input class="easyui-slider" style="width:300px" data-options="
				showTip:true,
				rule: [0,'|',25,'|',50,'|',75,'|',100]
			">
		</div>
    	</div>
    </div>

</body>
</html>

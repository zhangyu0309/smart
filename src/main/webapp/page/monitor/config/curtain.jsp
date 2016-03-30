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
<script type="text/javascript"
	src="<%=contextPath%>/js/admin/monitor/curtain.js"></script>
</head>

<style>
div {
	font-size: 25px;
	vertical-align: top;
}

td {
	text-align: center;
}

.panel {
	width: 600px;
	height: 250px;
	border: 1px solid #000000
}

.secondpanel {
	width: 600px;
	height: 500px;
	margin-top: 10px;
	text-align:center;
	border: 1px solid #000000
}

.panel1 {
	width: 130px;
	height: 130px;
	margin-top: 10px;
	margin-left: 50px;
	position: absolute;
}

.panel4 {
	width: 400px;
	height: 30px;
	margin-top: 20px;
	margin-left: 200px;
	position: absolute;
	display: inline-block;
	font-size: 15px;
}

.panel5 {
	width: 400px;
	height: 30px;
	margin-top: 120px;
	margin-left: 200px;
	position: absolute;
	display: inline-block;
}
</style>

</head>
<body>
	<div class="panel">
		<div class="panel1">
			<img width="100px" height="100px"
				src="../../../img/app/light_navig_icon_blind.png"
				onmouseover="this.src='../../../img/app/light_navig_icon_blind_focus.png'"
				onmouseout="this.src='../../../img/app/light_navig_icon_blind.png'"></img></br>&nbsp;窗帘
		</div>
		<div class="panel4">
			<table>
				<tr>
					<td>打开</td>
					<td></td>
					<td>关闭</td>
				</tr>
				<tr>
					<td><img width="50px" height="50px"
						src="../../../img/app/blind_switch_on.png"></img></td>
					<td style="width:100px"></td>
					<td><img width="50px" height="50px"
						src="../../../img/app/blind_switch_off.png"></img></td>
				</tr>


			</table>
		</div>
		<div class="panel5">
			<input class="easyui-slider" style="width:300px"
				data-options="
				showTip:true,
				rule: [0,'|',25,'|',50,'|',75,'|',100]
			">
		</div>
	</div>
	<div class="secondpanel">
			<table style="width:600px;">
				<tr>
				<td style="width:100px;"></td>
					<td><img width="400px" height="400px"
						src="../../../img/app/curtain.png"></img></td>
						<td style="width:100px;"></td>
				</tr>
				<tr><td style="width:100px;"></td>
					<td>
					<input id="curslider" class="easyui-slider" style="width:400px">
					</td>
					<td style="width:100px;"></td>
				</tr>
				<tr><td style="width:100px;"></td>
					<td>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-bullet_green',plain:true" onclick="open1();">开</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-bullet_red',plain:true" onclick="stop1();">停</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-bullet_delete',plain:true" onclick="close1();">关</a>
					</td>
					<td style="width:100px;"></td>
				</tr>
			</table>
	</div>
</body>
</html>

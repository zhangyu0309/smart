<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>舒适</title>
<jsp:include page="../../common/commonjs.jsp"></jsp:include>
<script type="text/javascript" src="<%=contextPath%>/js/admin/monitor/comfort.js" ></script>
</head>

<style>
div{font-size:25px;vertical-align:top;}
.panel{width:650px;height:260px;border:1px solid #000000}
.panel1{width:130px;height:130px;margin-top: 70px;margin-left: 50px;position:absolute;}
.panel2{width:400px;height:30px;margin-top: 20px;margin-left: 200px;position:absolute;}
.panel3{width:400px;height:30px;margin-top: 100px;margin-left: 200px;position:absolute;}
.panel4{width:400px;height:30px;margin-top: 180px;margin-left: 200px;position:absolute;display:inline-block;}
.format{margin-top: -20px;margin-left: 30px;position:absolute}
</style>

</head>
<body>
	<div class="panel">
    	<div class="panel1"><img width="100px" height="100px" src="../../../img/app/home_entry_icon_comfort.png" 
    	onmouseover="this.src='../../../img/app/home_entry_icon_comfort_focus.png'" 
    	onmouseout="this.src='../../../img/app/home_entry_icon_comfort.png'"></img></br>
    	温湿度计</div>
    	<div class="panel2"><img width="25px" height="25px" src="../../../img/app/icon_temperature.png"></img>当前温度：0.0℃</div>
    	<div class="panel3" ><img width="25px" height="25px" src="../../../img/app/icon_humidity.png"></img>相对湿度：0%</div>
    	<div class="panel4"><img width="20px" height="20px" src="../../../img/app/icon_battery.png"></img>
    	<div class="format"><input class="easyui-slider" style="width:300px" data-options="
				showTip:true,
				rule: [0,'|',25,'|',50,'|',75,'|',100]
			">
		</div></div>
    </div>
    
    <div class="op_weather4_twoicon_container_div" style="width:650px;margin-top:10px;">

		<div class="op_weather4_twoicon" style="height: 280px;">
			<a weath-eff="[]" weath-bg="cloudy" target="_blank"
				class="op_weather4_twoicon_today OP_LOG_LINK">
				<p class="op_weather4_twoicon_date" id="city_time"></p>
				<div id="w1" class="op_weather4_twoicon_icon"></div>

				<p class="op_weather4_twoicon_temp" id="temp1">
				</p>
				<p class="op_weather4_twoicon_weath" id="weather1"></p>
				<p class="op_weather4_twoicon_wind" id="wind1"></p>

				
			</a> <a weath-eff="{&quot;halo&quot;:1}" weath-bg="daytime"
				target="_blank" style="left: 188px"
				class="op_weather4_twoicon_day OP_LOG_LINK">


				<div class="op_weather4_twoicon_hover"></div>
				<div class="op_weather4_twoicon_split"></div>
				<p class="op_weather4_twoicon_date">明天</p>
<p class="op_weather4_twoicon_date_day"></p>
				<div id="w2" class="op_weather4_twoicon_icon"></div>

				<p class="op_weather4_twoicon_temp" id="temp2"></p>
				<p class="op_weather4_twoicon_weath" id="weather2"></p>
				<p class="op_weather4_twoicon_wind" id="wind2"></p>

			</a> <a weath-eff="{&quot;halo&quot;:1}" weath-bg="daytime"
				style="left: 276px" class="op_weather4_twoicon_day OP_LOG_LINK">


				<div class="op_weather4_twoicon_hover"></div>
				<div class="op_weather4_twoicon_split"></div>
				<p class="op_weather4_twoicon_date">后天</p>
<p class="op_weather4_twoicon_date_day"></p>
				<div id="w3" class="op_weather4_twoicon_icon"></div>

				<p class="op_weather4_twoicon_temp" id="temp3"></p>
				<p class="op_weather4_twoicon_weath" id="weather3"></p>
				<p class="op_weather4_twoicon_wind" id="wind3"></p>

			</a>

			<!--阴天-->
			<div bg-name="cloudy" class="op_weather4_twoicon_bg" style="display:none;background-image:-webkit-linear-gradient(top,#485663,#a1b8ca);background-image:-moz-linear-gradient(top,#485663,#a1b8ca);background-image:-o-linear-gradient(top,#485663,#a1b8ca);background-image:-ms-linear-gradient(top,#485663,#a1b8ca);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,StartColorStr=#485663,EndColorStr=#a1b8ca); z-index: 0.01; opacity: 1; height: 280px;">
                </div>

			
			<!--晴天-->
			<div bg-name="daytime" class="op_weather4_twoicon_bg" style="display:block;background-image:-webkit-linear-gradient(top,#0d68bc,#72ade0);background-image:-moz-linear-gradient(top,#0d68bc,#72ade0);background-image:-o-linear-gradient(top,#0d68bc,#72ade0);background-image:-ms-linear-gradient(top,#0d68bc,#72ade0);filter:progid:DXImageTransform.Microsoft.gradient(GradientType=0,StartColorStr=#0d68bc,EndColorStr=#72ade0); z-index: 2; opacity: 1; height: 280px;">
                </div>

		</div>

	</div>
</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>历史数据详情页</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<style type="text/css">
html {
	height: 100%
}

body {
	height: 100%;
	margin: 0px;
	padding: 0px
}

#controller {
	width: 100%;
	border-bottom: 3px outset;
	height: 30px;
	filter: alpha(Opacity =       100);
	-moz-opacity: 1;
	opacity: 1;
	z-index: 10000;
	background-color: lightblue;
}

#container {
	height: 100%
}
</style>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2&ak=Rz0geBnQNHhLBzOYwY7budkD"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/library/LuShu/1.2/src/LuShu_min.js"></script>
<jsp:include page="../../common/commonjs.jsp"></jsp:include>
<script type="text/javascript">
	var graphBean;
	var submitButton;
	var resultTemp;
	//所有点的坐标
	var points = [];
	var whole = [];
	var map; //百度地图对象
	var car; //汽车图标
	var label; //信息标签
	var centerPoint;

	var timer; //定时器
	var index = 0; //记录播放到第几个point

	var followChk; //几个控制按钮
	$(function() {
		followChk = document.getElementById("follow");

		//获取对象
		var startTime = $("#beginTime").datetimebox();
		var endTime = $("#endTime").datetimebox();
		startTime.datetimebox('setValue', TraceConfig
				.getTimeString(TraceConfig.MinuteType));
		endTime.datetimebox('setValue', TraceConfig.getTimeString(0));

		//查找数据
		submitButton = function() {
			var s = startTime.datetimebox('getValue');
			var e = endTime.datetimebox('getValue');
			$.ajax({
				type : "post",
				//async : false,
				url : sy.contextPath + "getPositionDataHost.do?deviceid="
						+ "${deviceid}" + "&stime=" + s + "&etime=" + e, //发送请求地址,
				success : function(r) {
					resultTemp = r;
					showTrace();
				}
			});
		};

		//地图展示
		var showTrace = function() {
			if (resultTemp && resultTemp != null) {
				for ( var str in resultTemp) {
					$.each(eval('(' + resultTemp[str] + ')'), function(i, n) {
						whole.push(n);
						points.push(new BMap.Point(n.longitude / 100.00,
								n.latitude / 100.00));
					});
				}
				if (points && points.length > 0) {
					console.log(points);
					//初始化地图,选取第一个点为起始点
					map = new BMap.Map("container");
					map.centerAndZoom(points[0], 15);
					map.enableScrollWheelZoom();
					map.addControl(new BMap.NavigationControl());
					map.addControl(new BMap.ScaleControl());
					map.addControl(new BMap.OverviewMapControl({
						isOpen : true
					}));
					$.each(whole, function(i, n) {
						if (i == 0) {
							// 开始点
							addMarkerFun(points[0], 1, 0, 'start');
						} else if (i == points.length - 1) {
							// 终点
							addMarkerFun(points[i], 1, 1, 'end');
						} else {
							addMarkerFun(points[i], 2, 2, 'path',
									getContent(n));
						}
					});

					//通过DrivingRoute获取一条路线的point
					var driving = new BMap.DrivingRoute(map);
					driving.search(points);
					driving.setSearchCompleteCallback(function() {
						console.log(driving);
						if (driving.getResults()){
						//得到路线上的所有point
							points = driving.getResults().getPlan(0)
									.getRoute(0).getPath();
						}
						//画面移动到起点和终点的中间
						centerPoint = new BMap.Point(
								(points[0].lng + points[points.length - 1].lng) / 2,
								(points[0].lat + points[points.length - 1].lat) / 2);
						map.panTo(centerPoint);
						//连接所有点
						map.addOverlay(new BMap.Polyline(points, {
							strokeColor : "blue",
							strokeWeight : 5,
							strokeOpacity : 0
						}));
						//显示小车子
						/*label = new BMap.Label("", {
							offset : new BMap.Size(-20, -20)
						});
						car = new BMap.Marker(points[0]);
						car.setLabel(label);
						map.addOverlay(car);
						play();*/
					});
				} else {
					console.log('无位置数据');
					//parent.$.messager.alert('提示', '指定时间段内无位置数据', 'info');
				}
			} else {
				console.log('no result');
			}
		};

		submitButton();
	});

	var play = function() {
		var point = points[index];
		/*	if (index > 0) {
				map.addOverlay(new BMap.Polyline([ points[index - 1], point ], {
					strokeColor : "red",
					strokeWeight : 1,
					strokeOpacity : 1
				}));
			}*/
		label.setContent("经度: " + point.lng + "<br>纬度: " + point.lat);
		car.setPosition(point);
		index++;
		if (followChk.checked) {
			map.panTo(point);
		}
		if (index < points.length) {
			//
		} else {
			//map.panTo(point);
			index = 0;
		}
		timer = window.setTimeout("play(" + index + ")", 200);
	};

	var addMarkerFun = function(point, imgType, index, title, content) {
		var url;
		var width;
		var height;
		var myIcon;
		// imgType:1的场合，为起点和终点的图；2的场合为车的图形
		if (imgType == 1) {
			url = "http://developer.baidu.com/map/jsdemo/img/dest_markers.png";
			width = 42;
			height = 34;
			myIcon = new BMap.Icon(url, new BMap.Size(width, height), {
				offset : new BMap.Size(14, 32),
				imageOffset : new BMap.Size(0, 0 - index * height)
			});
		} else {
			url = "http://developer.baidu.com/map/jsdemo/img/trans_icons.png";
			width = 22;
			height = 25;
			var d = 25;
			var cha = 0;
			var jia = 0;
			if (index == 2) {
				d = 21;
				cha = 5;
				jia = 1;
			}
			myIcon = new BMap.Icon(url, new BMap.Size(width, d), {
				offset : new BMap.Size(10, (11 + jia)),
				imageOffset : new BMap.Size(0, 0 - index * height - cha)
			});
		}

		var marker = new BMap.Marker(point, {
			icon : myIcon
		});
		if (title != null && title != "") {
			marker.setTitle(title);
		}
		if (content != null && content != "") {
			var label = new BMap.Label("", {
				offset : new BMap.Size(-20, -20)
			});
			label.setContent(content);
			marker.setLabel(label);
		}
		// 起点和终点放在最上面
		if (imgType == 1) {
			marker.setTop(true);
		}
		map.addOverlay(marker);
	};

	var getContent = function(record) {//生成弹出窗口的文字  
	console.log(record);
		var html = [];
		html.push('<font>');
		html.push('<span style="font-weight:bold;">速度:</span>'
				+ record.speed + '<br/>');
		html.push('<span style="font-weight:bold;">定位时间:</span>'
				+ record.time + '<br/>');
		html.push('</font>');
		return html.join("");
	};
</script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
	<!-- 查询条件 -->
	<div id="search_div" data-options="region:'north', border:true"
		title="" style="height: 50px;">
		<div style="height: 5px;"></div>
		<table style="font-size: 12px">
			<tr height="20px">
				<td id="td_time"></td>
				<td><input id="beginTime" name="admin.startTime"
					class="easyui-datetimebox" style="width: 180px;" />-<input
					id="endTime" name="admin.endTime" class="easyui-datetimebox"
					style="width: 180px;" />
				</td>
				<td><a id="but_view" href="javascript:void(0);"
					class="easyui-linkbutton"
					data-options="iconCls:'ext-icon-zoom',plain:true"
					onclick="submitButton();">查询</a>
				</td>
				<td><input id="follow" type="checkbox"><span
					style="font-size:12px;color:white;">画面跟随</span></input></td>
			</tr>
		</table>
	</div>
	<!-- 地图呈现 -->
	<div id="container"></div>
</body>
</html>

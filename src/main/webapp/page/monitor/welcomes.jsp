<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>首页</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<!-- 引入公共的JS及其CSS文件 -->
<jsp:include page="../common/commonjs.jsp"></jsp:include>
<script type="text/javascript" src="<%=basePath%>js/jquery.portal.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/portal.css">
<style type="text/css">
  
</style>
</head>

<body class="easyui-layout">
	<div region="center" border="false" >
        <div id="ww" style="position: relative; width: 100%; height: 100%;">
            <div style="width:100%;">
            </div>
        </div>
    </div>
    <script type="text/javascript">
    var currentclick = '';
    var currentname = '';
        options={
                border:false,
                fit:true,
                width:'100%',          
                height:'100%'  
            };
        $('#ww').portal(options);
        
        var p1 = $("<div id='hostStatus' ><table id='homeHostGroup' data-options='fit:true,border:false'></table></div>").appendTo('body');
        p1.panel({
            title: '数据总览',
            iconCls:'ext-icon-computer',
            height:400,
            maximizable:false,
            minimizable:false,
            doSize:false,
            fit:false,
            closable:false,
            collapsible: true
        });
        /*   var p2 = $("<div id='portalMemory' ></div>").appendTo('body');
        p2.panel({
            title: '',
            iconCls:'ext-icon-server',
            height:10,
            maximizable:false,
            minimizable:false,
            doSize:false,
            fit:false,
            closable:false,
            collapsible: true
        });
        
       var p3 = $("<div id='p3' ></div>").appendTo('body');
        p3.panel({
            title: '土壤监测',
            iconCls:'ext-icon-server',
            height:300,
            maximizable:false,
            minimizable:false,
            doSize:false,
            fit:false,
            closable:false,
            collapsible: true
        });
        */
         $('#ww').portal('add', {
            panel: p1,
            columnIndex: 0
        });
        /*  $('#ww').portal('add', {
            panel: p2,
            columnIndex: 0
        });
       $('#ww').portal('add', {
            panel: p3,
            columnIndex: 0
        });
        */
      /*  $.ajax({
          type:"post",
          async:true,
          url:sy.contextPath + "page/homeHostGroup.jsp",
          success:function(r){
            if(r){
               $("#hostStatus").html(r);
            }
          }
        });
        $.ajax({
          type:"post",
          async:true,
          url:sy.contextPath + "page/dash.jsp",
          success:function(r){
            if(r){
               $("#portalMemory").html(r);
            }
          }
        });
        */
        grid = $('#homeHostGroup')
			.datagrid(
					{
						title : '',
						url : sy.contextPath + '/getAllSensorDataDevice.do',
						singleSelect : true,
						idField : 'device_id',
						fit : true,
						pagination : true,
						pageSize : 10,
						pageList : [ 10, 20 ,50 ],
						columns : [ [  {
							field : 'device_name',
							title : '设备',
							width : '16%',
							formatter : function(value,data) {
								if (data.online == '1') {
									return value + '<font color="green">[在线]</font>';
								} else {
									return value + '<font color="red">[离线]</font>';
								} 
							}
						} ,{
							field : 'temp1',
							title : '数据1',
							width : '13%'
						} , {
							field : 'wet1',
							title : '数据2',
							width : '13%'
						} , {
							field : 'temp2',
							title : '数据3',
							width : '13%'
						} , {
							field : 'wet2',
							title : '数据4',
							width : '13%',
							formatter : function(value,data) {
								return "71";
							}
						} , {
							field : 'light',
							title : '数据5',
							width : '13%'
						}  , {
							field : 'data_time',
							title : '数据时间',
							width : '14%'
						}   ] ],
						onClickRow : function(rowIndex, rowData){  //点击事件
							//currentclick = rowData.device_id;
							//currentname = rowData.device_name;
							// p3.panel({
            				//	title: currentname + '-土壤监测'
       						// });
         					//p2.panel({
            				//	title: currentname + '-空气监测'
        					//});
							//refreshData(rowData.device_id);
						}
					});
    var refreshData = function(device_id){
		console.log(device_id);
		if (device_id && device_id != ''){
		$.ajax({
          type:"post",
          async:false,
          url:sy.contextPath + "getLatestDataDevice.do?device_id=" + device_id,
          success:function(r){
            if(r){
              console.log(r);
              	//dashFrame.window.refresh(r.temp1, r.wet1, r.light);
				//dash1Frame.window.refresh(r.temp2, '71');
            }
          }
        });
		}
	};
	//$("#hostStatus").html('<iframe src=\"'+ sy.contextPath + 'page/homeHostGroup.jsp\" allowTransparency=\"true\" style=\"border: 0; width: 100%; height: 99%;\" frameBorder=\"0\" name=\"self\"></iframe>');
	//$("#portalMemory").html('<nobr><iframe name=\"dashFrame\" scrolling=\"no\" src=\"'+ sy.contextPath + 'page/dash.jsp\" allowTransparency=\"true\" style=\"border: 0; width: 50%; height: 99%;zoom:50%;\" frameBorder=\"0\" name=\"self\"></iframe>' + 
	//'<iframe name=\"dash1Frame\" scrolling=\"no\" src=\"'+ sy.contextPath + '/page/dash1.jsp\" allowTransparency=\"true\" style=\"border: 0; width: 50%; height: 99%;zoom:50%;\" frameBorder=\"0\" name=\"self\"></iframe></nobr>');
     // $("#p3").html('<iframe name=\"dash1Frame\" src=\"'+ sy.contextPath + '/page/dash1.jsp\" allowTransparency=\"true\" style=\"border: 0; width: 100%; height: 99%;\" frameBorder=\"0\" name=\"self\"></iframe>');
    
	setInterval(function() {
		grid.datagrid('reload');
		if (currentclick && currentclick != ''){
		//p3.panel({
        //    title: currentname + '-土壤监测'
       // });
         p2.panel({
            title: currentname + '-数据监测'
        });
			refreshData(rowData.device_id);
		}
	}, 60000);
    </script>
</body>
</html>

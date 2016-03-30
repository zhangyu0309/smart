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

<title>监控组合预览</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
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
            <div style="width:50%;">
            </div>
             <div style="width:50%;">
            </div>
        </div>
    </div>
    <script type="text/javascript">
        options={
                border:false,
                fit:true,
                width:'100%',          
                height:'100%'  
            };
        $('#ww').portal(options);
        
    </script>
</body>
</html>

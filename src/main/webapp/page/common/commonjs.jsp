<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- 获取服务器的绝对地址 -->
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String contextPath = request.getContextPath()+"/";
%>
<!-- 设置页面的公共的地址 -->
<script type="text/javascript">
var sy = sy || {};
sy.contextPath = '<%=contextPath%>';
sy.basePath = '<%=basePath%>';
sy.imgTip = '<%=contextPath%>/style/images/pixel_0.gif';  //0像素的背景，一般用于占位（主要是给请求图标请求错误后的默认图标）
</script>
<!-- 添加Jquery支持 -->
<script type="text/javascript" src="<%=contextPath%>js/jquery-2.0.3.js" charset="utf-8"></script>
<!-- 添加Jquery扩展 -->
<script type="text/javascript" src="<%=contextPath%>js/external/externalJquery.js" charset="utf-8"></script>
<!-- 添加EasyUI支持 -->
<script type="text/javascript" src="<%=contextPath%>js/easyui/jquery-easyui-1.4.3/jquery.easyui.min.js"></script>
<!-- 添加EasyUI的语言支持（简体） -->
<script type="text/javascript" src="<%=contextPath%>js/easyui/jquery-easyui-1.4.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=contextPath%>js/jquery.portal.js"></script>
<!-- 添加EasyUI扩展 -->
<script type="text/javascript" src="<%=contextPath%>js/external/externalEasyUI.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="<%=contextPath%>js/external/treegrid-dnd.js" type="text/javascript" charset="utf-8"></script>
<!-- 添加EasyUI的样式 -->
<link id="easyuiTheme" rel="stylesheet" href="<%=contextPath%>js/easyui/jquery-easyui-1.4.3/themes/bootstrap/easyui.css" type="text/css">
<!-- 添加扩展的EasyUI图标的样式 -->
<link rel="stylesheet" href="<%=contextPath%>css/external/externalIcon.css" type="text/css">
<!-- 添加自定义Table的样式 -->
<link rel="stylesheet" href="<%=contextPath%>css/external/externalCss.css" type="text/css">
<!-- 添加自定义辅助方法 -->
<script type="text/javascript" src="<%=contextPath%>js/external/externalJavascript.js" type="text/javascript" charset="utf-8"></script>

<link rel="stylesheet" href="<%=contextPath%>css/weather.css" type="text/css">
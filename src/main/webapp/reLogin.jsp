<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<jsp:include page="page/common/commonjs.jsp"></jsp:include>

<script type="text/javascript">
	parent.$.messager.alert('提示','用户已过期，请重新登录', 'info', function() {
    	top.window.location.href='login.jsp';
	});
</script>

</head>

<body>
</body>
</html>
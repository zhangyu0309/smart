<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<meta name="description" content="">
<meta name="author" content="ThemeBucket">
<title>登录</title>
<!-- bootstrap样式 -->
<link href="css/bootstrap/style.css" rel="stylesheet">
<link href="css/bootstrap/style-responsive.css" rel="stylesheet">
<!-- bootstrap JS  -->
<script src="js/jquery-2.0.3.js"></script>
<script src="js/bootstrap/bootstrap.min.js"></script>
<script src="js/bootstrap/modernizr.min.js"></script>
<!-- IE9 以下浏览器的兼容JS -->
<script src="js/bootstrap/html5shiv.js"></script>
<script src="js/bootstrap/respond.min.js"></script>

<script type="text/javascript">
var authkey = "${authkey}";
var submitreset = function(){
	var pass = $('#pass').val();
	var repass = $('#repass').val();
	if (pass == repass){
		$.ajax({
			type : "post",
			//async : false,
			url : "passwdResetAdmin.do?authkey=" + authkey + '&rePasswd=' + repass + '&passwd=' + pass, //发送请求地址,
			success : function(r) {
				if (r.flag){
					$('#myModal').modal('hide');
					window.location.href = 'login.jsp';
				}else{
					$('#tips1').html(r.msg);
				}
			}
		});
	}else{
		alert('密码输入不一致');
	}
};
</script>
</head>
<body class="login-body">
	
<div class="container">
    <form class="form-signin">
        <div class="form-signin-heading text-center">
            <h1 class="sign-title">重置密码</h1>
        </div>
        <div class="login-wrap">
            <p> 输入您的新密码:</p>
            <input type="password" placeholder="新密码" id="pass" class="form-control">
            <input type="password" placeholder="重复密码" id="repass" class="form-control">
            <label class="checkbox"> <span id="tips1" class="pull-left"></span> <span
			class="pull-right"></span> </label> 
            <button type="button" class="btn btn-lg btn-login btn-block" onclick="submitreset();">提&nbsp;交 </button>
            <div class="registration">想起密码来了？
            <a href="login.jsp" class="">直接登录</a>
            </div>

        </div>

    </form>

</div>
</body>
</html>

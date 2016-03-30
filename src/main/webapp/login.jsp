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

<script type="text/javascript" src="js/login.js"></script>
</head>
<body class="login-body">
	<div class="container">
		<form class="form-signin" method="post" action="loginAdmin.do">
			<div class="form-signin-heading text-center">
				<h1 class="sign-title">SMARTHOME后台管理系统</h1>
				 <img height="128px" width="128px" src="img/login/login-logo.png" alt="" />
			</div>
			<div class="login-wrap">
				<input type="text" name="userId" class="form-control"
					value="${userId }" placeholder="账号" autofocus> <input
					type="password" name="passwd" class="form-control" placeholder="密码">
				<button class="btn btn-lg btn-login btn-block" type="submit">登
					录</button>
				<div class="registration">
					<!--   Not a member yet?
                <a class="" href="registration.html">
                    Signup
                </a>-->
				</div>
				<label class="checkbox"> <!--   <input type="checkbox" value="remember-me"> Remember me-->
					<span class="pull-right"> <a data-toggle="modal"
						href="#myModal">找回密码</a> </span> </label>
				<div class="registration">
					<font color="red">${loginMsg}</font>
				</div>
			</div>
		</form>
	</div>
	<!-- Modal -->
	<div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog"
		tabindex="-1" id="myModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">找回密码</h4>
				</div>
				<div class="modal-body">
					<p>输入您的手机号来找回密码</p>
					<input type="text" name="phone" placeholder="手机号码"
						autocomplete="off" class="form-control placeholder-no-fix">
					<label class="checkbox"> <span id="tips" class="pull-left"></span> <span
						class="pull-right"> <a class="" href="javascript:void(0);" onclick="sms();">获取验证码</a>
					</span> </label> 
					<input type="text" name="validcode" placeholder="收到的验证码"
						autocomplete="off" class="form-control placeholder-no-fix">
				</div>
				<div class="modal-footer">
					<button data-dismiss="modal" class="btn btn-default" type="button">取消</button>
					<button class="btn btn-primary" type="button" onclick="findpass();">确定</button>
				</div>
			</div>
		</div>
	</div>
	<!-- modal -->
</body>
</html>

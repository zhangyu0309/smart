<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑用户菜单</title>
<jsp:include page="/page/common/commonjs.jsp"></jsp:include>
<script type="text/javascript">
   //初始化父级节点
   $(function(){
 
	});

	/**
	 * 设置提交按钮
	 */
	var submitNow = function($dialog, $pjq) {
		parent.sy.progressBar('open');// 关闭上传进度条
		var url = sy.contextPath + "passwdChangeAdmin.do";
		$.post(url, sy.serializeObject($('form')), function(r) {
			parent.sy.progressBar('close');// 关闭上传进度条
			if (r.flag) {
				$pjq.messager.alert('提示', r.msg, 'info');
				$dialog.dialog('destroy');
			} else {
				$pjq.messager.alert('提示', r.msg, 'error');
			}
		}, 'json');
	};
	/**
	 *设置提交方法
	 */
	var submitForm = function($dialog, $pjq) {
		if ($('form').form('validate')) {
		console.log($("input[name='newpass']").val());
			if ($("input[name='newpass']").val() == $("input[name='rePasswd']").val()){
				submitNow($dialog, $pjq);
			}else{
				$pjq.messager.alert('提示', '两次密码输入不一致', 'error');
			}			
		}
	};
</script>
<style>
  table tr {
    height:35px;
  }
  .main{
       margin-left: 66px;
  }
</style>
</head>
<body>
<div class="main">
   <form method="post">
			<table>
				<tr>
					<td>原密码：</td><td><input name="passwd" class="easyui-textbox"
						data-options="required:true" style="width: 280px;" type="password" />
					</td>

				</tr>
				<tr>
					<td>新密码：</td><td><input name="newpass" class="easyui-textbox"
						data-options="required:true" style="width: 280px;" type="password"/>
					</td>
				</tr>
				<tr>
					<td>重复密码</td><td><input name="rePasswd"
						class="easyui-textbox" data-options="required:true"
						style="width: 280px;" type="password">
					</td>
				</tr>
			</table>
		</form>
    </div>
</body>
</html>
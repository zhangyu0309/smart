<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑用户菜单</title>
<jsp:include page="../../common/commonjs.jsp"></jsp:include>
<script type="text/javascript">
   var isAdd ;
   //初始化父级节点
   $(function(){
	 //初始化添加或者更新
		if("${edit_type}"==2){
		   isAdd = false;
		}else if("${edit_type}"==1){
		   isAdd = true;
		  // $("form input[name='userInfo.country']").val(Language.esuser.open.china);
		   //$("form input[name='userInfo.device_type']").val('STB');
		}
	});
	  /**
	   * 设置提交按钮(添加或者编辑用户的按钮)
	   */
	   var submitNow = function($dialog, $grid, $pjq) {
		   parent.sy.progressBar('open');// 关闭上传进度条
			var url ;
			if(isAdd){
				url = sy.contextPath+"openEsuser.do";
			}else{
				url = sy.contextPath+"editEsuser.do";
			}
			$.post(url, sy.serializeObject($('form')), function(r) {
				parent.sy.progressBar('close');// 关闭上传进度条
				if (r.flag) {
					$pjq.messager.alert('提示' , r.msg, 'info');
					$grid.datagrid('load');
					//searchBegin();
					$dialog.dialog('destroy');
				} else {
					$pjq.messager.alert('提示' , r.msg, 'error');
				}
			}, 'json');
		};
	/**
	*设置提交方法
	*/
   var submitForm = function($dialog, $grid, $pjq) {
		if ($('form').form('validate')) {
			submitNow($dialog, $grid, $pjq);
		}
	};
	
</script>
<style>
table tr {
	height: 35px;
}
input {
	height: 22px;
}
.main {
	margin-left: 66px;
}
</style>
</head>
<body>
	<div class="main">
		<form method="post">
			<table>
				<tr>
					<td id="td_customer_id">设备名称</td>
					<td><input class="easyui-validatebox textbox" type="text"
						name="userInfo.customer_id" value="${userInfo.customer_id}"
						data-options="required:true">
						<input type="hidden" name="userInfo.device_type"></input>
						</td>
					<td id="td_user_id">设备种类</td>
					<td><input class="easyui-validatebox textbox" type="text"
						name="userInfo.user_id" value="${userInfo.user_id}" 
						data-options="required:true"></input>
						</td>
				</tr>
				<tr>
					<td id="td_equipment_id">MAC地址</td>
					<td><input class="easyui-validatebox textbox" type="text"
						name="userInfo.equipment_id" value="${userInfo.equipment_id}"></td>
					<td id="td_user_name">设备Uid</td>
					<td><input class="easyui-validatebox textbox" type="text"
						name="userInfo.user_name" value="${userInfo.user_name}"></input>
						</td>
				</tr>
				<tr>
					<td id="td_city">设备图标</td>
					<td><input class="easyui-validatebox textbox" type="text"
						name="userInfo.city" value="${userInfo.city}"
						></td>
					<td id="td_postal_code"><img src="img/app/logo.png" height="24" width="24"></img></td>
					<td></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
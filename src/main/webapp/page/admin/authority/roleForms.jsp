<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑权限菜单</title>
<jsp:include page="../../common/commonjs.jsp"></jsp:include>
<script type="text/javascript">
var authorityTree;
var isAdd ;
var type=0;
var tempId="${role.roleId}";
var requestArray =[];
var requestAuth = []; 
$(function() {
	//初始化添加或者更新
	if("${editType}"==1){
		   isAdd = false;
		   type=1;
	   }else{
		   isAdd = true;
		   type=0;
	   }
	authorityTree = $('#authorityTree').tree(
			{
				url : sy.contextPath + '/getAllAuthorityWithRole.do?editType='+1+'&id='+tempId,
				parentField : 'parentId',
				idField : 'authorityId',
				width:282,
				singleSelect:false,
				cascadeCheck:true,
				checkbox: true,
				textField : 'authorityName',
				onLoadSuccess: function (data) {    //加载结束后收缩所有的节点
					$(this).tree("collapseAll"); 
					console.info(data);
				   
                }  ,
				onClick : function(node) {
			
					if(node.parentId){  //点击节点进行展开
						$(this).tree('expand',node.target);  
					}else{
						$(this).tree("collapseAll"); 
					    $(this).tree('expand',node.target);  
					}
				
				}
			});
});
 
/**
 * 设置提交按钮(添加或者修改角色的按钮)
 */
 var submitNow = function($dialog, $grid, $pjq) {
		var url ;
		var nodes = authorityTree.tree('getChecked');
		var tempAuthorityId = '';
		for(var i=0;i<nodes.length;i++){
			getParentChecked(nodes[i]);
		}
		requestAuth = sy.unique(requestArray);
		console.info(requestAuth);
		for(var i=0;i<requestAuth.length;i++){
		   tempAuthorityId+=(requestAuth[i]+",");
		}
		var requestData = sy.serializeObject($('form'));
		requestData.ids= tempAuthorityId;
		console.info(requestData);
		if(isAdd){
			url = sy.contextPath+"addRole.do";
		}else{
			url = sy.contextPath+"editRole.do";
		}
		$.post(url, requestData, function(result) {
			parent.sy.progressBar('close');// 关闭上传进度条
			if (result.flag) {
				$pjq.messager.alert('提示', result.msg, 'info');
				$grid.datagrid('load');
				$dialog.dialog('destroy');
			} else {
				$pjq.messager.alert('提示', result.msg, 'error');
			}
		}, 'json');
	};
/**
 * 提交按钮
 */
var submitForm = function($dialog, $grid, $pjq) {
	if ($('form').form('validate')) {
		submitNow($dialog, $grid, $pjq);

	}
};
/**
* 选中父级别的节点
*/
var getParentChecked = function(node){
     requestArray.push(node.authorityId);
     var parentNode = authorityTree.tree("getParent",node.target); 
     if(parentNode!=undefined){
        getParentChecked(parentNode);
     }
};
</script>
<style>
.table tr {
	height: 35px;
}

.main {
	margin-left: 66px;
}
</style>
</head>
<body>
	<div>
		<form method="post">
			<table>
				<tr>
				<td style="width:73px;"></td>
					<td>
						<fieldset style="height:282px;border:0px;margin:0px;padding:0px;">
							<legend>角色基本信息</legend>
							<table>
								<tr>
									<td>角色名称：<input name="role.roleName"
										class="easyui-textbox" value="${role.roleName}"
										data-options="required:true" style="width: 150px;" /> <input
										type="hidden" name="role.roleId"
										value="${role.roleId}" /></td>

								</tr>
								<tr>
									<td ><div>描述信息：</div>
										<div style="margin-top: -16px;margin-left: 79px;width: 100%;">
											<textarea style="width: 144px;" id="" rows=6 name="role.description"
												value="">${role.description}</textarea>
										</div>
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
					<td style="width:20px;"></td>
					<td>
						<fieldset style="height:282px;border:0px;margin:0px;padding:0px;">
							<legend>角色拥有权限</legend>
                           
                              <ul id="authorityTree"></ul>
                         
						</fieldset></td>
				</tr>

			</table>

		</form>
	</div>
</body>
</html>
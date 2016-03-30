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
   
   var parentObj;
   var typeObj;
   var enabledObj;
   var parentId;
   var isAdd ;
   //初始化父级节点
   $(function(){
	   /**
	   * 初始化combotree 
	   */
	  parentObj = $("#parentId").combotree({
		  url: sy.contextPath + "getAllParentMenuAuthority.do",
		  idField:"authorityId",
		  parentField:"parentId",
		  valueField:"id",  
		  textField:"text",	    
		  onSelect: function (row) {
		  }
	  });
	  typeObj =  $("#typeA").combobox({
			valueField: 'id',
			textField: 'text',
			required:true,
			editable:false,
			data: [{
				id: 1,
				text: '菜单权限'
			},{
				id: 2,
				text: '功能权限'
			}]
	  });
	  enabledObj =  $("#enabledA").combobox({
			valueField: 'id',
			textField: 'text',
			required:true,
			editable:false,
			data: [{
				id: 1,
				text: '正常'
			},{
				id: 0,
				text: '禁用'
			}]
	  });
	   if("${editType}"==1){
		   isAdd = false;
		   parentObj.combotree("setValue","${authority.parentId}");
		   typeObj.combobox("setValue","${authority.type}");
		   enabledObj.combobox("setValue","${authority.enabled}");
	   }else{
		   isAdd = true;
		   parentObj.combotree("setValue","${id}");
		   typeObj.combobox("setValue","1");
		   enabledObj.combobox("setValue","1");
	   }
	   
	   
   });
   
   /**
   * 删除权限菜单栏
   */
   var deleteAuthority = function(){
	   
   };
   /**
	   * 设置提交按钮(添加或者修改权限的按钮)
	   */
	   var submitNow = function($dialog, $grid, $pjq) {
			var url ;
			if(isAdd){
				url = sy.contextPath+"addAuthority.do";
			}else{
				url = sy.contextPath+"editAuthority.do";
			}
			$.post(url, sy.serializeObject($('form')), function(result) {
				parent.sy.progressBar('close');// 关闭上传进度条
				if (result.flag) {
					$pjq.messager.alert('提示', result.msg, 'info');
					$grid.treegrid('load');
					$grid.treegrid('expand',parentId);
					$dialog.dialog('destroy');
				} else {
					$pjq.messager.alert('提示', result.msg, 'error');
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
        <table >
            <tr >
                <td >菜单名称：<input name="authority.authorityName" class="easyui-textbox" value="${authority.authorityName}" data-options="required:true" style="width: 200px;" />
                <input type="hidden" name="authority.authorityId" value="${authority.authorityId}" />
                </td>
                <td>类别：<select id="typeA" name="authority.type" class="easyui-combobox"   style="width: 80px;">
						</select>
                    
                </td>
            </tr>
            <tr >
                <td>显示图标：<input  name="authority.iconCls" class="easyui-textbox" value="${authority.iconCls}" style="width: 200px;"/></td>
                <td>禁用：<select id="enabledA" name="authority.enabled" class="easyui-combobox"  style="width: 80px;"></select>
										
            </tr>
            <tr >
                <td colspan=2  >响应地址：<input name="authority.menuUrl" class="easyui-textbox" value="${authority.menuUrl}"  style="width: 330px;"/></td>
            </tr>
            <tr >
                <td colspan=2  >父级节点：<input id="parentId" name="authority.parentId" class="easyui-combotree"   style="width: 330px;"/></td>
            </tr>
            <tr >
                <td colspan=2  ><div>描述信息：</div ><div style="margin-top: -16px;margin-left: 79px;width: 100%;" ><textarea id="" rows=6 name="authority.description"   style="width: 330px;" >${authority.description}</textarea></div></td>
            </tr>
        </table>
    </form>
    </div>
</body>
</html>
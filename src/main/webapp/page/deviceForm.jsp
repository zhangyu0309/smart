<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑设备菜单</title>
<jsp:include page="common/commonjs.jsp"></jsp:include>
<script type="text/javascript">
   var user_id = "${user_id}";
   var parentObj;
   var typeObj;
   var enabledObj;
   var parentId;
   var isAdd ;
   var subType;
   //初始化父级节点
   $(function(){
	   /**
	   * 初始化combotree 
	   */
	  parentObj = $("#parentId").combobox({
		  url: sy.contextPath + "getAllParentDevice.do?user_id="+user_id,
		  idField:"device_id",
		  valueField:"device_id",  
		  textField:"device_name",
		  panelHeight:"auto",
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
				text: '主控板'
			},{
				id: 2,
				text: '子设备'
			}]
	  });
	  
	  typeObj.combobox({
			onSelect: function(param){
				if (param.id == 2){
					$("#devicetype").attr('style', '');
				}else {
					$("#devicetype").attr('style', 'display:none;');
				}
			}
		});
	  subType =  $("#subType").combobox({
			valueField: 'id',
			textField: 'text',
			required:true,
			editable:false,
			data: [{
				id: 0,
				text: '开关/插座'
			},{
				id: 1,
				text: '电机'
			},{
				id: 2,
				text: '光调节器'
			}]
	  });
	   if("${editType}"==1){
		   isAdd = false;
		   if ("${device.parent_id}" == "0"){
		   	typeObj.combobox("setValue","1");
		   	$("#devicetype").attr('style', 'display:none;');
		   }
		   else{
			$("#devicetype").attr('style', '');
		    parentObj.combobox("setValue","${device.parent_id}");
		   	typeObj.combobox("setValue","2");
		   	subType.combobox("setValue","${device.device_type}");
		   }
	   }else{
		   isAdd = true;
		   typeObj.combobox("setValue","1");
		   subType.combobox("setValue","0");
		   $("#devicetype").attr('style', 'display:none;');
	   }
	   
   });
   
   /**
	   * 设置提交按钮(添加或者修改权限的按钮)
	   */
	   var submitNow = function($dialog, $grid, $pjq) {
			var url ;
			if(isAdd){
				url = sy.contextPath+"addDevice.do";
			}else{
				url = sy.contextPath+"editDevice.do";
			}
			$.post(url, sy.serializeObject($('form')), function(result) {
				parent.sy.progressBar('close');
				if (result.flag) {
					$pjq.messager.alert('提示', result.msg, 'info');
					$grid.treegrid('load');
					if (typeObj.combobox("getValue") == "2"){
						$grid.treegrid('expand',parentObj.combobox("getValue"));
					}
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
       margin-left: 10px;
  }
</style>
</head>
<body>
<div class="main">
   <form method="post">
        <table >
            <tr >
                <td >设备&nbsp;ID：
                <s:if test="editType == 0">
                	<input name="device.device_id" class="easyui-textbox" value="${device.device_id}" data-options="required:true" style="width: 200px;" />
                </s:if>
                <s:if test="editType == 1">
                	<input class="easyui-textbox" value="${device.device_id}" readonly data-options="required:true" style="width: 200px;" />
                	<input name="device.device_id" value="${device.device_id}" type="hidden"/>
                </s:if>
                </td>
                <td>类别：<select id="typeA" name="device.online" class="easyui-combobox" style="width: 80px;">
						</select>
                </td>
            </tr>
            <tr><td colspan=2><font color="red" size="1.5">
            	设备ID:主设备直接填写设备号，子设备填写子设备序号，限制1-255之间的整数 
            	</font>
            </td></tr>
            <tr id="devicetype">
	            <td colspan=2>子设备类型：<select id="subType" name="device.device_type" class="easyui-combobox" style="width: 310px;">
						</select>
	            </td>
            </tr>
            <tr >
                <td colspan=2  >设备名称：<input name="device.device_name" class="easyui-textbox" data-options="required:true" value="${device.device_name}"  style="width: 330px;"/>
                <input name="device.user_id" value="${user_id}" type="hidden"/>
                </td>
            </tr>
            <tr >
                <td colspan=2  >上级设备：<input id="parentId" name="device.parent_id" class="easyui-combobox" style="width: 330px;"/>
            </tr>
            <tr >
                <td colspan=2  ><div>描述信息：</div ><div style="margin-top: -16px;margin-left: 79px;width: 100%;" >
                <textarea id="" rows=6 name="device.description"   style="width: 330px;" >${device.description}</textarea></div></td>
            </tr>
        </table>
    </form>
    </div>
</body>
</html>
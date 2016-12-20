var treeGrid;
var clickNode;
var clickRows =[];
$(function() {
	/**
	 * 初始化设备列表
	 * 
	 * 
	 * returns
	 */
	treeGrid = $('#treeGrid')
			.treegrid(
					{
						title : '',
						rownumbers : true,
						lines : false,
						url : sy.contextPath + '/getAllMyDataDevice.do',
						idField : 'device_id',
						parentField : 'parent_id',
						treeField : 'device_id',
						singleSelect : true,
						onLoadSuccess : function(row) {
							//初始化后展开的菜单选项
							$(this).treegrid("collapseAll");
							if(clickNode!=undefined){
								treeGridExpand(clickNode,'treeGrid');
								for(var i=0;i<clickRows.length;i++){
									$(this).treegrid("expand",clickRows[i].device_id);
								}
							}
						},
						onDragOver: function(targetRow,sourceRow){
			                   console.info(targetRow.name);
			                   console.info(sourceRow.name);
			                   return false;
			            },
			            onClickRow : function(node){  //点击子节点进行展开
							if(node.parent_id){
								if(node.parent_id=='0'){
									$(this).treegrid("expand",node.device_id);
								}
							}else{
								$(this).treegrid("collapseAll");
								$(this).treegrid("expand",node.device_id);
							}
							clickNode = node;
							clickRows = [];
						},
						columns : [ [
								{
									title : '设备编号',
									field : 'device_id',
									width : 175,
									formatter : function(value,data) {
										if (data.parent_id == '0') {
											return value;
										} else {
											return value.split('-')[1];
										} 
									}
								},{
									title : '设备名称',
									field : 'device_name',
									width : 175
								},
								{
									field : 'parent_id',
									title : '设备类型',
									width : 100,
									formatter : function(value, data) {
										if (value == '0') {
											var s = '主控板';
											return s;
										} else {
											if(data.device_type == 1){
												return '电机';
											}else if(data.device_type == 0){
												return '开关/插座';
											}else if(data.device_type == 2){
												return '光调节器';
											}
										} 
									}
								},
								{
									field : 'user_id',
									title : '用户ID',
									width : 100
								},
								{
									field : 'real_name',
									title : '用户名称',
									width : 100
								},
								{
									field : 'description',
									title : '描述信息',
									width : 250
								}
								 ] ],
						toolbar : '#toolbar'
					});

});
/**
 * 遍历方法来控制树形的展开
 * 
 */
var treeGridExpand = function(node,domId){
	var tempNode = $('#'+domId).treegrid("getParent",node.device_id);
	if(node.type==1){
		clickRows.push(node);
	}
	if(tempNode){
		clickRows.push(tempNode);
		treeGridExpand(tempNode,domId);
	}
	
};

var searchBegin = function() {
	var beginTime = $('#beginTime').val();
	var endTime = $('#endTime').val();
	if (beginTime != "" && endTime == "") {
		treeGrid.treegrid('load', sy.serializeObject($('#searchForm')));
	} else if (beginTime > endTime) {
		parent.$.messager.alert('提示', "搜索开始时间不能大于结束时间", 'info');
	} else {
		treeGrid.treegrid('load', sy.serializeObject($('#searchForm')));
	}

};

/**
 * kai
 */
var deviceon = function(){
	var rows = treeGrid.datagrid('getSelections');
	var num = rows.length;

	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行操作!', 'info'); 
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行操作!', 'info'); 
		return;
	}else {
		if (rows[0].device_type != 0 && rows[0].device_type != 1){
			parent.$.messager.alert('提示', '请选择[开关/插座/电机]类型的子设备!', 'info'); 
			return;
		}
			$.ajax({
				type:"post", //请求方式
				url:sy.contextPath+"onoffDevice.do", //发送请求地址
				data:{
			   		"option":0,
			   		"device_id":rows[0].device_id
				   },
				success:function(r){
					if(r.flag){
						parent.$.messager.alert('提示', r.msg, 'info');
						//treeGrid.treegrid('load');
					}else{
						parent.$.messager.alert('提示', r.msg, 'info');
					}
				}
				});
	}
};

/**
 * off
 */
var deviceoff = function(){
	var rows = treeGrid.datagrid('getSelections');
	var num = rows.length;

	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行操作!', 'info'); 
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行操作!', 'info'); 
		return;
	}else {
		if (rows[0].device_type != 0 && rows[0].device_type != 1){
			parent.$.messager.alert('提示', '请选择[开关/插座/电机]类型的子设备!', 'info'); 
			return;
		}
		$.ajax({
			type:"post", //请求方式
			url:sy.contextPath+"onoffDevice.do", //发送请求地址
			data:{
		   		"option":1,
		   		"device_id":rows[0].device_id
			   },
			success:function(r){
				if(r.flag){
					parent.$.messager.alert('提示', r.msg, 'info');
					//treeGrid.treegrid('load');
				}else{
					parent.$.messager.alert('提示', r.msg, 'info');
				}
			}
			});
	}
};
/**
 * 停
 */
var stopdevice = function(){
	var rows = treeGrid.datagrid('getSelections');
	var num = rows.length;

	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行操作!', 'info'); 
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行操作!', 'info'); 
		return;
	}else {
		if (rows[0].device_type != 1){
			parent.$.messager.alert('提示', '请选择[电机]类型的子设备!', 'info'); 
			return;
		}
		//rows[0].device_id
			$.ajax({
				type:"post", //请求方式
				url:sy.contextPath+"onoffDevice.do", //发送请求地址
				data:{
			   		"option":2,
			   		"device_id":rows[0].device_id
				   },
				success:function(r){
					if(r.flag){
						parent.$.messager.alert('提示', r.msg, 'info');
					}else{
						parent.$.messager.alert('提示', r.msg, 'info');
					}
				}
				});
	}
};
/**
 * 定时
 */
var devicetimer = function(){
	var rows = treeGrid.datagrid('getSelections');
	var num = rows.length;
	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行操作!', 'info'); 
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行操作!', 'info'); 
		return;
	}else {
		if (rows[0].device_type != 0 && rows[0].device_type != 1){
			parent.$.messager.alert('提示', '请选择[开关/插座/电机]类型的子设备!', 'info'); 
			return;
		}
		if (rows[0].parent_id == '0'){
			var dialog = parent.sy.modalDialog({
				title : '定时场景-' + rows[0].device_name,
				width:600,
			    height:400,
				url : sy.contextPath + "beginAddOrUpdateDevice.do?editType=4&deviceId=" + rows[0].device_id,
				buttons : [ {
					text : '保存',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.submitForm(dialog,
								treeGrid, parent.$);

					}
				} ]
			});
		}else {
			var dialog = parent.sy.modalDialog({
				title : '定时开关-' + rows[0].device_name,
				width:600,
			    height:400,
				url : sy.contextPath + "beginAddOrUpdateDevice.do?editType=3&deviceId=" + rows[0].device_id,
				buttons : [ {
					text : '保存',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.submitForm(dialog,
								treeGrid, parent.$);

					}
				} ]
			});
		}
	}
};

/**
 * 调亮
 */
var dimmer = function(){
	var rows = treeGrid.datagrid('getSelections');
	var num = rows.length;
	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行操作!', 'info'); 
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行操作!', 'info'); 
		return;
	}else {
		if (rows[0].device_type != 2){
			parent.$.messager.alert('提示', '请选择[光调节器]类型的子设备!', 'info'); 
			return;
		}
		var dialog = parent.sy.modalDialog({
			title : '亮度调节-' + rows[0].device_name,
			width:500,
		    height:300,
			url : sy.contextPath + "beginAddOrUpdateDevice.do?editType=5&deviceId=" + rows[0].device_id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog,
							treeGrid, parent.$);

				}
			} ]
		});
	}
};
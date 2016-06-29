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
						url : sy.contextPath + '/getAllDataDevice.do?device.user_id='+user_id,
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
									width : 225,
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
									width : 225
								},
								{
									field : 'parent_id',
									title : '设备类型',
									width : 100,
									formatter : function(value) {
										if (value == '0') {
											var s = '主控板';
											return s;
										} else {
											return '子设备';
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
 * 添加设备
 * @returns
 */
var addDevice = function(path) {
//	var tempId="";
//	if(clickNode!=undefined){
//		tempId = clickNode.device_id;
//	}
	var dialog = parent.sy.modalDialog({
		title : '添加菜单',
		width:600,
	    height:400,
		url : sy.contextPath +"beginAddOrUpdateDevice.do?editType=0&user_id="+user_id,
		buttons : [ {
			text : '添加',
			handler : function() {
				dialog.find('iframe').get(0).contentWindow.submitForm(dialog,
						treeGrid, parent.$);

			}
		} ]
	});
};
/**
 * 修改功能
 * @returns
 */
var editDevice = function() {
	var rows = treeGrid.datagrid('getSelections');
	var num = rows.length;
	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行编辑操作!', 'info'); // $.messager.alert('提示', '请选择一条记录进行操作!',
		// 'info');
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行编辑操作!', 'info'); // $.messager.alert('提示',
		// '您选择了多条记录,只能选择一条记录进行修改!',
		// 'info');
		return;
	}else {
		var dialog = parent.sy.modalDialog({
			title : '修改菜单',
			width:600,
		    height:400,
			url : sy.contextPath + "beginAddOrUpdateDevice.do?editType=1&device.device_id="+clickNode.device_id+"&user_id="+user_id,
			buttons : [ {
				text : '修改',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog,
							treeGrid, parent.$);

				}
			} ]
		});
	}
	
};
/**
 * 删除设备
 * @returns
 */
var deleteDevice = function() {
	var rows = treeGrid.datagrid('getSelections');
	var num = rows.length;

	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行删除操作!', 'info'); // $.messager.alert('提示', '请选择一条记录进行操作!',
		// 'info');
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行删除操作!', 'info'); // $.messager.alert('提示',
		// '您选择了多条记录,只能选择一条记录进行修改!',
		// 'info');
		return;
	}else {
			parent.$.messager.confirm('询问', '您确定要删除此记录？删除一个主控板同时也会删除所有的子设备！', function(r) {
				clickNode = treeGrid.treegrid("getParent",rows[0].device_id);
				if (r) {
					$.ajax({
						type:"post", //请求方式
						url:sy.contextPath+"deleteDevice.do", //发送请求地址
						data:{
					   		"device.device_id":rows[0].device_id,
						   },
						//请求成功后的回调函数有两个参数
						success:function(r){
							if(r.flag){
								parent.$.messager.alert('提示', r.msg, 'info');
								treeGrid.treegrid('load');
							}else{
								parent.$.messager.alert('提示', r.msg, 'info');
							}
						}
						
						});
				}
			});
		}
};

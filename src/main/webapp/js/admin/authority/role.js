var roleGrid;
/**
 * 初始化数据表
 */
$(function() {  
	roleGrid = $('#grid')
			.datagrid(
					{
						title : '',
						url : sy.contextPath + '/getAllRole.do',
						rownumbers : true,
						pagination : true,
						singleSelect: true,
//						selectOnCheck: true,
//						checkOnSelect: true,
						page:1,
						pageSize : 25,
						pageList : [25],
						fit : true,
						columns : [ [
								{
									 field:'ck',
									 checkbox:true
								},
								{
									width : '100',
									title : '角色名称',
									field : 'roleName',
									sortable : false
								},
								{
									width : '150',
									title : '创建时间',
									field : 'createTime'
								},
								{
									width : '240',
									title : '角色描述',
									field : 'description'
								}
								 ] ],
						toolbar : '#toolbar',
						onLoadSuccess : function(data) {
							roleGrid.datagrid('unselectAll');
							roleGrid.datagrid('clearSelections');
						}
					});
});
/**
 * 添加功能权限
 * @returns
 */
var addRole = function(path) {
	var dialog = parent.sy.modalDialog({
		title : '添加角色',
		width:650,
	    height:400,
		url : sy.contextPath +"beginAddOrUpdateRole.do?editType=0",
		buttons : [ {
			text : '添加',
			handler : function() {
				dialog.find('iframe').get(0).contentWindow.submitForm(dialog,
						roleGrid, parent.$);

			}
		} ]
	});
};

/**
 * 修改角色
 * @returns
 */
var editRole = function() {
	var rows = roleGrid.datagrid('getSelections');
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
			width:650,
		    height:400,
			url : sy.contextPath + "beginAddOrUpdateRole.do?editType=1&id="+rows[0].roleId,
			buttons : [ {
				text : '修改',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog,
							roleGrid, parent.$);

				}
			} ]
		});
	}
	
};
/**
 * 删除角色权限
 * @returns
 */
var deleteRole = function() {
	var rows = roleGrid.datagrid('getSelections');
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
			parent.$.messager.confirm('询问', '您确定要删除此记录？', function(r) {
				
				if (r) {
					$.ajax({
						
						type:"post", //请求方式
						url:sy.contextPath+"deleteRole.do", //发送请求地址
						data:{
					   		"role.roleId":rows[0].roleId,
						   },
						//请求成功后的回调函数有两个参数
						success:function(r){
							if(r.flag){
								parent.$.messager.alert('提示', r.msg, 'info');
								roleGrid.datagrid('load');
							}else{
								parent.$.messager.alert('提示', r.msg, 'info');
							}
						}
						
						});
				}
			});
		}
};


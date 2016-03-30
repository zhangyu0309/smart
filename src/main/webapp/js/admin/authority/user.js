var grid;
var nowSelectRow = undefined;
var rowNowStatue = undefined;
$(function() {
	grid = $('#grid')
			.datagrid(
					{
						title : '',
						url : sy.contextPath + '/getAllUser.do',
						rownumbers : true,
						pagination : true,

						singleSelect : true,
						idField : 'userId',
						pageSize : 25,
						pageList : [ 25 ],
						fit : true,

						columns : [ [
								{
									width : 50,
									field : 'ck',
									checkbox:true
								},
								{
									width : 100,
									title : '账号',
									field : 'userId',
									sortable : false
								},
								{
									width : 150,
									title : '设备编号',
									field : 'deviceno',
									sortable : false
								},
								{
									width : 100,
									title : '姓名',
									field : 'realName',
									sortable : false
								},
								{
									width : 150,
									title : '邮箱',
									field : 'email',
									sortable : false
								},
								{
									width : 150,
									title : '电话',
									field : 'phone',
									sortable : false
								},
								{
									width : 150,
									title : '城市',
									field : 'city',
									sortable : false
								},
								{
									width : '80',
									title : '冻结',
									field : 'enable',
									sortable : false,
									formatter : function(value, row, index) {

										if (value == '1')
											return '<span aligin="center" style="color:green;">'
													+ '未冻结</span>';
										;
										if (value == '0')
											return '<span aligin="center" style="color:#FFCC33;">'
													+ '已冻结</span>';
										;
									}
								},
								{
									width : 200,
									title : '创建时间',
									field : 'createTime'
								} ] ],
						toolbar : '#toolbar',
						onClickRow : function(rowIndex, rowData) {
							// console.info("Clear Selections !");
							if (rowNowStatue == undefined
									&& nowSelectRow != rowIndex) {
								grid.datagrid('selectRow', rowIndex);
								rowNowStatue = 1;
								nowSelectRow = rowIndex;

							} else if (nowSelectRow != rowIndex) {
								grid.datagrid('unselectAll');
								grid.datagrid('clearSelections');
								grid.datagrid('selectRow', rowIndex);
								rowNowStatue = 1;
								nowSelectRow = rowIndex;
							} else {
								grid.datagrid('unselectAll');
								grid.datagrid('clearSelections');
								rowNowStatue = undefined;
								nowSelectRow = undefined;
							}

						},
						onLoadSuccess : function(data) {
							// console.info("Clear Selections !");
							grid.datagrid('unselectAll');
							grid.datagrid('clearSelections');
						}
					});
});

var addUser = function() {
	var dialog = parent.sy.modalDialog({
		title : '添加用户信息',
		width : 500,
		height : 400,
		url : sy.contextPath + '/beginAddOrUpdateUser.do?editType=0',
		buttons : [ {
			text : '添加',

			handler : function() {
				dialog.find('iframe').get(0).contentWindow.submitForm(dialog,
						grid, parent.$);

			}
		} ]
	});
};
var searchBegin = function() {
	var beginTime = $('#beginTime').val();
	var endTime = $('#endTime').val();
	if (beginTime != "" && endTime == "") {
		grid.datagrid('load', sy.serializeObject($('#searchForm')));
	} else if (beginTime > endTime) {
		parent.$.messager.alert('提示', "搜索开始时间不能大于结束时间", 'info');
	} else {
		grid.datagrid('load', sy.serializeObject($('#searchForm')));
	}

};
/**
 * 编辑用户
 * 
 * @returns
 */
var editUser = function() {

	var rows = grid.datagrid('getSelections');
	var num = rows.length;

	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行操作!', 'info'); // $.messager.alert('提示',
																// '请选择一条记录进行操作!',
		// 'info');
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行修改!', 'info'); // $.messager.alert('提示',
		// '您选择了多条记录,只能选择一条记录进行修改!',
		// 'info');
		return;
	} else {

		var dialog = parent.sy.modalDialog({
			title : '编辑用户信息',
			width : 500,
			height : 400,
			url : sy.contextPath + '/beginAddOrUpdateUser.do?editType=1&id='
					+ rows[0].userId,
			buttons : [ {
				text : '编辑',

				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(
							dialog, grid, parent.$);
				}
			} ]
		});
	}
};
/**
 * 解冻
 * 
 * @returns
 */
var cancelFreezeUser = function() {
	var rows = grid.datagrid('getSelections');
	var num = rows.length;

	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行操作!', 'info'); // $.messager.alert('提示',
																// '请选择一条记录进行操作!',
		// 'info');
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行修改!', 'info'); // $.messager.alert('提示',
		// '您选择了多条记录,只能选择一条记录进行修改!',
		// 'info');
		return;
	} else {
		$.ajax({
			type : "post", // 请求方式
			url : "cancelFreezeUser.do", // 发送请求地址
			data : {
				"admin.userId" : rows[0].userId,
				"admin.enable" : 1,
				"admin.zabbix_id" : rows[0].zabbix_id
			},
			// 请求成功后的回调函数有两个参数
			success : function(r) {
				if (r.flag) {
					parent.$.messager.alert('提示', r.msg, 'info');
					grid.datagrid('reload');

				} else {
					parent.$.messager.alert('提示', r.msg, 'info');
					grid.datagrid('reload');
				}
			}

		});

	}
};
/**
 * 冻结
 * 
 * @returns
 */
var freezeUser = function() {
	var rows = grid.datagrid('getSelections');
	var num = rows.length;

	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行操作!', 'info'); // $.messager.alert('提示',
																// '请选择一条记录进行操作!',
		// 'info');
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行修改!', 'info'); // $.messager.alert('提示',
		// '您选择了多条记录,只能选择一条记录进行修改!',
		// 'info');
		return;
	} else {
		$.ajax({
			type : "post", // 请求方式
			url : "freezeUser.do", // 发送请求地址
			data : {
				"admin.userId" : rows[0].userId,
				"admin.enable" : 0,
				"admin.zabbix_id" : rows[0].zabbix_id
			},
			// 请求成功后的回调函数有两个参数
			success : function(r) {
				if (r.flag) {
					parent.$.messager.alert('提示', r.msg, 'info');
					grid.datagrid('reload');

				} else {
					parent.$.messager.alert('提示', r.msg, 'info');
					grid.datagrid('reload');
				}
			}

		});

	}
};

var deleteUser = function() {
	var rows = grid.datagrid('getSelections');
	var num = rows.length;
	if (num == 0) {
		$.messager.alert('提示', '请选择一条记录进行操作!', 'info'); // $.messager.alert('提示',
														// '请选择一条记录进行操作!',
		// 'info');
		return;
	} else if (num > 1) {
		$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行修改!', 'info'); // $.messager.alert('提示',
		// '您选择了多条记录,只能选择一条记录进行修改!',
		// 'info');
		return;
	} else {
		parent.$.messager.confirm('询问', '您确定要删除此记录？', function(r) {
			if (r) {
				$.ajax({

					type : "post", // 请求方式
					url : "deleteUser.do", // 发送请求地址
					data : {
						id : rows[0].userId,
						zid : rows[0].zabbix_id,
					},
					// 请求成功后的回调函数有两个参数
					success : function(r) {
						if (r.flag) {
							parent.$.messager.alert('提示', r.msg, 'info');
							grid.datagrid('reload');
						} else {
							parent.$.messager.alert('提示', r.msg, 'info');
						}
					}

				});
			}
		});
	}
};

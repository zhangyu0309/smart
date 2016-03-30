var grid;
var nowSelectRow = undefined;
var rowNowStatue = undefined;
$(function() {
	grid = $('#dg')
			.datagrid(
					{
						title : '设备列表',
						url : '',//sy.contextPath + '/getAllHost.do',
						singleSelect : false,
						idField : 'deviceid',
						fit : true,
						columns : [ [ {
							field : '1',
							title : '设备图标',
							width : '25%'
						}, {
							field : '2',
							title : '设备种类',
							width : '25%'
						},{
							field : '3',
							title : 'MAC地址',
							width : '25%'
						},{
							field : '4',
							title : '设备Uid',
							width : '25%'
						} ] ],
						toolbar : '#toolbar',
						onClickRow : function(rowIndex, rowData) {
						},
						onLoadSuccess : function(data) {
							grid.datagrid('unselectAll');
							grid.datagrid('clearSelections');
						}
					});
});

/**
 * 查询
 * @returns
 */
var searchBegin = function() {
	grid.datagrid('load', sy.serializeObject($('#searchForm')));
};


var add = function() {
	var dialog = parent.sy.modalDialog({
		title : '添加设备',
		width : 700,
		height : 400,
		url : sy.contextPath + '/beginAddOrUpdateHost.do?editType=0',
		buttons : [ {
			text : '添加',
			handler : function() {
				dialog.find('iframe').get(0).contentWindow.submitForm(dialog,
						grid, parent.$);

			}
		} ]
	});
};

var deleteHost = function() {
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
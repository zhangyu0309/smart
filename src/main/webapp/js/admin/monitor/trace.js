var grid;
var nowSelectRow = undefined;
var rowNowStatue = undefined;
$(function() {
	grid = $('#dg')
			.datagrid(
					{
						title : '设备列表',
						url : sy.contextPath + '/getAllHost.do',
						singleSelect : false,
						idField : 'deviceid',
						fit : true,
						columns : [ [ {
							field : 'deviceid',
							title : '设备编号',
							width : '50%'
						}, {
							field : 'online',
							title : '在线状态',
							width : '40%',
							formatter : function(value, row) {
								return value == 1 ? '<span aligin="center" style="color:green;">在线</span>'
										: '<span aligin="center" style="color:red;">离线</span>';
							}
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
/**
 * 
 * 查看轨迹
 * @returns
 */
var view = function() {
	var rows = grid.datagrid('getSelections');
	var num = rows.length;
	if (num == 0) {
		$.messager.alert('提示', '请选择至少一台设备', 'info');
		return;
	}/* else if (num > 1) {
		$.messager.alert(Language.Common.tip, Language.Common.selectone, 'info');
		return;
	}*/ else {
		var ids = rows[0].deviceid;
		$.each(rows, function(i, n) {
			if (i > 0){
				ids = ids + ',' + n.deviceid;
			}
		});
		var tempPathUrl = '/previewHost.do?deviceid='+  ids;
		/*var dialog = sy.modalDialog({
			title : '查看轨迹 ' + rows[0].deviceid,
			width : "100%",
			height :"100%",
			url : sy.contextPath + tempPathUrl,
			closable:true,
			collapsible:true,
			resizable:true
		});*/
		window.open(sy.contextPath + tempPathUrl);
	}
};

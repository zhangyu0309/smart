var treeGrid;
var clickNode;
var clickRows =[];
$(function() {
	/**
	 * 初始化权限列表
	 * 
	 * @author RM
	 * 
	 * returns
	 */
	treeGrid = $('#treeGrid')
			.treegrid(
					{
						title : '',
						rownumbers : true,
						lines : false,
						url : sy.contextPath + '/getAllDataAuthority.do',
						idField : 'authorityId',
						parentField : 'parentId',
						treeField : 'authorityName',
						singleSelect : true,
						onLoadSuccess : function(row) {
							//初始化后展开的菜单选项
							$(this).treegrid("collapseAll");
							if(clickNode!=undefined){
								treeGridExpand(clickNode,'treeGrid');
								for(var i=0;i<clickRows.length;i++){
									$(this).treegrid("expand",clickRows[i].authorityId);
								}
							}
//							$(this).treegrid('enableDnd', row?row.authorityId:null);
						},
						onDragOver: function(targetRow,sourceRow){
			                   console.info(targetRow.name);
			                   console.info(sourceRow.name);
			                   return false;
			            },
			            onClickRow : function(node){  //点击子节点进行展开
							if(node.parentId){
								if(node.type==1){
									$(this).treegrid("expand",node.authorityId);
								}
							}else{
								$(this).treegrid("collapseAll");
								$(this).treegrid("expand",node.authorityId);
//								clickNode = $(this).treegrid("getParent",node.authorityId);
							}
							clickNode = node;
							clickRows = [];
						},
						columns : [ [
								{
									title : '权限名称',
									field : 'authorityName',
									width : 225
								},
								{
									field : 'type',
									title : '权限类别',
									width : 100,
									formatter : function(value) {
										if (value == 1) {
											var s = '菜单权限';
											return s;
										} else if (value == 2) {
											return '功能权限';
										} 
									}
								},
								{
									title : '权限Url',
									field : 'menuUrl',
									width : 275
								},
								{
									field : 'iconCls',
									title : '权限图标',
									width : 150
								},
								{
									field : 'enabled',
									title : '是否禁用',
									width : 60,
									formatter : function(value) {
										if (value == 0) {
											var s = '禁用';
											return s;
										} else {
											return '正常';
										}
									}
								},
								{
									field : 'description',
									title : '资源描述',
									width : 250,
									editor : 'text'
								}
								 ] ],
						toolbar : '#toolbar'
					});

});
/**
 * 遍历方法来控制树形的展开
 * 
 * @author RM
 * 
 */
var treeGridExpand = function(node,domId){
	var tempNode = $('#'+domId).treegrid("getParent",node.authorityId);
	if(node.type==1){
		clickRows.push(node);
	}
	if(tempNode){
		clickRows.push(tempNode);
		treeGridExpand(tempNode,domId);
	}
	
};
/**
 * 添加功能权限
 * @returns
 */
var addAuthority = function(path) {
	var tempId="";
	if(clickNode!=undefined){
		tempId = clickNode.authorityId;
	}
	var dialog = parent.sy.modalDialog({
		title : '添加菜单',
		width:600,
	    height:400,
		url : sy.contextPath +"beginAddOrUpdateAuthority.do?editType=0&id="+tempId,
		buttons : [ {
			text : '添加',
			handler : function() {
				dialog.find('iframe').get(0).contentWindow.submitForm(dialog,treeGrid, parent.$);
			}
		} ]
	});
};
/**
 * 修改功能权限
 * @returns
 */
var editAuthority = function() {
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
			url : sy.contextPath + "beginAddOrUpdateAuthority.do?editType=1&id="+clickNode.authorityId,
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
 * 删除功能菜单权限
 * @returns
 */
var deleteAuthority = function() {
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
			parent.$.messager.confirm('询问', '您确定要删除此记录？', function(r) {
				clickNode = treeGrid.treegrid("getParent",rows[0].authorityId);
				
				if (r) {
					$.ajax({
						
						type:"post", //请求方式
						url:sy.contextPath+"deleteAuthority.do", //发送请求地址
						data:{
					   		"authority.authorityId":rows[0].authorityId,
						   },
						//请求成功后的回调函数有两个参数
						success:function(r){
							if(r.flag){
								parent.$.messager.alert('提示', r.msg, 'info');
								treeGrid.treegrid('load');
//								treeGrid.treegrid("expand",expandId);
							}else{
								parent.$.messager.alert('提示', r.msg, 'info');
							}
						}
						
						});
				}
			});
		}
};

/**
 * 上移
 * @returns
 */
var upAuthority = function(){
	var rows = treeGrid.datagrid('getSelections');
	var num = rows.length;

	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行删除操作!', 'info'); 
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行删除操作!', 'info'); 
		return;
	}else {
		var alterid = '';//需要和选中记录交换位置的node的id
		var alterindex = -1;//需要和选中记录交换位置的node的dataindex
		$.each(treeGrid.treegrid('getChildren', rows[0].parentId), function(i, n) {
			if (n.parentId == rows[0].parentId && n.dataIndex < rows[0].dataIndex){
				if (n.dataIndex > alterindex){
					alterid = n.authorityId;
					alterindex = n.dataIndex;
				}
			}
		});
		if (alterid != ''){
			$.ajax({
				
				type:"post", //请求方式
				url:sy.contextPath+"moveAuthority.do", //发送请求地址
				data:{
			   		"authorityId1":alterid,
			   		"index1":alterindex,
			   		"authorityId2": rows[0].authorityId,
			   		"index2": rows[0].dataIndex,
				   },
				//请求成功后的回调函数有两个参数
				success:function(r){
					if(r.flag){
						parent.$.messager.alert('提示', r.msg, 'info');
						treeGrid.treegrid('load');
//						treeGrid.treegrid("expand",expandId);
					}else{
						parent.$.messager.alert('提示', r.msg, 'info');
					}
				}
				
				});
			
		}else{
			parent.$.messager.alert('提示', '无法上移，当前选中的记录已经是第一条', 'info'); 
		}
	}
};

/**
 * 下移
 * @returns
 */
var downAuthority = function(){
	var rows = treeGrid.datagrid('getSelections');
	var num = rows.length;

	if (num == 0) {
		parent.$.messager.alert('提示', '请选择一条记录进行删除操作!', 'info'); 
		return;
	} else if (num > 1) {
		parent.$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行删除操作!', 'info'); 
		return;
	}else {
		var alterid = '';//需要和选中记录交换位置的node的id
		var alterindex = 9999;//需要和选中记录交换位置的node的dataindex
		$.each(treeGrid.treegrid('getChildren', rows[0].parentId), function(i, n) {
			if (n.parentId == rows[0].parentId && n.dataIndex > rows[0].dataIndex){
				if (n.dataIndex < alterindex){
					alterid = n.authorityId;
					alterindex = n.dataIndex;
				}
			}
		});
		if (alterid != ''){
			$.ajax({
				type:"post", //请求方式
				url:sy.contextPath+"moveAuthority.do", //发送请求地址
				data:{
			   		"authorityId1":alterid,
			   		"index1":alterindex,
			   		"authorityId2": rows[0].authorityId,
			   		"index2": rows[0].dataIndex,
				   },
				//请求成功后的回调函数有两个参数
				success:function(r){
					if(r.flag){
						parent.$.messager.alert('提示', r.msg, 'info');
						treeGrid.treegrid('load');
//						treeGrid.treegrid("expand",expandId);
					}else{
						parent.$.messager.alert('提示', r.msg, 'info');
					}
				}
				
				});
			
		}else{
			parent.$.messager.alert('提示', '无法下移，当前选中的记录已经是最后一条', 'info'); 
		}
	}
};
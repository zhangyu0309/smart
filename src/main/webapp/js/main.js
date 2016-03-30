var mainMenu;
var mainTabs;

$(function() {

	/**
	 * 系统退出
	 * 
	 * @author RM
	 * 
	 * @returns
	 */
	$("#loginOut").click(function() {
		parent.$.messager.confirm('询问', '您确定要退出系统吗？', function(r) {
			if (r) {
				window.location.href = sy.contextPath + 'loginOutAdmin.do';
			}
		});
	});
	/**
	 * 密码修改
	 * 
	 * @author RM
	 * 
	 * @returns
	 */
	$("#passwdChange").click(function() {
		parent.$.messager.confirm('询问', '您确定要修改密码吗？', function(r) {
			if (r) {
				if (r) {
					var dialog = parent.sy.modalDialog({
						title : '修改密码',
						width : 500,
						height : 250,
						url : sy.contextPath + '/beginAddOrUpdateUser.do?editType=100',
						buttons : [ {
							text : '修改',
							handler : function() {
								dialog.find('iframe').get(0).contentWindow.submitForm(dialog,
										 parent.$);
							}
						} ]
					});
				}
			}
		});
	});

	/**
	 * 获取该账号下的功能菜单
	 * 
	 * @author RM
	 * 
	 * @Returns
	 */
	mainMenu = $('#mainMenu')
			.tree(
					{
						url : sy.contextPath + '/getMenuAuthority.do',
						parentField : 'parentId',
						idField : 'authorityId',
						textField : 'authorityName',
						onLoadSuccess: function () {    //加载结束后收缩所有的节点
							$(this).tree("collapseAll"); 
                        }  ,
						onClick : function(node) {
							console.info(node);
							if(node.parentId){  //点击节点进行展开
								$(this).tree('expand',node.target);  
							}else{
								$(this).tree("collapseAll"); 
							    $(this).tree('expand',node.target);  
							}
							if (node.menuUrl) {
								var src =  node.menuUrl;
								if (!sy.startWith(node.menuUrl, '/')) {
									src = node.menuUrl;
									var tabs = $('#mainTabs');
									var opts = {
										title : node.authorityName,
										closable : true,
										iconCls : node.iconCls,
										content : sy
												.formatString(
														'<iframe src="{0}" allowTransparency="true" style="border:0;width:100%;height:99%;" frameBorder="0"></iframe>',
														src),
										border : false,
										fit : true
									};
								} else {
									var tabs = $('#mainTabs');
									var src = sy.basePath+node.menuUrl;
									var opts = {
										title : node.authorityName,
										closable : true,
										iconCls : node.iconCls,
										content : sy.formatString(
														'<iframe src="{0}" allowTransparency="true" style="border:0;width:100%;height:99%;" frameBorder="0"></iframe>',
														src),
										border : false,
										fit : true
									};
								}
								if (tabs.tabs('exists', opts.title)) {
									tabs.tabs('select', opts.title);
								} else {
									tabs.tabs('add', opts);
								}
							} else {
								// parent.$.messager.alert('提示', "
								// "+node.authorityName+'未绑定地址！', 'warn');
							}
						}
					});

	/**
	 * 控制整体排版布局整体居中
	 * 
	 * @author RM
	 * 
	 * 
	 */
	$('#mainLayout').layout('panel', 'center').panel(
			{
				onResize : function(width, height) {
					sy.setIframeHeight('centerIframe', $('#mainLayout').layout(
							'panel', 'center').panel('options').height - 5);
				}
			});
	/**
	 * 主页面Tab展示
	 * 
	 * @author RM
	 * 
	 * returns
	 */
	mainTabs = $('#mainTabs')
			.tabs(
					{
						fit : true,
						border : false,
						tools : [
								{
									text : '刷新',
									iconCls : 'ext-icon-arrow_refresh',
									handler : function() {
										var panel = mainTabs
												.tabs('getSelected').panel(
														'panel');
										var frame = panel.find('iframe');
										try {
											if (frame.length > 0) {
												for ( var i = 0; i < frame.length; i++) {
													frame[i].contentWindow.document
															.write('');
													frame[i].contentWindow
															.close();
													frame[i].src = frame[i].src;
												}
												if (navigator.userAgent
														.indexOf("MSIE") > 0) {// IE特有回收内存方法
													try {
														CollectGarbage();
													} catch (e) {
													}
												}
											}
										} catch (e) {
										}
									}
								}]
					});
});
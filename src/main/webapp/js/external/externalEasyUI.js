var sy = sy || {};
var catalog = catalog;
/**
 * 更改easyui加载panel时的提示文字
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.panel.defaults, {
	loadingMessage : '加载中....'
});
/**
 * 更改easyui加载grid时的提示文字
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.datagrid.defaults, {
	loadMsg : '数据加载中....'
});

/**
 * panel关闭时回收内存，主要用于layout使用iframe嵌入网页时的内存泄漏问题
 * 
 * @requires jQuery,EasyUI
 * 
 */
$.extend($.fn.panel.defaults, {
	onBeforeDestroy : function() {
		var frame = $('iframe', this);
		try {
			if (frame.length > 0) {
				for (var i = 0; i < frame.length; i++) {
					frame[i].src = '';
					frame[i].contentWindow.document.write('');
					frame[i].contentWindow.close();
				}
				frame.remove();
				if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
					try {
						CollectGarbage();
					} catch (e) {
					}
				}
			}
		} catch (e) {
		}
	}
});

/**
 * 防止panel/window/dialog组件超出浏览器边界
 * 
 * @requires jQuery,EasyUI
 */
sy.onMove = {
	onMove : function(left, top) {
		var l = left;
		var t = top;
		if (l < 1) {
			l = 1;
		}
		if (t < 1) {
			t = 1;
		}
		var width = parseInt($(this).parent().css('width')) + 14;
		var height = parseInt($(this).parent().css('height')) + 14;
		var right = l + width;
		var buttom = t + height;
		var browserWidth = $(window).width();
		var browserHeight = $(window).height();
		if (right > browserWidth) {
			l = browserWidth - width;
		}
		if (buttom > browserHeight) {
			t = browserHeight - height;
		}
		$(this).parent().css({/* 修正面板位置 */
			left : l,
			top : t
		});
	}
};
$.extend($.fn.dialog.defaults, sy.onMove);
$.extend($.fn.window.defaults, sy.onMove);
$.extend($.fn.panel.defaults, sy.onMove);

/**
 * 
 * 通用错误提示
 * 
 * 用于datagrid/treegrid/tree/combogrid/combobox/form加载数据出错时的操作
 * 
 * @requires jQuery,EasyUI
 */
sy.onLoadError = {
	onLoadError : function(XMLHttpRequest) {
		if (parent.$ && parent.$.messager) {
			parent.$.messager.progress('close');
			parent.$.messager.alert('错误', XMLHttpRequest.responseText);
		} else {
			$.messager.progress('close');
			$.messager.alert('错误', XMLHttpRequest.responseText);
		}
	}
};
$.extend($.fn.datagrid.defaults, sy.onLoadError);
$.extend($.fn.treegrid.defaults, sy.onLoadError);
$.extend($.fn.tree.defaults, sy.onLoadError);
$.extend($.fn.combogrid.defaults, sy.onLoadError);
$.extend($.fn.combobox.defaults, sy.onLoadError);
$.extend($.fn.form.defaults, sy.onLoadError);

/**
 * 扩展combobox在自动补全模式时，检查用户输入的字符是否存在于下拉框中，如果不存在则清空用户输入
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.combobox.defaults, {
	onShowPanel : function() {
		var _options = $(this).combobox('options');
		if (_options.mode == 'remote') {/* 如果是自动补全模式 */
			var _value = $(this).combobox('textbox').val();
			var _combobox = $(this);
			if (_value.length > 0) {
				$.post(_options.url, {
					q : _value
				}, function(result) {
					if (result && result.length > 0) {
						_combobox.combobox('loadData', result);
					}
				}, 'json');
			}
		}
	},
	onHidePanel : function() {
		var _options = $(this).combobox('options');
		if (_options.mode == 'remote') {/* 如果是自动补全模式 */
			var _data = $(this).combobox('getData');/* 下拉框所有选项 */
			var _value = $(this).combobox('getValue');/* 用户输入的值 */
			var _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */
			for (var i = 0; i < _data.length; i++) {
				if (_data[i][_options.valueField] == _value) {
					_b = true;
				}
			}
			if (!_b) {/* 如果在下拉列表中没找到用户输入的字符 */
				$(this).combobox('setValue', '');
			}
		}
	}
});

/**
 * 扩展combogrid在自动补全模式时，检查用户输入的字符是否存在于下拉框中，如果不存在则清空用户输入
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.combogrid.defaults, {
	onShowPanel : function() {
		var _options = $(this).combogrid('options');
		if (_options.mode == 'remote') {/* 如果是自动补全模式 */
			var _value = $(this).combogrid('textbox').val();
			if (_value.length > 0) {
				$(this).combogrid('grid').datagrid("load", {
					q : _value
				});
			}
		}
	},
	onHidePanel : function() {
		var _options = $(this).combogrid('options');
		if (_options.mode == 'remote') {/* 如果是自动补全模式 */
			var _data = $(this).combogrid('grid').datagrid('getData').rows;/* 下拉框所有选项 */
			var _value = $(this).combogrid('getValue');/* 用户输入的值 */
			var _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */
			for (var i = 0; i < _data.length; i++) {
				if (_data[i][_options.idField] == _value) {
					_b = true;
				}
			}
			if (!_b) {/* 如果在下拉列表中没找到用户输入的字符 */
				$(this).combogrid('setValue', '');
			}
		}
	}
});

/**
 * 扩展validatebox，添加新的验证功能
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.validatebox.defaults.rules, {
	eqPwd : {/* 验证两次密码是否一致功能 */
		validator : function(value, param) {
			return value == $(param[0]).val();
		},
		message : '密码不一致！'
	}
});

/**
 * 等待提示
 * 
 * @requires jQuery,EasyUI
 */
sy.progressBar = function(options) {
	if (typeof options == 'string') {
		console.log(options);
		if (options == 'close') {
			$('#syProgressBarDiv').dialog('destroy');
		}
		else{
			if ($('#syProgressBarDiv').length < 1) {
				var opts = $.extend({
					title : '&nbsp;',
					closable : false,
					width : 300,
					height : 60,
					modal : true,
					content : '<div id="syProgressBar" class="easyui-progressbar" data-options="value:0,text:\'正在处理，请稍候...\'"></div>'
				}, options);
				$('<div id="syProgressBarDiv"/>').dialog(opts);
				$.parser.parse('#syProgressBarDiv');
			} else {
				$('#syProgressBarDiv').dialog('open');
			}
			if (options.value) {
				$('#syProgressBar').progressbar('setValue', options.value);
			}
		}
	}
};
/**
 * 扩展tree和combotree，使其支持平滑数据格式
 * 
 * @requires jQuery,EasyUI
 * 
 */
sy.loadFilter = {
	loadFilter : function(data, parent) {
		var opt = $(this).data().tree.options;
		var idField, textField, parentField, valueField;
		if (opt.parentField) {
			idField = opt.idField || 'id' || 'authorityId';
			textField = opt.textField || 'name' ||'authorityName';
			parentField = opt.parentField || 'parentId' ||'pid';
			valueField = opt.valueField || 'id' || 'authorityId';
			var i, l, treeData = [], tmpMap = [];
			
			//组件改造
			//TODO: 刘磊@2016.04.06：待改造
			for (i = 0, l = data.length; i < l; i++) {
				var tempData = {};
				
				tempData = data[i];
				tempData.id = data[i].authorityId || data[i].id;
				tempData.text = data[i].authorityName || data[i].name || data[i].text;
				tmpMap[data[i][idField]] = tempData;
			}
			
			for (i = 0, l = data.length; i < l; i++) {
				if (tmpMap[data[i][parentField]] && data[i][idField] != data[i][parentField]) {
					if (!tmpMap[data[i][parentField]]['children'])
						tmpMap[data[i][parentField]]['children'] = [];
					data[i]['text'] = data[i][textField];
					tmpMap[data[i][parentField]]['children'].push(data[i]);
				} else {
					data[i]['text'] = data[i][textField];
					treeData.push(data[i]);
				}
			}
			
			console.info(treeData);
			
			return treeData;
		}
		
		return data;
	}
};
$.extend($.fn.combotree.defaults, sy.loadFilter);
$.extend($.fn.tree.defaults, sy.loadFilter);
/**
 * 扩展treegrid，使其支持平滑数据格式
 * 
 * @requires jQuery,EasyUI
 * 
 */
$.extend($.fn.treegrid.defaults, {
	loadFilter : function(data, parentId) {
		var opt = $(this).data().treegrid.options;
		var idField, treeField, parentField;
		if (opt.parentField) {
			idField = opt.idField || 'id' || 'authorityId';;
			treeField = opt.textField || 'name' ||'authorityName';
			parentField = opt.parentField || 'parentId' ||'pid';
			var i, l, treeData = [], tmpMap = [];
			for (i = 0, l = data.length; i < l; i++) {
				tmpMap[data[i][idField]] = data[i];
			}
			for (i = 0, l = data.length; i < l; i++) {
				if (tmpMap[data[i][parentField]] && data[i][idField] != data[i][parentField]) {
					if (!tmpMap[data[i][parentField]]['children'])
						tmpMap[data[i][parentField]]['children'] = [];
					data[i]['text'] = data[i][treeField];
					tmpMap[data[i][parentField]]['children'].push(data[i]);
				} else {
					data[i]['text'] = data[i][treeField];
					treeData.push(data[i]);
				}
			}
			return treeData;
		}
		return data;
	}
});
/**
 * 创建一个模式化的dialog
 * 
 * @requires jQuery,EasyUI
 * 
 */
sy.modalDialog = function(options) {
	var opts = $.extend({
		title : '&nbsp;',
		width : 640,
		height : 500,
		modal : true,
		onClose : function() {
			$(this).dialog('destroy');
		}
	}, options);
	opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
	if (options.url) {
		opts.content = '<iframe id="" src="' + options.url + '" allowTransparency="true" scrolling="auto" width="100%" height="98%" frameBorder="0" name=""></iframe>';
	}
	return $('<div/>').dialog(opts);
};
/**
 * 扩展  comboxtree 获取多个值
 * 
 * @requires jQuery,EasyUI
 */
//$.extend($.fn.treegrid.defaults.editors, {
//    combotree: {
//        init: function(container, options){
//            var editor = jQuery('<input type="text">').appendTo(container);
//            editor.combotree(options);
//            return editor;
//        },
//        destroy: function(target){
//            jQuery(target).combotree('destroy');
//        },
//        getValue: function(target){
//            var temp = jQuery(target).combotree('getValues');
//            //alert(temp);
//            return temp.join(',');
//        },
//        setValue: function(target, value){
//        	parent.$.messager.confirm('询问', '是否加载该项相关联的信息？', function(r) {
//    			if(r){
////    				console.info("false");
//    				if(value==undefined){
//                		 jQuery(target).combotree('setValues', "");
//                	  }else{
//                		 var temp = value.split(',');
//                   		 
//                         jQuery(target).combotree('setValues', temp);
//                	 }
//    			}else{
//          		  jQuery(target).combotree('setValues', "");
//          	  }  
//    	    });
//        	  /*ques=window.confirm("是否重置相关联的信息？");
//        	  if(ques){
//        		  if(value==undefined){
//             		 jQuery(target).combotree('setValues', "");
//             	  }else{
//             		 var temp = value.split(',');
//                		 
//                      jQuery(target).combotree('setValues', temp);
//             	  }
//        	  }else{
//        		  jQuery(target).combotree('setValues', "");
//        	  }      */    
//        },
//        resize: function(target, width){
//            jQuery(target).combotree('resize', width);
//        }
//}
//});
/**
 * 扩展combogrid 到Editor中
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.datagrid.defaults.editors, {
	combogrid: {
		init: function(container, options){
			var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container); 
			input.combogrid(options);
			return input;
		},
		destroy: function(target){
			$(target).combogrid('destroy');
		},
		getValue: function(target){
			var temp = $(target).combogrid('getValues');
			return temp.join(',');
		},
		setValue: function(target, value){
			if(value==undefined){
				$(target).combogrid('setValues', '');
			}else{
				var temp = value.split(',');
				$(target).combogrid('setValues', temp);
			}
			
		},
		resize: function(target, width){
			$(target).combogrid('resize',width);
		}
	}
});
/**
 * 扩展combogrid 到Editor中
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.datagrid.defaults.editors, {
	combo: {
		init: function(container, options){
			var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container); 
			input.combo(options);
			return input;
		},
		destroy: function(target){
			$(target).combo('destroy');
		},
		getValue: function(target){
			var temp = $(target).combo('getValues');
			return temp.join(',');
		},
		setValue: function(target, value){
			if(value==undefined){
				$(target).combo('setValues', '');
			}else{
				var temp = value.split(',');
				$(target).combo('setValues', temp);
			}
			
		},
		resize: function(target, width){
			$(target).combo('resize',width);
		}
	}
});

var sy = sy || {};
sy.data = sy.data || {};// 用于存放临时的数据或者对象

/**
 * 屏蔽右键
 * 
 * @author RM
 * 
 * @requires jQuery
 */
$(document).bind('contextmenu', function() {
	// return false;
});

/**
 * 禁止复制
 * 
 * @author RM
 * 
 * @requires jQuery
 */
$(document).bind('selectstart', function() {
	// return false;
});
/**
 * 增加formatString功能
 * 
 * @author RM
 * 
 * @example sy.formatString('字符串{0}字符串{1}字符串','第一个变量','第二个变量');
 * 
 * @returns 格式化后的字符串
 */
sy.formatString = function(str) {
	for (var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};

/**
 * 将form表单元素的值序列化成对象( 主要是将Form表单转换成对象)
 * 
 * @example sy.serializeObject($('#formId'))
 * 
 * @author RM
 * 
 * @requires jQuery
 * 
 * @returns object
 */
sy.serializeObject = function(form) {
	var o = {};
	$.each(form.serializeArray(), function(index) {
		if (this['value'] != undefined && this['value'].length > 0) {// 如果表单项的值非空，才进行序列化操作
			if (o[this['name']]) {
				o[this['name']] = o[this['name']] + "," + this['value'];
			} else {
				o[this['name']] = this['value'];
			}
		}
	});
	return o;
};

/**
 * 将一个带有，号的字符串全部转换成字符串类型的数组
 * 
 * @author RM
 * 
 * @returns list
 */
sy.stringToList = function(value) {
	if (value != undefined && value != '') {
		var values = [];
		var t = value.split(',');
		for (var i = 0; i < t.length; i++) {
			values.push('' + t[i]);/* 避免他将ID当成数字 */
		}
		return values;
	} else {
		return [];
	}
};

/**
 * 将JSON对象转换成String
 * 
 * @author RM
 * 
 * @param o
 * 
 * @returns
 */
sy.jsonToString = function(o) {
	var r = [];
	if (typeof o == "string")
		return "\"" + o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
	if (typeof o == "object") {
		if (!o.sort) {
			for ( var i in o)
				r.push(i + ":" + sy.jsonToString(o[i]));
			if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) {
				r.push("toString:" + o.toString.toString());
			}
			r = "{" + r.join() + "}";
		} else {
			for (var i = 0; i < o.length; i++)
				r.push(sy.jsonToString(o[i]));
			r = "[" + r.join() + "]";
		}
		return r;
	}
	return o.toString();
};

/**
 * 改变jQuery的AJAX中Post方法在请求错误后的方法
 * 
 * @author RM
 * 
 * @requires jQuery
 * 
 */
$.ajaxSetup({
	type : 'POST',
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		try {
			parent.$.messager.progress('close');
			parent.$.messager.alert('错误',"请求失败");
		} catch (e) {
			alert("请求失败");
		}
	}
});

/**
 * 解决class="iconImg"的img标记，没有src的时候，会出现边框问题
 * 
 * @author RM
 * 
 * @requires jQuery
 */
$(function() {
	$('.iconImg').attr('src', sy.imgTip);
});
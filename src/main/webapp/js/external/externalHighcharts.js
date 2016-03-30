var sy = sy || {};
/**
 * 修改HighChart中的导出图片显示为中文简体
 * 
 * @author RM 
 * 
 */
$.extend(Highcharts.getOptions().lang, {
	printChart : '打印图表',
	downloadPNG : '下载 PNG 图片',
	downloadJPEG : '下载 JPEG 图片',
	downloadPDF : '下载 PDF 文档',
	downloadSVG : '下载 SVG vector image',
	contextButtonTitle : '导出菜单'
});
/**
 * 复写图片下载工具的指定的方法
 * 
 * @author RM 
 * 
 */
$.extend(Highcharts.getOptions().exporting, {
	filename : 'chart',
	url : sy.contextPath + '/downloadChart'
});
/**
 * 使图标工具底部的来自HighCharts样式去掉
 * 
 * @author RM 
 * 
 */
$.extend(Highcharts.getOptions(), {
	credits : {
		enabled : false
	}
});
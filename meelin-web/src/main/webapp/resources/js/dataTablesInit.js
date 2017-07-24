var root= $("#root").val();
/**
 * DataTables默认参数初始化
 */
$.extend($.fn.DataTable.defaults, {
	"processing": true,	// 加载信息
	"serverSide": true,	// 开启服务器模式,由服务端获取数据
	"pageLength": 5,	// 每页显示条数
	"searching": false,	// 隐藏搜索框
	"stateSave": false,	// 允许浏览器缓存Datatables，以便下次恢复之前的状态
	"lengthChange": false, // 隐藏每页显示条数选项
	"ordering": false,	// 排序禁用
	"pagingType":   "full_numbers",
	"language": {
		"url": root+"/resources/plugin/datatables/i18n/zh_cn.lang"
	}
}
);
/**
 * 列表复选框初始化
 */
var _init= function(){
	//全选
	$("#checkedAll").on("click", function(){
		var trDom= $("input[name='checkList']");
		if($(this).prop("checked")) {
			trDom.prop("checked", $(this).prop("checked"));
			trDom.parents("tr").addClass("selected");
        } else {
        	trDom.prop("checked", false);
        	trDom.parents("tr").removeClass("selected");
        }
    });
};

//只读文本框禁用退格返回功能，浏览器为IE的情况下
document.documentElement.onkeydown = function(evt){
	var b = !!evt, oEvent = evt || window.event;
	if (oEvent.keyCode == 8) {
		var node = b ? oEvent.target : oEvent.srcElement;
		var reg = /^(input|textarea)$/i, regType = /^(text|textarea)$/i;
		if (!reg.test(node.nodeName) || !regType.test(node.type) || node.readOnly || node.disabled) {
			if (b) {
				oEvent.stopPropagation();
			}else{
				oEvent.cancelBubble = true;
				oEvent.keyCode = 0;
				oEvent.returnValue = false;
			}
		}
	}
}
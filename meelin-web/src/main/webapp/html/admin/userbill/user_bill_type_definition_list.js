var setting = {
	view: {
		dblClickExpand: false,
		showLine: false,
		selectedMulti: false,
		showIcon: true
	},
	async: {
		enable: true,
		autoParam:["id=parentId"], 
		url: getUrl,
		dataFilter: filter,
		type:"post", 
		expandSpeed : "fast"
	},
	check : {
		enable : false
	},
	data:{
		simpleData :{
			enable:true
		} 
	},
	callback: {
		onClick: onClick ,
		beforeClick: beforeClick,
		onExpand: zTreeOnExpand
	}
};

/**
 * 请求地址
 * 
 * @param treeId
 * @param treeNode
 * @returns {String}
 */
function getUrl(treeId, treeNode) {
	return root + "/admin/ub/typedef/resJson";
}

/**
 * 过滤
 * 
 * @param treeId
 * @param parentNode
 * @param childNodes
 * @returns
 */
function filter(treeId, parentNode, childNodes) {
	if (!childNodes) return null;
	for (var i=0, l = childNodes.length; i<l; i++) {
		childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
	}
	return childNodes;
}

/**
 * 单击事件
 * 
 * @param event
 * @param treeId
 * @param treeNode
 */
function onClick(event, treeId, treeNode) {
	var treeObj = $.fn.zTree.getZTreeObj("treeData");
	var sNodes = treeObj.getSelectedNodes();
	if (sNodes.length> 0) {
		if (!treeNode.isParent) {
			$.ajax({
				type : 'POST',
				url : root+ '/admin/ub/typedef/resJson',
				data : {'parentId' : treeNode.id},
				dataType : 'JSON',
				async : false,
				success : function(dat) {
					var dats = eval(dat);
					if (dats.length != 0) {
						treeObj.addNodes(treeNode, dats);
					}
				}
			});
		} else {
			treeObj.expandNode(treeNode);
		}
	}
}

/**
 * 单击前置事件
 * 
 * @param treeId
 * @param treeNode
 */
function beforeClick(treeId, treeNode) {
	// 设置父节点ID
	$("#parentId").val(treeNode.id);
	
	// 刷新列表
	$.dataModel.ajax.reload();
}

/**
 * 展开事件
 * 
 * @param event
 * @param treeId
 * @param treeNode
 */
function zTreeOnExpand(event, treeId, treeNode) {
	// 设置父节点ID
	$("#parentId").val(treeNode.id);
	
	// 刷新列表
	$.dataModel.ajax.reload();
};

/**
 * @param e
 */
function refreshNode(e) {
	var zTree = $.fn.zTree.getZTreeObj("treeData"),
	type = e.data.type,
	silent = e.data.silent,
	nodes = zTree.getSelectedNodes();
	if (nodes.length == 0) {
		layer.msg('请先选择一个父节点', {icon: 5});
	}
	for (var i=0, l=nodes.length; i<l; i++) {
		zTree.reAsyncChildNodes(nodes[i], type, silent);
		if (!silent) zTree.selectNode(nodes[i]);
	}
}


//表格初始化
var createTable= function(){
	var table= $("#listTable").DataTable({
		"pageLength": 20,	// 每页显示条数
		'bPaginate': true, //是否分页。
		"bProcessing": true, //当datatable获取数据时候是否显示正在处理提示信息。
		'bFilter': false, //是否使用内置的过滤功能。
		'bLengthChange': false, //是否允许用户自定义每页显示条数。
		'sPaginationType': 'full_numbers',      //分页样式
		"ajax": {
			"url": root+'/admin/ub/typedef/getNodeById',
			"type": 'POST',
			"data": function (d) {
				return $.extend({}, d, {//搜索条件
					"parentId": $("#parentId").val()
				});
			}
		},
		"columns": [
			{ "data": "id",
				"createdCell": function (nTd, sData, oData, iRow, iCol) {
					$(nTd).html("<input type='checkbox' name='checkList' value='" + sData + "'>");
				}
			},
			{ "data": "title" },
			{ "data": "code" },
			{ "data": "icon" },
			{ "data": "description"},
			{ "data": "enabled",
				"createdCell": function (nTd, sData, oData, iRow, iCol) {
					if(sData == '1'){
						$(nTd).html("启用");
					} else if(sData == '2'){
						$(nTd).html("禁用");
					} else{
						$(nTd).html("状态出错");
					}
				}
			}
		],
		"createdRow": function(nRow, aData, iDataIndex) {
			$(nRow).click(
				function() {
					$(this).toggleClass("selected");
					if ($(this).hasClass("selected")) {
						$("input[name='checkList']", $(this)).prop("checked", true);
					} else {
						$("input[name='checkList']", $(this)).prop("checked", false);
					}
			});
		},
		"initComplete": function (oSettings, json) {
			$("#addRes").click(_add); 
			$("#editRes").click(_edit);
			$("#refreshDict").click(_refresh);
			_init();	//初始化全选(取消)
		}
	});
	return table;
};

/**
 * 添加
 */
var _add= function(){
	var zTree = $.fn.zTree.getZTreeObj("treeData");
	var nodes = zTree.getSelectedNodes();
	if(nodes.length==0){
		layer.msg("请选择一个节点", {icon: 5});
		return false;
	}
	window.location.href = root+"/admin/ub/typedef/add?id=" + nodes[0].id;
};

/**
 * 编辑
 */
var _edit= function(){
	var arrs = new Array();
	var data = $.dataModel.rows("tr.selected").data();
	$.each(data, function(index,value){
		arrs.push(this.id);
	});
	if (arrs.length == 0 || arrs.length > 1) {
		layer.msg("请选择一条记录进行操作!", {icon: 5});
		return false;
	}
	window.location.href= root+"/admin/ub/typedef/edit?id="+arrs[0];
};

/**
 * 刷新字典缓存
 */
var _refresh = function() {
	$.ajax({
		url: root + "/admin/ub/typedef/refresh",
		type: "post",
		dataType: "json",
		success: function(data){
			var obj = $.parseJSON(JSON.stringify(data));
			if (obj.state) {
				var treeObj = $.fn.zTree.getZTreeObj("treeData");
				treeObj.refresh();
				
				// 初始化目录树
				$.fn.zTree.init($("#treeData"), setting);
				layer.msg(obj.msg, {icon: 1});
			} else {
				layer.msg(obj.msg, {icon: 2});
			}
		},
		error: function() {
			layer.msg('数据请求出现异常，请刷新重试!', {icon: 2});
		}
	});
};

/**
 * 初始化组件
 */
$(document).ready(function(){
	// 初始化目录树
	$.fn.zTree.init($("#treeData"), setting);
	// 初始化分页数据
	$.dataModel= createTable();
});

var setting = {
	async: {
		enable: true,
		url: root+"/admin/resource/resJson"
	},
	callback: {
		beforeClick: beforeClick
	}
};
function beforeClick(treeId, treeNode) {
	$("#rspId").val(treeNode.id);
	$.dataModel.ajax.reload();
}

$(document).ready(function(){
	$.dataModel= createTable();
	$.fn.zTree.init($("#treeDemo"), setting);
	$("#submit").click(function(){
		$.ajax({
            cache: true,
            type: "POST",
            url: root+"/admin/res/saveNode.html",
            data: $("#commentForm").serialize(),//formid
            async: false,
            error: function(request) {
                alert("Connection error");
            },
            success: function(data) {
            	$.fn.zTree.init($("#treeDemo"), setting);
            }
        });
	});
});


//表格初始化
var createTable= function(){
	var table= $("#listTable").DataTable({
		"pageLength": 200,	// 每页显示条数
        "ajax": {
        	"url": root+'/admin/resource/getResById',
            "type": 'POST',
            "data": function (d) {
				return $.extend({}, d, {//搜索条件
              		"rspId": $("#rspId").val()
				});
			}
        },
        "columns": [
			{ "data": "rsId",
				"createdCell": function (nTd, sData, oData, iRow, iCol) {
                    $(nTd).html("<input type='checkbox' name='checkList' value='" + sData + "'>");
                }
			},
			{ "data": "name" },
		    //{ "data": "rsKey" },
			{ "data": "type",
				"createdCell": function (nTd, sData, oData, iRow, iCol) {
					if(sData == '1'){
						$(nTd).html("导航");
					}else if(sData == '2'){
						$(nTd).html("目录");
					}else if(sData == '3'){
						$(nTd).html("按钮");
					}else{
						$(nTd).html("状态出错");
					}
            	}
			},
		    { "data": "uri" },
		    { "data": "sort" },
			{ "data": "enabled",
				"createdCell": function (nTd, sData, oData, iRow, iCol) {
					if(sData == '1'){
						$(nTd).html("启用");
					}else if(sData == '2'){
						$(nTd).html("禁用");
					}else{
						$(nTd).html("状态出错");
					}
            	}
			}
		],
		"createdRow": function(nRow, aData, iDataIndex) {
            //add selected class
            $(nRow).click(function () {
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
			$("#deleteRes").click(_delete);
			_init();	//初始化全选(取消)
       	}
	});
	return table;
};

var _add= function(){
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = zTree.getSelectedNodes();
	if(nodes.length==0){
		layer.msg("请选择一个节点");
		return false;
	}
	alert(nodes[0].id);
	window.location.href= root+"/admin/resource/add?rsId="+nodes[0].id;
};

var _edit= function(){
	var arrs=new Array();
	var data = $.dataModel.rows("tr.selected").data();
	$.each(data, function(index,value){
		arrs.push(this.rsId);
	});
	if(arrs.length==0 ||arrs.length> 1){
		layer.msg("请选择一条记录进行操作!");
    	return false;
    }
	window.location.href= root+"/admin/resource/edit?rsId="+arrs[0];
};

var _delete= function(e, treeId, treeNode){
	var arrs=new Array();
	var data = $.dataModel.rows("tr.selected").data();
	$.each(data, function(index,value){
		arrs.push(this.rsId);
	});
	if(arrs.length==0 ||arrs.length> 1){
		layer.msg("请选择一条记录进行操作!");
    	return false;
    }
	
	layer.confirm("确认删除 节点 -- " + data[0].name + " 吗？", {icon: 3}, function(index){
	    $.ajax({
		 type: "POST",
         url: root+"/admin/resource/remove",
         data: {rsId: arrs[0], rspId: arrs[0]},
         dataType: "json",
         success: function(data){
        	 if(data.state){
        		 layer.msg(data.msg);
        		 $.fn.zTree.init($("#treeDemo"), setting);
        		 $.dataModel.ajax.reload();
        	 }else{
        		 layer.msg(data.msg);
        	 }
        	
         }
	})});
};
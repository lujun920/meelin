$(document).ready(function(){
	$.dataModel= createTable();
	
});
//表格初始化
var createTable= function(){
	var table= $("#listTable").DataTable({
        "ajax": {
        	"url": root+'/admin/role/list',
            "type": 'POST',
            "data": function (d) {
				return $.extend({}, d, {//搜索条件
//              		"rspId": $("#rspId").val()
				});
			}
        },
        "columns": [
			{ "data": "roleId",
				"createdCell": function (nTd, sData, oData, iRow, iCol) {
                    $(nTd).html("<input type='checkbox' name='checkList' value='" + sData + "'>");
                }
			},
			{ "data": "roleId" },
			{ "data": "name" },
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
			$("#addRole").click(_add); 
			$("#editRole").click(_edit);
			$("#deleteRole").click(_delete);
			$("#authorityRole").click(_authority);
			_init();	//初始化全选(取消)
       	}
	});
	return table;
};

var _add= function(){
	window.location.href= root+"/admin/role/add";
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
	window.location.href= root+"/admin/role/edit?roleId="+arrs[0];
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

//权限分配
var _authority= function(){
	var arrs=new Array();
	var data = $.dataModel.rows("tr.selected").data();
	$.each(data, function(index,value){
		arrs.push(this.roleId);
	});
	if(arrs.length==0){
		layer.msg("请选择一条记录进行操作!");
    	return false;
    }
    layer.open({
        type : 2,	//0：信息框（默认），1：页面层，2：iframe层，3：加载层，4：tips层
        title: "权限分配",
        shadeClose: false,//用来控制点击遮罩区域是否关闭层
        fix: false,  		//用于设定层是否不随滚动条而滚动，固定在可视区域。
        area: ["300px", "600px"],
        content: [root+"/admin/role/authority?roleId="+arrs[0], "no"], //iframe的url，no代表不显示滚动条
        end: function(index){
        	$.dataModel.ajax.reload();
        }
    });
};
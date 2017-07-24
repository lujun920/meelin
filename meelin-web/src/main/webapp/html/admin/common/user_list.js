$(document).ready(function(){
	$.dataModel= createTable();
});
//表格初始化
var createTable= function(){
	var getSliderFn = $.fn.dataTable.slide()
	var table= $("#listTable").DataTable({
        "ajax": {
        	"url": root+'/admin/user/list',
            "type": 'POST',
            "data": function (d) {
				return $.extend({}, d, {//搜索条件
//              		"rspId": $("#rspId").val()
					username:$('#userNameSearch').val(),
					status:$('#userStatus').val()
				});
			}
        },
        "columns": [
			{ "data": "userId",
				"createdCell": function (nTd, sData, oData, iRow, iCol) {
                    $(nTd).html("<input type='checkbox' name='checkList' value='" + sData + "'>");
                }
			},
			{ "data": "username",
				"createdCell": function (nTd, sData, oData, iRow, iCol) {
                    $(nTd).html("<span id='slideClick' style='cursor:pointer'>" + sData + "</span>");
                }
			},
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
            $(nRow).find('#slideClick').click(function(event){
            	event.stopPropagation()
            	getSliderFn(aData,['username','userId','password'])//如果要显示全部属性则第2个参数传递true
            })
        },
		"initComplete": function (oSettings, json) {
			$("#addUser").click(_add); 
			$("#editUser").click(_edit);
			$("#enabledUser").click(_enabled);
			$("#disabledUser").click(_disabled);
			_init();	//初始化全选(取消)
			_comboSearch();
       	}
	});
	return table;
};

var _add= function(){
	window.location.href= root+"/admin/user/add";
};

var _edit= function(){
	var arrs=new Array();
	var data = $.dataModel.rows("tr.selected").data();
	$.each(data, function(index,value){
		arrs.push(this.userId);
	});
	if(arrs.length==0 ||arrs.length> 1){
		layer.msg("请选择一条记录进行操作!");
    	return false;
    }
	window.location.href= root+"/admin/user/edit?userId="+arrs[0];
};

var _enabled= function(){
	var arrs = new Array();
	var data = $.dataModel.rows("tr.selected").data();
	$.each(data, function(index, value) {
		arrs.push(this.userId);
	});
	if (arrs.length == 0) {
		layer.msg("请至少选择一条记录进行操作!");
		return false;
	}
	layer.confirm("确认启用吗？", {icon: 3}, function(index){
		$.ajax({
			type : "POST",
			traditional : true,
			url : root + "/admin/user/updateStatusToEnabled",
			data : {
				intList : arrs
			},
			dataType : "json",
			success : function(data) {
				if (data.state) {
					layer.msg(data.msg);
					$("#checkedAll").prop("checked", false);
					$.dataModel.ajax.reload();
				} else {
					layer.msg(data.msg);
				}
			}
		})
	});
};

var _disabled = function(){
	var arrs = new Array();
	var data = $.dataModel.rows("tr.selected").data();
	$.each(data, function(index, value) {
		arrs.push(this.userId);
	});
	if (arrs.length == 0) {
		layer.msg("请至少选择一条记录进行操作!");
		return false;
	}
	layer.confirm("确认禁用吗？", {icon: 3}, function(index){
		$.ajax({
			type : "POST",
			traditional : true,
			url : root + "/admin/user/updateStatusToDisabled",
			data : {
				intList : arrs
			},
			dataType : "json",
			success : function(data) {
				if (data.state) {
					layer.msg(data.msg);
					$("#checkedAll").prop("checked", false);
					$.dataModel.ajax.reload();
				} else {
					layer.msg(data.msg);
				}
			}
		})
	});
};

var _comboSearch = function(){
	$('#comboSerachBtn').click(function(){
		$.dataModel.ajax.reload(); 
	})
}

$(document).ready(function(){
	$.dataModel= createTable();
	// 表格初始化完成后才能获取表格对象,查询事件这样写
	$("#btnType").bind("click", function () { // 按钮 触发table重新请求服务器
		$.dataModel.ajax.reload();
    });
});

// 表格初始化
var createTable= function(){
	var table= $("#listTable").DataTable({
        "ajax": {
        	"url": root+'/user/listmap',
            "type": 'POST',
            "data": function (d) {
				return $.extend({}, d, {// 搜索条件
					//"ILC":[3,4]
				});
			}
        },
        "columns": [
			{ "data": "userId",
				"createdCell": function (nTd, sData, oData, iRow, iCol) {
                    $(nTd).html("<input type='checkbox' name='checkList' value='" + sData + "'>");
                }
			},
			{ "data": "userId",
				"createdCell": function (nTd, sData, oData, iRow, iCol) {
                    $(nTd).html(sData+"<i class='icon-eye-open'></i>");
                }
			},
		    { "data": "username"},
		    { "data": "password" },
		    { "data": "status" }
		],
		"createdRow": function(nRow, aData, iDataIndex) {
            // add selected class
            $(nRow).click(function () {
            	$(this).toggleClass("selected");
                if ($(this).hasClass("selected")) {
                    $("input[name='checkList']", $(this)).prop("checked", true);
                } else {
                    $("input[name='checkList']", $(this)).prop("checked", false);
                } 
            });
           /* $(nRow).dblclick(function () {
            	alert("1");
            });*/

        },
		"dom": "<'row-fluid'<'span6 myBtnBox'><'span6'f>r>t<'row-fluid'<'span6'i><'span6 'p>>",
		"initComplete": function (oSettings, json) {
			$("#editCustomer").click(_edit);
			$("#deleteLoanee").click(_delete);
			_init();	// 初始化全选(取消)
       	}
	});
	return table;
};

var _edit= function(){
	var arrs=new Array();
	var data = $.dataModel.rows("tr.selected").data();
	$.each(data, function(index,value){
		arrs.push(this.loaneeId);
	});
	if (arrs.length == 0 || arrs.length>1) {
		layer.msg("请选择一条记录进行操作!");
		return false;
	}
	window.location.href= root+"/admin/xhjd/getcar.html?repage=cardcl&loaneeId="+arrs[0];
	
	/*
	 * layer.open({ type : 2, //0：信息框（默认），1：页面层，2：iframe层，3：加载层，4：tips层 title:
	 * "新增", shadeClose: false,//用来控制点击遮罩区域是否关闭层 //maxmin: true, fix: false,
	 * //用于设定层是否不随滚动条而滚动，固定在可视区域。 area: ["820px", "500px"], content:
	 * [root+"/admin/role/getRole.html?roleId="+arrs[0], "no"],
	 * //iframe的url，no代表不显示滚动条 end: function(index){ $.dataModel.ajax.reload(); }
	 * });
	 */
   

};
var _delete= function(){
	var arrs=new Array();
	var data = $.dataModel.rows("tr.selected").data();
	$.each(data, function(index,value){
		arrs.push(this.roleId);
	});
	if (arrs.length == 0 || arrs.length>1) {
		layer.msg("请选择一条记录进行操作!");
		return false;
	}
    layer.confirm('确定要删除所选记录?', {icon: 3}, function(index){
	    $.ajax({
			 type: "POST",
			 traditional:true,
             url: root+"/admin/role/deleteRole.thml",
             data: {id: arrs[0]},
             dataType: "json",
             success: function(data){
            	 $.dataModel.ajax.reload();
            	 layer.close(index);
             }
		});
	});
};

$(document).ready(function() {
	//关闭iframe
	$("#goBack").click(function() {
		window.location.href = root + "/admin/ub/typedef/list";
	});
	
	/**
	 * 异步校验 
	 * */
	function GetRemoteInfo(postUrl, data) {
		var remote = {
			type : "POST",
			async : true,
			url : postUrl,
			dataType : "json",
			data : data,
			dataFilter : function(data) {
				var obj = $.parseJSON(data);
				if (obj.state) {
					return false;
				}
				return true;
			}
		};
		return remote;
	}
	
	var dataInfo ={code:function(){return $("#code").val();}, id: function(){return $("#id").val();}};
	var remoteInfo = GetRemoteInfo(root + '/admin/ub/typedef/checkCode?time=' + (new Date()).getTime(), dataInfo);
	
	// 表单验证
	var validate = $("#subForm").validate({
		submitHandler : function(form) {
			$("#subForm").attr("action",  root + "/admin/ub/typedef/save");
		},
		errorClass: "unchecked",
		validClass: "checked",
		errorElement: "span",
		errorPlacement:function(error,element){
			var s = element.parent().find("span[htmlFor='" + element.attr("id") + "']");
			if(s != null){
				s.remove();
			}
			error.appendTo(element.parent());
		},
		rules : {
			title : {
				required : true,
				maxlength : 50
			},
			code : {
				required : true,
				maxlength : 100,
				remote: remoteInfo
			}
		},
		messages : {
			title : {
				required : "字典名称不能为空"
			},
			code : {
				required : "字典编号不能为空",
				remote : '字典编号已存在'
			}
		}
	});
	// 初始化编辑器
	KindEditor.ready(function(K) {
		K.create('#description', {
			height:'120px',
			uploadJson : root + '/resources/plugin/kindeditor-4.1.10/jsp/upload_json.jsp',
			fileManagerJson : root + '/resources/plugin/kindeditor-4.1.10/jsp/file_manager_json.jsp',
			allowFileManager : true,
			afterBlur: function () {this.sync();},
			items : ['fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
					'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
					'insertunorderedlist', '|', 'emoticons', 'image', 'link']
		});
	});
	
	
	// ajax form 提交表单 
	$("#subForm").submit(function() {
		var id = $("#id").val();
		$("#subForm").ajaxSubmit({
			url: root + "/admin/ub/typedef/save",
			dataType: "json",
			type: "post",
			beforeSubmit: function(formData,jqForm,options) {
				return $("#subForm").valid();
			},
			success: function (data) {
					var obj = $.parseJSON(JSON.stringify(data));
					// 清空表单
					validate.resetForm();
					// 添加
					if (null == id || '' == id) {
						if (obj.state) {
							//询问框
							layer.confirm('已添加成功，是否要继续？', {
								btn: ['确定','取消'] //按钮
							}, function(){
								 window.location.reload();
							}, function(){
								window.location.href = root + "/admin/ub/typedef/list";
							});
						}
					} else {
						window.location.href = root + "/admin/ub/typedef/list";
					}
			}
		});
		return false; //此处必须返回false，阻止常规的form提交
	});
});

;(function($){
	$.fn.autoComplete =  function(opt){
		$(this).addClass('btn-default')
		var queryobj = {}, flag = opt.flag || false, placeHolder = $(this).attr('placeholder')?$(this).attr('placeholder'):'请选择'
		this.parent().css({'position':'relative'})
		var that = this, width = this.innerWidth(), font_size = this.css('font-size'),font_size_num = font_size.substring(0,font_size.indexOf('px')),dataLength = 0
			color = this.css('color')

		this.on('change', function(){
			if($(that).val().trim() ==='') {
				$('#jqautocomplete').remove()
				return
			}
			var html = '<div class="auto-wrap-div" id="jqautocomplete" style="background-color:#fff;padding-top:5px;padding-bottom:5px;z-index:100;position:absolute;"><ul style="text-align:center;overflow-y:auto;' + 
						'background-color:#fff; color:' + color + ';width:' + width +'px; font-size:' + font_size + ';cursor:pointer;">'
			$('#jqautocomplete').remove()
			var queryStr = that.val()
			queryobj[opt.param] = queryStr
			$.ajax({
				url: opt.url,
				type:'POST',
				data:queryobj,
				success:function(data){
					dataLength = data.length
					var li = '<li class="" id="autoTipNoQuery" style="line-height:'+ font_size_num*2 +'px;background-color:#495D74;color:#fff">' + placeHolder + '</li>'
					
					for(var i = 0, length = data.length ; i<length; i++ ){
						li += '<li class="jqAutoLi" ' 
						for(var x in opt.showParam){
							if(data[i][opt.showParam[x]]){
								li += ('data-' + opt.showParam[x] + ' = ' + data[i][opt.showParam[x]]) + ' '
							}
						}
						if(flag && !isContain(opt.cbParam, opt.showParam)){
							li += ('data-' + opt.cbParam + ' = ' + data[i][opt.cbParam]) + ' '
						}
						li += ' style="width:100%; line-height:' + font_size_num*2 + 'px;text-align:center;color:#333">' + data[i].username + '</li>'
					}
					html += li
					html += '</ul></div>'
					that.parent().append(html)
					if(dataLength>10) dataLength = 10
					$('#jqautocomplete').css({'height':(font_size_num*2)*(dataLength+1)+2 + 10 +'px'})
					addListener()
					
				},
				dataType:'json'
			})
			
		})
//		this.blur(function(){
//			$('#jqautocomplete').remove()
//		})
		function addListener(){
			$('.jqAutoLi').on('mouseover', function(){
				$(this).css({'background-color':'#495D74'})
				$(this).css({'color':'#fff'})
			}).on('mouseout', function(){
				$(this).css({'background-color':'#fff'})
				$(this).css({'color':'#333'})
			}).on('click', function(){
				that.val($(this).html())
				opt.callback(this)
				$('#jqautocomplete').remove()
				for(var y in opt.showBox){
					$('#' + opt.showBox[y]).val($(this).data(opt.showParam[y].toLowerCase()))
				}
				if(!flag || $(this).attr('id')==='autoTipNoQuery') return
				cbAjax(this, opt.cbParam)
			})
		}
		
		function cbAjax(_this, param){
			var restStr = $(_this).data(param.toLowerCase())
			$.ajax({
				url: opt.cbUrl + restStr,
				type:'GET',
//				data:queryobj,
				success:function(data){
					
					if(typeof data !== 'object'){//如果返回的不是数组（是字符串或者数字），则默认填入cbShowBox[0]
						$('#'+opt.cbShowBox[0]).val(data)
					}else{
						for(var z in opt.cbShowParam){
							if(data[opt.cbShowParam[z]]){
								$('#'+opt.cbShowBox[z]).val(data[opt.cbShowParam[z]])
							}
						}
					}
					
				},
				dataType:'json'
			})
		}
		
		function isContain(str, array){
			for(var x in array){
				if(array[x] === str) return true
			}
			return false
		}
	}
})(jQuery)
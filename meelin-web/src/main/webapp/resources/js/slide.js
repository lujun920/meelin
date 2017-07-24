$.fn.dataTable.slide = function(){
	var isSlided = false
	$('#slider').click(function(){
		$(this).animate({'right':'-50%'},400)
		isSlided = false
	})
	return function (data,colArray){
				var html = '', tbody = $('#slider-tbody'),sliderEle = $('#slider')
				tbody.children().remove()
				
				if(Array.prototype.splice.call(arguments,0)[1] === true){
					for(a in data){
						html +='<tr><td>'+ a + '</td><td>' +data[a] + '</td></tr>'
					}
				}else{
					for(a in colArray){
						html +='<tr><td>'+ colArray[a] + '</td><td>' +data[colArray[a]] + '</td></tr>'
					}
				}
				tbody.append(html)
				if(!isSlided){
					sliderEle.animate({'right':'0%'},400)
					isSlided = true
				}
		}
} 
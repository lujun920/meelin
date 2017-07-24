<#macro button key="" id="" name="" class="btn btn-primary btn-sm" style="" href="javascript:;" title="" disabled="" onclick="" text="">
	<#if key!="">
		<#list RES_RESOURCES as res><#rt/>
			<#if res['uri']==key>
			<a type="button"<#rt/>
			<#if id!=""> id="${id}"</#if><#rt/>
			<#if name!=""> name="${name}"</#if><#rt/>
			<#if class!=""> class="${class}"</#if><#rt/>
			<#if style!=""> style="${style}"</#if><#rt/>
			<#if title!=""> title="${title}"</#if><#rt/>
			<#if disabled!=""> disabled="${disabled}"</#if><#rt/>
			<#if onclick!=""> onclick="${onclick}"</#if><#rt/>
			>${text}</a><#rt/>
			</#if>
		</#list>
	<#else>
		key不能为空
	</#if>
</#macro>
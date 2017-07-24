<#ftl strip_whitespace=true>

<#--
 * base.getContextPath() as root
 *
 -->
<#macro root>${base.getContextPath()}</#macro>

<#--    -->
<#macro sdate>${.now?string("yyyyMMdd")}</#macro>

<#--  FTP  -->
<#macro ftp>${www_ftp!}</#macro>
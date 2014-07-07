<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]>
<#include "_menuMacros.ftl"/>

<#import "_macro.ftl" as macro/>

<ul id="menu" class="clearfix">
	<li><a href="${model.contextPath}/index.html">首页</a></li>
	<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<li><@macro.addSubRubric model.site.rootRubric.children[0]/></li>
	</@sec.authorize>
	<#list model.site.rootRubric.children[0].children as rubric>
		<#if canDrawInMenu(rubric)>
			<li><@macro.deleteRubric rubric/><@macro.drawRubricLink rubric/></li>
		</#if>
	</#list>
</ul>


<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>
<#import "_macro.ftl" as macro/>

<#macro navpath rubric>
	<span id="navpath"><@concatNavPath rubric /></span>
</#macro>

<#macro concatNavPath rubric>
	<#if rubric?? >
		 <#if rubric.parent?? >
		 	<#if rubric.parent.parent?? >
		 		<@concatNavPath rubric.parent />
		 	<#else/>
		 		<a href="${model.contextPath}/index.html"><@spring.message code="cms.first.page"/></a>
		 	</#if>
		 </#if> &gt; <@macro.drawRubricLink rubric/>
	<#else/>
		&nbsp;
	</#if>
</#macro>
<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>

<#import "_macro.ftl" as macro/>

<#macro drawArticle article drawTitle=true>
		<#if drawTitle &&(model.isAdmin?? || (article.title?? && article.title?trim?length > 0))>
			<h2 class="edit" id="article_title_${article.id}">${article.title?html}</h2>
		</#if>
		<#if model.isAdmin??>
			<@macro.articleControls article/>
		</#if>
	<#if model.isAdmin?? || (article.date?? && article.date?trim?length > 0)>
		<p class="edit article_date text" id="article_date_${article.id}">${article.date!""}</p>
	</#if>
	
	<div class="article text" id="article_body_${article.id}" <#if model.isAdmin??>onclick="convertToEditor(this);return null;"</#if>>${article.body}<#if article.body == ""><#if model.isAdmin??>(<@spring.message code="cms.clickHere"/>)<#else>(<@spring.message code="cms.noinfo"/>)</#if></#if></div>
</#macro>

<#-- 显示消息文章 -->
<#macro showArticleListEntry article isArchive>
	<#local class=""/>
	<#if model.currentRubric?? && article.id == model.currentRubric.id>
		<#local class="current"/>
	</#if>
	<a class="${class}" href="${model.contextPath}/${article.id}.html">${article.title?html}<#if '' == article.title><@spring.message code="cms.noname" /></#if></a>
	<#if model.isAdmin??>
		<#if !article.isOnline()> [离线]</#if>	
		<#if !isArchive>
			<#if article.isPending()> [正在编辑]</#if>
		</#if>
	</#if>	
</#macro>
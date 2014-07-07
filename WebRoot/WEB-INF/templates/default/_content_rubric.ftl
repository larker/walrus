<#include "_article.ftl"/>
<#include "_rubric.ftl"/>
<#include "_navpath.ftl"/>
<#import "_macro.ftl" as macro/>

<div id="rubric">
	<#--  管理员显示导航 -->
	<#if model.isAdmin??> 
		<@navpath model.currentRubric />--> 
	</#if>
	<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<#if !model.currentRubric.leaf && model.currentRubric.parent?? && (!model.currentRubric.parent.parent?? || !model.currentRubric.parent.parent.parent?? || !model.currentRubric.parent.parent.parent.parent??)>
			<@macro.addSubRubric model.currentRubric/>
		</#if>
	</@sec.authorize>
	<#if model.isAdmin?? && (model.currentRubric != model.site.rootRubric)>
		<@macro.rubricControls model.currentRubric/>
	</#if>
	<p>&nbsp;</p>
	<@drawRubric model.currentRubric />
</div>
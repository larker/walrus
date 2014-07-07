<#import "_macro.ftl" as macro/>

<#macro parseSubrubrics parent>
	<#if parent.children?? && (parent.children?size > 0)>
		<#list parent.children as rubric>
			<#if canDrawInMenu(rubric)>
				<#local drawSubMenu = true/>
			</#if>
		</#list>
		<#if drawSubMenu??>		
			<ul>
			<@drawMenuItems parent.children/>
			</ul>
		</#if>
	</#if>
</#macro>

<#function canDrawInMenu rubric>
	<#return macro.canShowInList(rubric, false) && (!rubric.leaf || model.isAdmin??)/>
</#function>

<#macro drawMenuItems items>
	<#list items as rubric>
		<#if canDrawInMenu(rubric)>
			<li class="<#if !rubric_has_next>last </#if><@macro.activeClass rubric />"><@macro.deleteRubric rubric/><@macro.drawRubricLink rubric/><@parseSubrubrics rubric/></li>
		</#if>
	</#list>
</#macro>

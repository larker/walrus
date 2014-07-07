<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>
<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]/>
<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>
<#import "_box.ftl" as box/>
<#import "_macro.ftl" as macro/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=9"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name ="keywords" content="java,spring, hibernamte, mysql,开源,免费,CMS,cms,内容管理,免费建站">
	<title>CMS <@tiles.insertAttribute name="title"/></title>
	<script src="${model.contextPath}/libs/jquery/jquery-1.10.2.min.js"></script>
	<script src="${model.contextPath}/libs/jquery/jquery-migrate-1.2.1.js"></script>
	<script>var ctx = '${model.contextPath}';</script>
	<@macro.adminScripts />
	
	<#if model.isAdmin??>
		<script src="${model.contextPath}/libs/js/slides-admin.js"></script>
	</#if>

	<#if model.currentRubric == model.site.rootRubric>
		<script>
			//<![CDATA[
				<@box.prepareSlideshow 'indexSlides' />
			//]]>
		</script>	
		<script src="${model.contextPath}/libs/js/slides.js"></script>
		<#if model.isAdmin??>
			<script src="${model.contextPath}/libs/js/slides-admin.js"></script>
		</#if>
	</#if>

	<link rel="shortcut icon" href="${model.contextPath}/favicon.ico" type="image/x-icon">
	<link rel="icon" href="${model.contextPath}/favicon.ico" type="image/x-icon">
	
	<link rel="stylesheet"  href="${model.contextPath}/styles/reset.css" type="text/css" media="screen"/>
	<link rel="stylesheet"  href="${model.contextPath}/styles/main.css" type="text/css" media="screen"/>
		
</head>

<body>
	
<div id="wrapper" class="clearfix">
			<div id="header">
				<div class="clearfix">
					<#if model.isAdmin??>
						<form>
							<input type="radio" name="flag" onclick="setCacheDisabled(false)" <#if false == model.isCacheDisabled>checked</#if>/>启用页面缓存
							<input type="radio" name="flag" onclick="setCacheDisabled(true)" <#if true == model.isCacheDisabled>checked</#if>/>禁用页面缓存
							<font color='red'>登录后，默认禁用页面缓存！完成网站内容编辑后，请重新启用页面缓存。</font>
						</form>
					</#if>
					<img src="${model.contextPath}/img/logo.gif" width="29" height="29" id="logo" />
					<#include "_topMenu.ftl"/>
				</div>
				<#if model.isAdmin??>
					<div id="header-content">
						<div class="clearfix">
	
							<div id="short-about">
								<@box.showTextBox "topTextBox1" true/>
							</div>
							<@box.showImageBox "topImageBox1"/>
						</div>
					</div>
				</#if>
			</div>

			<div id="main" class="clearfix">

				<div id="content">
					<div id="mainContent">
						<@tiles.insertAttribute name="content"/>
					</div>
				</div>
				
				<div id="right-sidebar" class="sidebar">

					<div class="block green list invisible">
						<div class="rounded-corners">
							<div class="rounded-corners clearfix">
								<@box.showNewsBox "cultureBox" true/>
							</div>
						</div>
					</div>
					
					<div class="block green mix">
						<div class="rounded-corners">
							<div class="rounded-corners clearfix">
							<@box.showTextBox "ultraTextBox2" true/>
							</div>
						</div>
					</div>
					<div class="block commercial">
						<@box.showBannerBox "rightBanner1"/>
						<@box.showBannerBox "rightBanner2"/>
						<@box.showBannerBox "rightBanner3"/>
					</div>
				</div>
			</div><!--main-->			
			
			<div id="footer" class="clearfix">
				<p><@macro.loginLogout /> 
			</div>			
			
			<@sec.authorize ifAllGranted="ROLE_ADMIN">
				<div id="errorBox">
					<h2>出现错误...</h2>
					<p id="error"></p>
					<p><a href="#" onclick="jQuery('#errorBox').hide(); return false;">OK</a></p>
				</div>
			</@sec.authorize>
</div> <!--wrapper -->
<@sec.authorize ifAllGranted="ROLE_ADMIN">
	<@tiles.insertAttribute name="bottomControls" />
</@sec.authorize>	
</body>
</html>

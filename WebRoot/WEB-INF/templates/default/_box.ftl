<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]/>
<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>
<#import "_macro.ftl" as macro />
<#include "_menuMacros.ftl"/>

<#function findBox list boxId>
	<#list list as box>
		<#if box.boxId == boxId>
			<#return box/>
		</#if>
	</#list>
</#function>

<#macro showNewsBox boxId showTitle=true>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)??>
			<#assign box = findBox(model.site.boxes, boxId)/> 
			<#if box?? && box.rubric??>
				<#if showTitle>
					<h2>${box.rubric.title}</h2>
				</#if>
				<#if box.rubric.children??>
				<ul id="box_${boxId}">
					<@sec.authorize ifAllGranted="ROLE_ADMIN">
						<li><@macro.addSubRubric box.rubric/></li>
					</@sec.authorize>			
					<@drawMenuItems box.rubric.children/>		
				</ul>
				</#if>
			</#if>
		</#if>
	</#if>
</#macro>

<#macro showBannerBox boxId>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)??>
			<#assign box = findBox(model.site.boxes, boxId) />
			
			<#if box??>
                <#if box.banners?? && (box.banners?size > 0)>
                	<#assign banner = box.randomBanner/>
                    <#if banner.url?? && '' != banner.url && banner.banner?? && '' != banner.banner>
						<div id="${boxId}" class="bannerBox">
	                    <a id="link_${boxId}" target='_blank' href="${banner.url}" <#if model.isAdmin??>onclick="return false;"</#if>><img alt="${banner.banner}" id="banner_${boxId}" src="${model.contextPath}${banner.banner}" class="banner" /></a>
						</div>
	                <#else/>
	                    <#if model.isAdmin??>
							<div id="${boxId}" class="bannerBox"><img id="banner_${boxId}" src="${model.contextPath}/img/sample_banner.jpg" class="banner" /></div>
	                    </#if>
                    </#if>                    
                <#else/>
                    <#if model.isAdmin??>
                    	<div id="${boxId}" class="bannerBox"><a href="#"><img id="banner_${boxId}" src="${model.contextPath}/img/sample_banner.jpg" class="banner" /></a></div>
                    </#if>
                </#if>
			</#if>
        <#else/>
          	<#if model.isAdmin??>出错 ${boxId}</#if>
		</#if>
	</#if>
</#macro>

<#macro showImageBox boxId>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)??>
			<#assign box = findBox(model.site.boxes, boxId) />
			<div id="${boxId}" class="topImageBox1">
            <#if box.image?? && '' != box.image>
				<img alt="${box.image}" id="banner_${boxId}" src="${model.contextPath}${box.image}" onerror="javascript:this.src='${model.contextPath}/img/header.jpg';" class="banner" />
            <#else/>
	            <#if model.isAdmin??>
                <img id="banner_${boxId}" src="${model.contextPath}/img/sample_image.jpg" class="banner" />
                </#if>
            </#if>
        	</div>
        <#else/>
          	<#if model.isAdmin??>${boxId}</#if>
		</#if>
	</#if>
</#macro>

<#macro showTextBox boxId showHeader=true>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)??>
			<#assign box = findBox(model.site.boxes, boxId)/>
			<#if showHeader> 
				<h2 class="edit" id="box_title_${boxId}">${box.title}</h2>
			</#if>
			<div id="box_body_${boxId}" class="textBoxBody" <#if model.isAdmin??>onclick="convertToEditor(this);return null;"</#if>>
				${box.body}<#if box.body?trim == "" && model.isAdmin??>(点击这里)</#if>
			</div>
		</#if>
	</#if>
</#macro>

<#macro prepareSlideshow boxId>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)?? >
			<#assign slideshow = findBox(model.site.boxes, boxId) />
			
			<#if slideshow??>
				var slides = new Array();
				var slideIndex = 0;
				<#if slideshow.slides?? && 0 < slideshow.slides?size>
					<#list slideshow.slides as slide>
						slides.push('${slide.id}');
					</#list>
				</#if>
			</#if>
		</#if>
	</#if>
</#macro>

<#macro drawSlideshow boxId>
	<#if boxId??>
		<#if findBox(model.site.boxes, boxId)?? >
			<#assign slideshow = findBox(model.site.boxes, boxId) />
		
			<#if slideshow??>
				<div id="${boxId}" class="slideshow">
					<div class="list">
						<p>
							<span class="shortcuts">
								<@drawSlideLinks slideshow boxId />
							</span>
						</p>
						<@sec.authorize ifAllGranted="ROLE_ADMIN">
							<a href="javascript:" class="slideAdder">[ + 添加滚动列]</a>
						</@sec.authorize>
					</div>
				
					<div class="slides">
						<@drawSlides slideshow boxId />
					</div>

					<div class="controls <#if (! slideshow.slides??) || slideshow.slides?size < 2 > hidden</#if>">
						<a href="#" id="slideshow-prev" onclick="previousSlide();return false;"><img src="${model.contextPath}/img/arrow-left.png" width="51" height="51" alt="Previous" /></a>
						<a href="#" id="slideshow-next" onclick="nextSlide();return false;"><img src="${model.contextPath}/img/arrow-right.png" width="51" height="51" alt="Next" /></a>
					</div>				
				</div>
			<#else/>
				&nbsp;
				<@sec.authorize ifAllGranted="ROLE_ADMIN">
					出错 ${boxId}!!!
				</@sec.authorize>
			</#if>
		<#else/>
			&nbsp;
			<@sec.authorize ifAllGranted="ROLE_ADMIN">
				出错: ${boxId}
			</@sec.authorize>
		</#if>
	<#else/>
		&nbsp;
		<@sec.authorize ifAllGranted="ROLE_ADMIN">
			出错!!!
		</@sec.authorize>
	</#if>
</#macro>

<#macro drawSlides slideshow boxId >
	<#if slideshow.slides?? && 0 < slideshow.slides?size>
		<#assign count = 0 />
		<#list slideshow.slides as slide>
			<@drawSlide slide count />
			<#assign count = count + 1 />
		</#list>
	<#else/>
		&nbsp;
		<@sec.authorize ifAllGranted="ROLE_ADMIN">
			出错 ${boxId}!
		</@sec.authorize>
	</#if>	
</#macro>

<#macro drawSlideLinks slideshow boxId >
	<#if slideshow.slides?? && 0 < slideshow.slides?size>
		<#assign count = 0 />
		<#list slideshow.slides as slide>
			<@drawSlideLink slideshow slide count slideshow.slides?size />
			<#assign count = count + 1 />
		</#list>
	</#if>	
</#macro>

<#macro drawSlide slide count>
	<#if slide??>
		<div class="item <#if count?? && 0 == count>firstSlide</#if>" id="slide_${slide.id}">
		<@sec.authorize ifAllGranted="ROLE_ADMIN">
			<h2 class="edit" id="slide_title_${slide.id}">${slide.title}</h2>
			<div id="slide_body_${slide.id}" title="${slide.title}" onclick="convertToEditor(this, function(){jQuery('.slideshow .controls').hide()}, function(){jQuery('.slideshow .controls').show()}, function(){jQuery('.slideshow .controls').show()});return null;">
		</@sec.authorize>
		<@sec.authorize ifNotGranted="ROLE_ADMIN">
			<div id="slide_body_${slide.id}" title="${slide.title}">
		</@sec.authorize>	
			<@sec.authorize ifAllGranted="ROLE_ADMIN">
				<span class="slideadmin">
			</@sec.authorize>			
				${slide.body}
			<@sec.authorize ifAllGranted="ROLE_ADMIN">
				</span>
			</@sec.authorize>			
			</div>
		</div>		
	<#else/>
		<@sec.authorize ifAllGranted="ROLE_ADMIN">
			出错!!!
		</@sec.authorize>
	</#if>
</#macro>

<#macro drawSlideLink slideshow slide count max>
	<#if slide??>
		<span id="slide_link_${slide.id}" <@sec.authorize ifAllGranted="ROLE_ADMIN">class="slideadmin"</@sec.authorize>>
			<@sec.authorize ifAllGranted="ROLE_ADMIN">
				<#if count?? && 0 < count>
					<a href="javascript:" onclick="moveSlide(${count}, -1);return false;"><img src="${model.contextPath}/img/left.png" alt="left"></a>
				</#if>
				<img class="deleteSlide" src="${model.contextPath}/img/menu_handle.png" onclick="if(confirm('确定删除该滚动列?')) delSlide('${slide.id}', '${slideshow.boxId}');">
			</@sec.authorize>
			<a id="slide_shortcut_${slide.id}" onclick="skipToSlide(${count}); return false;" class="shortcut <#if count?? && 0 == count>current</#if>" href="#">${slide.title}</a>
			<@sec.authorize ifAllGranted="ROLE_ADMIN">
				<#if count?? && count < (max - 1)>
					<a href="javascript:" onclick="moveSlide(${count}, 1);return false;"><img src="${model.contextPath}/img/right.png" alt="right"></a>
				</#if>
			</@sec.authorize>
			 
		</span>
	<#else/>
		<@sec.authorize ifAllGranted="ROLE_ADMIN">
			出错!!!
		</@sec.authorize>
	</#if> 
</#macro>
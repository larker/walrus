<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>
<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"]>

<#macro adminScripts>
<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<link rel="stylesheet"  href="${model.contextPath}/styles/cm.css" type="text/css" media="screen"/>
		<link rel="stylesheet" href="${model.contextPath}/libs/date_input/date_input.css" type="text/css" media="screen" />
		<script src="${model.contextPath}/libs/date_input/jquery.date_input.js"></script>
		<script src="${model.contextPath}/libs/jquery/jquery.form.js"></script>
		<script src="${model.contextPath}/libs/jeditable/jquery.jeditable.js"></script>
		<script src="${model.contextPath}/libs/js/boxes.js"></script>
		<script src="${model.contextPath}/libs/js/functions.js"></script>
		<script src="${model.contextPath}/libs/js/mcebrowser.js"></script>
		<script src="${model.contextPath}/libs/js/bootstrap.js"></script>
		
		<script language="javascript" type="text/javascript" src="${model.contextPath}/libs/tiny_mce/tiny_mce.js"></script>
		<script language="javascript" type="text/javascript">
		
		tinyMCE.init({
			// Location of TinyMCE script
			//script_url : '${model.contextPath}/libs/tiny_mce/tiny_mce.js',
			language : 'cn',
			// General options
			theme : "advanced",
			//skin : "o2k7"
			plugins : "safari,table,advimage,advlink,preview,media,contextmenu,paste,visualchars,inlinepopups,xhtmlxtras",  
			mode : "none",
			relative_urls : 0,
			dialog_type: "modal",
			// Theme options
			theme_advanced_buttons1 : "cut,copy,paste,pastetext,pasteword,|,undo,redo,|,fontsizeselect,forecolor,bold,italic,underline,strikethrough",
			theme_advanced_buttons2 : "justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,charmap,anchor,|,link,unlink,media,image",
			theme_advanced_buttons3 : "tablecontrols,|,forecolorpicker,|,code,removeformat",
			theme_advanced_buttons4 : "",
			//theme_advanced_containers : "commandManager",
			//theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_statusbar_location : "none",
			theme_advanced_toolbar_location : "external",
			theme_advanced_resizing : true,
			
			extended_valid_elements : "a[name|href|target|title|onclick|rel],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],input[size|name|type|class|value|id],textarea[name|rows|cols],button[type],form[action|method|id|onsubmit|enctype],label,#td[*],object[classid|codebase|width|height|align|*],param[name|value],",
			invalid_elements : "font,p[style]",
			setupcontent_callback : "focusMCE",
			file_browser_callback: "fileBrowserCallback",
			 
			// Example content CSS (should be your site CSS)
			content_css : "${model.contextPath}/styles/style.css"
		});		
		
	 	var globals = {
	 		currentRubricId : '${model.currentRubric.id}'
	 	};

		function reloadMenu(args) {
			var treeFlag = "";
			
			if(args && '1' == args.isTree) {
				treeFlag = "&isTree=1";
			}
			jQuery.get("index?menu=yo&rubricId=" + globals.currentRubricId + treeFlag, {}, function(html) {
				jQuery("*[id]", html).each(function() {
					jQuery("#"+this.id).html(jQuery(this).html());
				});
				dressRubricLinks();
			});
		}
		</script>
</@sec.authorize>
</#macro>

<#macro loginLogout>
	<@sec.authorize ifNotGranted="ROLE_ADMIN, ROLE_USER">
		<a href="${model.contextPath}/login/login.jsp" id="loginLink"><@spring.message code="cms.login"/></a>
	</@sec.authorize>
	<@sec.authorize ifAnyGranted="ROLE_ADMIN, ROLE_USER">
		<#assign currentUrl = "${model.requestURL}" />
		<#if model.queryString?? >
			<#assign currentUrl = "${currentUrl}?${model.queryString}" />
		</#if>
		<a href="${model.contextPath}/logout.html"><@spring.message code="cms.logout"/></a>
	</@sec.authorize>
</#macro>

<#function canShowInList article isArchive>
	<#if article??>
        <#if !isArchive >
            <#return (article.isOnline() && article.isVisible()) || (model.isAdmin?? && model.isAdmin && (!article.isOnline() || article.isPending())) />
        <#else/>
            <#return (article.isOnline() && article.isArchived()) || (model.isAdmin?? && model.isAdmin && !article.isOnline() && article.isArchived()) />
        </#if>
	<#else/>
        <#return false />
	</#if>
</#function>

<#macro delLink rubric>
	<#if !(model.currentRubric?exists && model.currentRubric.id == rubric.id)>
		<#if model.isAdmin?exists>
			<img class="deleteRubric cmsControl" src="${model.contextPath}/img/menu_handle.png" onclick="if (confirm('确定删除该区块?')) delRubric('${rubric.id}', '<#if model.currentRubric?exists>${model.currentRubric.id}</#if>'); return false;" />
		</#if>
	</#if>
</#macro>

<#macro deleteSite site>
	<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<img class="deleteRubric cmsControl" src="${model.contextPath}/img/menu_handle.png" onclick="if (confirm('Are you sure you want to delete this site?')) delSite('${site.id}'); return false;" />
	</@sec.authorize>
</#macro>

<#macro addSubRubric rubric>
	<#if model.isAdmin?exists>
		<#if model.currentRubric?exists >
			<span id="${rubric.id}" class="addSubRubricLink cmsControl"><img src="${model.contextPath}/img/addrubric.png" /></span>
		</#if>
	</#if>
</#macro>

<#macro addArticle rubric>
	<@sec.authorize ifAllGranted="ROLE_ADMIN">
		<p id="newArticleLink"><a href="#" onclick="XT.doAjaxAction('addArticle', this, {'rubricId': '${rubric.id}'}); return false;"><img src="${model.contextPath}/img/add_new_article_btn.png" alt="pridėti naują straipsnį" title="pridėti naują straipsnį" /></a></p>
	</@sec.authorize>
</#macro>

<#macro rubricModes rubric>
	<select id="dropdown" onchange="
						jQuery.ajax({
							url : ctx + '/cms/setRubricMode.do',
							type : 'POST',
							data : {
								rubricId : '${rubric.id}',
								mode : this.options[this.selectedIndex].value
							},
							success : function(result, status) {
								if (false === result.success) {
									displayError(result);
					 			} else if (true === result.success) {
					 				displaySuccess(result);
					 			}
							},
							error : function(xhr, status, error) {
								displayError({msg : '请求异常'});
							}
						})">
		<option <#if rubric.mode == "NONE">selected="selected"</#if>value="NONE">隐藏子区块</option>
		<option <#if rubric.mode == "SIMPLE_LIST">selected="selected"</#if>value="SIMPLE_LIST">子区块显示标题</option>
		<option <#if rubric.mode == "EXPANDED_LIST">selected="selected"</#if>value="EXPANDED_LIST">子区块显示标题和摘要</option>
	</select>
</#macro>

<#-- 管理元添加删除按钮操作 -->
<#macro deleteRubric rubric><@sec.authorize ifAllGranted="ROLE_ADMIN"><img class="deleteRubric cmsControl" src="${model.contextPath}/img/menu_handle.png" onclick="if(confirm('确定删除该区块?')) delRubric('${rubric.id}', '<#if model.currentRubric?exists>${model.currentRubric.id}</#if>');" /></@sec.authorize></#macro>

<#macro rubricControls rubric>
	<div id="articleControlsHeader"><img src="${model.contextPath}/img/arrow.gif"/><span>区块配置</span></div>
	<div id="articleControls">
		<p>
			<b>可用时间: </b>永远可用
			<input type="checkbox" class="help-visibleForever"
				onclick="
				jQuery.ajax({
					url : ctx + '/cms/setVisibleForever.do',
					type : 'POST',
					data : {
						rubricId : '${rubric.id}',
						visible : this.checked
					},
					success : function(result, status) {
						if (false === result.success) {
							displayError(result);
			 			} else if (true === result.success) {
			 				displaySuccess(result);
			 			}
					},
					error : function(xhr, status, error) {
						displayError({msg : '请求异常'});
					}
				})
				if (this.checked) { 
					jQuery('#rubricDates_${rubric.id}').hide()
				} else { 
					jQuery('#rubricDates_${rubric.id}').show()
				};" 
				<#if rubric.visibleForever>checked='checked'</#if> 
			/>
			<span id="rubricDates_${rubric.id}" class="rubricDates" <#if rubric.visibleForever>style="display:none"</#if>>
				可用时间从:
				<input id="visibleFrom_${rubric.id}" value="${rubric.visibleFrom?string("yyyy-MM-dd")}" class="date_input" type="text" size="10"
					onchange="
						var oThis = this;
						jQuery.ajax({
							url : ctx + '/cms/setVisibleFrom.do',
							type : 'POST',
							data : {
								rubricId : '${rubric.id}',
								date : this.value
							},
							success : function(result, status) {
								if (false === result.success) {
									displayError(result);
									oThis.value = result.o;
					 			} else if (true === result.success) {
					 				displaySuccess(result);
					 			}
							},
							error : function(xhr, status, error) {
								displayError({msg : '请求异常'});
							}
						})
					" 
				/>
				至:
				<input id="visibleTo_${rubric.id}" value="${rubric.visibleTo?string("yyyy-MM-dd")}"class="date_input" type="text" size="10"
					onchange="
						var oThis = this;
						jQuery.ajax({
							url : ctx + '/cms/setVisibleTo.do',
							type : 'POST',
							data : {
								rubricId : '${rubric.id}',
								date : this.value
							},
							success : function(result, status) {
								if (false === result.success) {
									displayError(result);
									oThis.value = result.o;
					 			} else if (true === result.success) {
					 				displaySuccess(result);
					 			}
							},
							error : function(xhr, status, error) {
								displayError({msg : '请求异常'});
							}
						})
						"				 
				/>
			</span>
		</p>
			<p>
				<b>区块类型:</b> <@macro.rubricModes rubric/>		
			</p>
		<#--if-->
		<p class="last">
			<b>发布:</b>
			<input type="checkbox" class="help-leaf"
					onclick="
						if(this.checked){
							jQuery.ajax({
								url : ctx + '/cms/publishArticle.do',
								type : 'POST',
								data : {
									rubricId : '${rubric.id}'
								},
								success : function(result, status) {
									if (false === result.success) {
										displayError(result);
						 			} else if (true === result.success) {
						 				displaySuccess(result);
						 			}
								},
								error : function(xhr, status, error) {
									displayError({msg : '请求异常'});
								}
							})
							jQuery('#onlineDescriotion_isOnline').show();
							jQuery('#onlineDescriotion_isOffline').hide();
							jQuery('#rubric_is_offline').hide();
						}else {
							jQuery.ajax({
								url : ctx + '/cms/unpublishArticle.do',
								type : 'POST',
								data : {
									rubricId : '${rubric.id}'
								},
								success : function(result, status) {
									if (false === result.success) {
										displayError(result);
						 			} else if (true === result.success) {
						 				displaySuccess(result);
						 			}
								},
								error : function(xhr, status, error) {
									displayError({msg : '请求异常'});
								}
							})
							jQuery('#onlineDescriotion_isOnline').hide();
							jQuery('#onlineDescriotion_isOffline').show();
							jQuery('#rubric_is_offline').show();
						}
						" 
					<#if rubric.online>checked='checked'</#if>
				/> 
			<span class="description" id="onlineDescriotion_isOnline" <#if !rubric.online>style="display:none;"</#if>>(状态：在线可访问)</span>
			<span class="description" id="onlineDescriotion_isOffline" <#if rubric.online>style="display:none;"</#if>>(状态：仅管理员可见.)</span>
		</p>			
		
	</div>
</#macro>



<#macro activeClass rubric>
	<#if (model.currentRubric?exists && model.currentRubric.id == rubric.id)>active</#if>
</#macro>

<#macro drawRubricLink rubric><@compress single_line=true>
	<#if "" == rubric.title && model.isAdmin??>
		<#local rtitle="[空名称]"/>
	<#else>
		<#local rtitle=rubric.title?html/>
	</#if>
	<#assign url>${model.contextPath}/${rubric.id}.html</#assign>
	<a href="${url}" class="rubric_link<#if !rubric.online || rubric.leaf> offline</#if> <@activeClass rubric/>">${rtitle}</a>
</@compress></#macro>

<#macro addSubRubricLink>
	<div id="addSubRubricLink" class="label cmsControl">
		<img class="help-addSubrubric" src="${model.contextPath}/img/addrubric.png" />
	</div>
</#macro>

<#function isRubricAttachedToBox arubric>
	<#list model.site.boxes as box>
		<#if box?? && box.rubric??>
			<#if arubric.id == box.rubric.id>
				<#return true/>
			</#if>
		</#if>
	</#list>
	<#return false/>
</#function>
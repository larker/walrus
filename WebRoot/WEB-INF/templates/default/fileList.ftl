<html>
	<head>  
			<script src="${model.contextPath}/libs/jquery/jquery-1.10.2.min.js"></script>
			<script src="${model.contextPath}/libs/jquery/jquery.form.js"></script>
	        <script type="text/javascript" src="${model.contextPath}/libs/js/mcebrowser.js"></script>
	        <script type="text/javascript" src="${model.contextPath}/libs/tiny_mce/tiny_mce_popup.js"></script>
	        <link rel="stylesheet" href="${model.contextPath}/styles/upload.css" type="text/css" media="screen"/>
	</head>
<body>
<div id ="uploadContainer">
<#assign type = model.type!"" />
<form method="post" id='upload' action="../cms/upload${type}" enctype="multipart/form-data">
 	<input type="file" name="file" id="fileInput"/><input type="button" value="上传" onclick="
 		jQuery('#upload').ajaxSubmit({
			url : '${model.contextPath}/cms/uploadFile.do',
			success : function(result, status) {
				if (false === result.success) {
					alert(result.msg)
	 			} else if (true === result.success) {
	 				selectURL('${model.contextPath}'+result.o);
	 			}
			},
			error : function(xhr, status, error) {
				alert('请求异常');
			}
		})"/>
</form>

<#if model.showFiles?? && model.showFiles>
	<#if model.list??>
	        <#list model.list as file>
				<p><a href="#" onclick="
					if(confirm('该文件可能会在其他地方引用，删除后可能报错. \n\n确定删除该文件?'))
					var oThis = this;
					jQuery.ajax({
						url : '${model.contextPath}/cms/deleteFile.do',
						type : 'POST',
						data : {
							fileName:'${file.name}',
							type:'${type}'
						},
						success : function(result, status) {
							if (false === result.success) {
				 			} else if (true === result.success) {
				 				jQuery(oThis).parent().remove();
				 			}
						},
						error : function(xhr, status, error) {
							alert('请求异常');
						}
					});
					return false;"><img src="${model.contextPath}/img/menu_handle.png" border="0"/></a> <a href="" onclick="selectURL('${model.contextPath}/upload/files/${file.name}');return false;">${file.name}</a></p>
	        </#list>
	<#else/>
	        <p>出错</p>
	</#if>
<#else/>
	<input type="button" value="Show uploaded files" onclick="window.location.href='list<#if model.type??>?type=${model.type}&<#else/>?</#if>full=true'" />
</#if>
</div>
</body>
</html>
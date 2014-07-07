
function dressRubricLinks() {
	jQuery('.addSubRubricLink').rubricAdder('newSubRubric.do', globals.currentRubricId);
}
function dressEditables() {
	jQuery(".edit").editable(ctx + '/cms/saveField.do',{
		 		tooltip	: '点击修改',
		 		cancel	: '<img src="'+ctx+'/img/cancel.png"/>',
		 		submit	: '<img src="'+ctx+'/img/save.png"/>',
		 		placeholder : '点击这里',
		 		onblur : 'ignore',
		 		ajaxoptions : {
		 			dataType : 'json'
		 		},
		 		callback : function(result, settings) {
		 			if (true === result.success) {
		 				jQuery(this).html(result.o);
		 				displaySuccess(result);
		 			} else if (false === result.success) {
		 				displayError(result);
		 			}
		 		}
		 	}
		);
}

(function($jq) {
	$jq.fn.rubricAdder = function(ajaxAction) {
		return this.each(function() {
			
			var self = this;
			var currentRubricId = this.id;
			//var link = jQuery('<span>' + $jq(this).html() + '</span>');
			var link = jQuery('<span><img src="'+ctx+'/img/addrubric.png"></span>');
			link.css('cursor', 'pointer');
			
			var adder = jQuery('<form style="display: inline;"/>');
			adder.hide();
			var input = jQuery("<input type=\"text\" class=\"input\" name=\"data\"/>");
			adder.append(input);
			var ok = jQuery("<input type=\"image\" class=\"imgInput\" src=\""+ctx+"/img/save.png\" value=\"保存\"/>");
			adder.append(ok);
			var cancel = jQuery("<input type=\"image\" class=\"imgInput\" src=\""+ctx+"/img/cancel.png\" value=\"取消\"/>");
			adder.append(cancel);
			
			link.click(function(e){
				link.hide();
				adder.show();
				input.focus();
				return false;
			});
			
			input.keydown(function (e) {
				if (e.keyCode == 27) {
					e.preventDefault();
					adder.hide();
					link.show();
					return false;
				}
			});
/*			
			input.blur(function(e) {
				adder.hide();
				link.show();
				return false;
			});
*/
			
			ok.click(function(e){
				adder.submit();
				return false;
			});
			
			cancel.click(function(e){
				adder.hide();
				link.show();
				return false;
			});			
			
			
			adder.submit(function (e){
				e.preventDefault();
				if(null != currentRubricId){
					jQuery.ajax({
						type : 'POST',
						data : {
							currentRubricId : currentRubricId,
							value : input.val()
						},
						url : ctx + '/cms/'+ ajaxAction,
						success : function(result, status) {
							if (false === result.success) {
								displayError(result);
				 			} else if (true === result.success) {
				 				displaySuccess(result);
				 				window.location.reload();
				 			}
						},
						error : function(xhr, status, error) {
							displayError({msg : '请求异常'});
						}
					})
				} else {
					alert('no no no: ' + currentRubricId);
				}
				return false;
			});	
			
			$jq(this).html('');
			$jq(this).append(link);
			$jq(this).append(adder);
		});
	};
})(jQuery);


(function($jq) {
	$jq.fn.slideAdder = function(ajaxAction) {
		return this.each(function() {
			
			var self = this;
			var slideshowId = this.parentNode.parentNode.id;
			var link = jQuery('<span>' + $jq(this).html() + '</span>');
			link.css('cursor', 'pointer');
			
			var adder = jQuery('<form style="display: inline; margin: 0px; padding: 0px;"/>');
			adder.hide();
			var input = jQuery("<input type=\"text\" class=\"input\" name=\"data\"/>");
			adder.append(input);
			var ok = jQuery("<input type=\"image\" class=\"imgInput\" src=\""+ctx+"/img/save.png\" value=\"保存\"/>");
			adder.append(ok);
			var cancel = jQuery("<input type=\"image\" class=\"imgInput\" src=\""+ctx+"/img/cancel.png\" value=\"取消\"/>");
			adder.append(cancel);
			
			link.click(function(e){
				link.hide();
				adder.show();
				input.focus();
				return false;
			});
			
			input.keydown(function (e) {
				if (e.keyCode == 27) {
					e.preventDefault();
					adder.hide();
					link.show();
					return false;
				}
			});

			ok.click(function(e){
				adder.submit();
				return false;
			});
			
			cancel.click(function(e){
				adder.hide();
				link.show();
				return false;
			});			
			
			
			adder.submit(function (e){
				e.preventDefault();
				if(null != slideshowId){
					jQuery.ajax({
						type : 'POST',
						data : {
							slideshowId : slideshowId,
							title : input.val()
						},
						url : ctx + '/cms/'+ ajaxAction,
						success : function(result, status) {
							if (false === result.success) {
								displayError(result);
				 			} else if (true === result.success) {
				 				input.val('');
								adder.hide();
								link.show();
								addSlide(result.o);
								displaySuccess(result);
				 			}
						},
						error : function(xhr, status, error) {
							displayError({msg : '请求异常'});
						}
					})
					
				} else {
					alert('no no no: ' + slideshowId);
				}
				return false;
			});	
			$jq(this).html('');
			$jq(this).append(link);
			$jq(this).append(adder);
		});
	};
})(jQuery);

function delSlide(slideId, slideshowId) {
	jQuery.ajax({
		type : 'POST',
		data : {
			slideId : slideId,
			slideshowId : slideshowId
		},
		url : ctx + '/cms/deleteSlide.do',
		success : function(result, status) {
			if (false === result.success) {
				displayError(result);
			} else if (true === result.success) {
				displaySuccess(result);
				reload();
			}
		},
		error : function(xhr, status, error) {
			displayError({msg : '请求异常'});
		}
	})
	return false;
}

function delRubric(deleteRubricId, currentRubricId) {
	jQuery.ajax({
		type : 'POST',
		data : {
			currentRubricId : currentRubricId,
			deleteRubricId : deleteRubricId
		},
		url : ctx + '/cms/deleteRubric.do',
		success : function(result, status) {
			if (false === result.success) {
				displayError(result);
			} else if (true === result.success) {
				displaySuccess(result);
				reload();
			}
		},
		error : function(xhr, status, error) {
			displayError({msg : '请求异常'});
		}
	})
	return false;
}

function reload() {
	window.location.reload();
}

function convertToEditor(el, beforeconvert, onsubmit, oncancel){
	var body = jQuery(el);
	var formId = el.id + '_form';
	var adder = jQuery('<form name=\"bodySaver\" method=\"POST\" id=\"' + formId + '\"/>');
	var input = jQuery("<input type=\"hidden\" name=\"text\"/>");
	input.val(body.html());
	adder.append(input);
	var submitButton = jQuery("<input type=\"image\" class=\"imgInput\" src=\""+ctx+"/img/save.png\" value=\"保存\"/>")
	var cancelButton = jQuery("<input type=\"image\" class=\"imgInput\" src=\""+ctx+"/img/cancel.png\" value=\"取消\"/>")
	var originalParentHeight = body.parent()[0].clientHeight;

	adder.append(submitButton);
	adder.append(cancelButton);
	
	cancelButton.click(function(e){
		//jQuery(el).tinymce().execCommand('mceCleanup');
		tinyMCE.execInstanceCommand(el.id, 'mceCleanup');
		//jQuery(el).tinymce().hide();
		tinyMCE.execCommand('mceRemoveControl',false,el.id);
		body.html(input.val());
		body.parent().css("height", originalParentHeight);
		adder.remove();
		if(oncancel) {
			oncancel();
		}
		return false;
	});

	submitButton.click(function (e){
		e.preventDefault();
		//jQuery(el).tinymce().execCommand('mceCleanup');
		tinyMCE.execInstanceCommand(el.id, 'mceCleanup');
		//jQuery(el).tinymce().hide();
		tinyMCE.execCommand('mceRemoveControl',false,el.id);
		//input.val(jQuery(el).tinymce().html());
		input.val(body.html());
		//XT.doAjaxSubmit('saveBody', el, null, {'formId' : formId, 'enableUpload' : true})
		jQuery.ajax({
			type : 'POST',
			data : {
				id : el.id,
				value : input.val()
			},
			url : ctx + '/cms/saveField.do',
			success : function(result, status) {
				if (false === result.success) {
	 				displayError(result);
	 			} else if (true === result.success) {
	 				displaySuccess(result);
	 			}
			},
			error : function(xhr, status, error) {
				displayError('请求失败');
			}
		})
		
		
		// body.parent().css("height", originalParentHeight);
		body.parent()[0].style.height = originalParentHeight;
		adder.hide();
		if(jQuery.trim(body.html()) == '') {
			body.text('(点击这里编辑)');
		}
		if(onsubmit){
			onsubmit();
		}
		return false;
		//adder.remove();
	});	

	
	body.after(adder);
	body.parent().css("height", "auto");
	if(beforeconvert){
		beforeconvert();
	}
	
	//jQuery(el).tinymce().show();
	tinyMCE.execCommand('mceAddControl',false,el.id);
}

function focusMCE(editor_id) {
	//tinyMCE.execCommand('mceFocus', false, editor_id);
}

function updateArticlePic(params) {
	var a = jQuery('#article_pic_' + params.articleId);
	var el = jQuery('#article_pic_' + params.articleId + ' img');
	el.attr('src',params.thumb);
}

function displayError(params) {
	jQuery('#error').html(params.msg);
	jQuery('#errorBox').show();
	return false;
}
function displaySuccess(params) {
    var time = 1000;
    if (jQuery("#tip_message").text().length > 0) {
        var msg = "<span>" + params.msg + "</span>";
        jQuery("#tip_message").empty().append(msg);
    } else {
        var msg = "<div id=\"tip_message\"><span>" + params.msg + "</span></div>";
        jQuery("body").append(msg);
    }
    jQuery("#tip_message").fadeIn(time);
    jQuery("#tip_message").fadeOut(time)
};

function setCacheDisabled(flag) {
	jQuery.ajax({
		type : 'POST',
		data : {
			flag : flag
		},
		url : ctx + '/cms/setCacheDisabled.do',
		success : function(result, status) {
			if (false === result.success) {
 				displayError(result);
 			} else if (true === result.success) {
 				displaySuccess(result);
 				window.location = ctx + "/logout.html";
 			}
		},
		error : function(xhr, status, error) {
			displayError('请求失败');
		}
	})
}


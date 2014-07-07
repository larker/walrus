jQuery.noConflict();

jQuery(document).ready(function() {

	dressEditables();
 	dressRubricLinks();
 	
 	jQuery('.bannerBox').bannerEditor();
 	
 	jQuery('.imageBox').imageEditor();
	
	jQuery('.slideAdder').slideAdder('newSlide.do'); 	
	
 	jQuery.extend(DateInput.DEFAULT_OPTS, {
	  stringToDate: function(string) {
	    var matches;
	    if (matches = string.match(/^(\d{4,4})-(\d{2,2})-(\d{2,2})$/)) {
	      return new Date(matches[1], matches[2] - 1, matches[3]);
	    } else {
	      return null;
	    };
	  },
	  dateToString: function(date) {
	    var month = (date.getMonth() + 1).toString();
	    var dom = date.getDate().toString();
	    if (month.length == 1) month = "0" + month;
	    if (dom.length == 1) dom = "0" + dom;
	    return date.getFullYear() + "-" + month + "-" + dom;
	  }
	  
	});

 	jQuery(".date_input").date_input();
 	jQuery("#articleControlsHeader").click(function(){jQuery("#articleControls").toggle()});
});

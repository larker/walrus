function fileBrowserCallback(field_name, url, type, win) {
        var listUrl = ctx + "/cms/list.do?type="+type;
        
        tinyMCE.activeEditor.windowManager.open({
        file : listUrl,
        title : '上传图片',
        width : 420, 
        height : 400,
        resizable : "yes",
        inline : "yes",
        close_previous : "no"
    }, {
        'window' : win,
        'input' : field_name
    }); 
    return false;
}

function selectURL(url) {
        //field = window.top.opener.browserWin.document.forms[0].elements[window.top.opener.browserField];
        browserWin=tinyMCEPopup.getWindowArg('window');
        browserField=tinyMCEPopup.getWindowArg('input');
        field=browserWin.document.forms[0].elements[browserField];
        field.value = url;
        if (field.onchange != null)
                field.onchange();
        browserWin.focus();  
        tinyMCEPopup.close();
}

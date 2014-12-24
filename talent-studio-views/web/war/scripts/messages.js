
function markMessageAsRead(url, messageId) {
    // call the update and submit to the url
    messageItemBean.setMessageItemRead(messageId);
    window.location.href = url;
}

function confirmDeleteMessages(frmName, message) {

    var elems = document.getElementsByName("m_ids");
    var count = 0;
    for(var i = 0; i < elems.length; i++) {
        if(elems[i].checked) count = count + 1;
    }
    if (count > 0) {
        confirmActionAndPost(frmName, message);
    } else {
        alert("Please select the items to be deleted");
    }
}
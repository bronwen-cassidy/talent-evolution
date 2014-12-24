var popupId = 'helpText';
var popupTitleId = popupId + 'Title';
var popupIFrameId = popupId + 'Iframe';
var popupContentIFrameId = popupId + 'contentIframe';
var popupHiddenField = popupId + 'Field';
var textAreaElementId = 'helpTextAreaElementId';
var editing = false;
var itemIdElem = null;

var noHelpTextMessage = null;

var helpTextCallback = function(helpTextItem) {
    
    var divElem = getElemById(popupId + "Content");    
    var message;
    if(helpTextItem == null) {
        message = noHelpTextMessage;
    } else {
        message = helpTextItem.helpText;
    }
    if (editing) {
        var textElem = getElemById(textAreaElementId);
        textElem.value = message;
    } else {
        divElem.innerHTML=message;
    }
}

function showHelpText(itemId, noValueMessage, editVar) {
    editing = editVar;
    itemIdElem = itemId;
    noHelpTextMessage = noValueMessage;
    helpTextBean.getHelpTextItem(itemId, helpTextCallback);
}

function showHelpTextPopup(title, popupRoot) {
    popupShow(title, popupRoot, popupId);
}

function saveHelpText() {

    if(itemIdElem != null) {
        var textElem = getElemById(textAreaElementId);
        helpTextBean.setHelpText(itemIdElem, textElem.value);
        // now clear the text area
        textElem.value = "";
    }
}

function closePopup() {
    if (editing) {
        var textElem = getElemById(textAreaElementId);
        textElem.value = "";
    } else {
        divElem.innerHTML="";
    }
}

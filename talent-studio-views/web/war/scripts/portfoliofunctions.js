var PORTFOLIO_ITEM_UPLOAD_TEXT = 'TEXT';
var PORTFOLIO_ITEM_URL = 'URL';
var PORTFOLIO_ITEM_UPLOAD = 'UPLOAD';
var PORTFOLIO_ITEM_NONE = '';

function onContentSubTypeSelect(selectId) {

    var selectDropDown = document.getElementById(selectId);

    for (var i = 0; i < selectDropDown.options.length; i++) {
        if (selectDropDown.options[i].selected) {
            var type = selectDropDown.options[i].value;
            activateSubTypeSelection(type);
            break
        }
    }

}

function activateSubTypeSelection(type) {
    var textElem = document.getElementById("portfolioItemText");
    var urlElem = document.getElementById("portfolioItemUrl");
    var uploadElem = document.getElementById("portfolioItemUpload");

    if (type == PORTFOLIO_ITEM_UPLOAD_TEXT) {
        textElem.style.display = '';
        urlElem.style.display = 'none';
        uploadElem.style.display = 'none';

    } else if (type == PORTFOLIO_ITEM_URL) {
        textElem.style.display = 'none';
        urlElem.style.display = '';
        uploadElem.style.display = 'none';

    } else if (type == PORTFOLIO_ITEM_UPLOAD) {
        textElem.style.display = 'none';
        urlElem.style.display = 'none';
        uploadElem.style.display = '';

    } else if (type == PORTFOLIO_ITEM_NONE) {
        textElem.style.display = 'none';
        urlElem.style.display = 'none';
        uploadElem.style.display = 'none';
    }

}
var currentTimeOutAmount;
var timer;
var countDownTimer;
var timeOutWarningMessage;
var logoutUrl;
var ignoreTimer = false;
var CountActive = true;
var CountStepper = -1;
var DisplayFormat = "%%M%% Minutes, %%S%% Seconds.";
var navTdId = "td_hideable";
var thisTimeoutAmount;

if (!String.prototype.trim) {
    String.prototype.trim = function () {
        return this.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');
    };
}

function disableButton(theButton)
{
    theButton.disabled = true;
}

function disableSubmitButton(btnId) {
    disableButton(getElemById(btnId));
}

function enableDisableCheckbox(srcElem, targetElemId) {
    var targetElem = getElemById(targetElemId);
    if (srcElem.checked) {
        targetElem.checked = false;
        targetElem.disabled = true;
    } else {
        targetElem.disabled = false;
    }
}

function disableElement(elementIds) {

    if (elementIds) {
        clearElements(elementIds);
        for (var i = 0; i < elementIds.length; i++) {
            var elem = getElemById(elementIds[i]);
            elem.disabled = true;
        }
    }
}

function enableElement(elementIds) {
    if (elementIds) {
        for (var i = 0; i < elementIds.length; i++) {
            var elem = getElemById(elementIds[i]);
            elem.disabled = false;
        }
    }
}

function clearElements(elementIds) {
    if (elementIds) {
        for (var i = 0; i < elementIds.length; i++) {
            var elem = getElemById(elementIds[i]);
            if (elem.tagName == "SELECT") {
                elem.options[0].selected = "true";
            }
            if (elem.tagName == "INPUT") {
                elem.value = "";
                elem.text = "";
            }
        }
    }
}

function selectElements(elementIds) {
    if (elementIds) {
        for (var i = 0; i < elementIds.length; i++) {
            var elem = getElemById(elementIds[i]);
            elem.checked = true;
        }
    }
}

function deselectElements(elementIds) {
    if (elementIds) {
        for (var i = 0; i < elementIds.length; i++) {
            var elem = getElemById(elementIds[i]);
            elem.checked = false;
        }
    }
}

function clearSelectionOptions(selectElemId) {
    var selectElem = getElemById(selectElemId);
    for (var j = 0; j < selectElem.options.length; j++) {
        selectElem.options[j].selected = false;
    }
}

var submitted = false;

function submitForm(theForm)
{
    if (submitted) {
        return;
    }

    theForm.submit();
    submitted = true;
}

function createCookie(name, value) {
    var expires = "";
    document.cookie = name + "=" + value + expires + "; path=/";
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name, "", -1);
}

function submitFormWithTarget(theForm, target, targetFieldId) {
    var targetField = getElemById(targetFieldId);
    targetField.name = "_target" + target;
    targetField.value = "_target" + target;
    var frm = document.forms[theForm];
    submitForm(frm);
}

function confirmAction(url, message) {
    var confirmed;
    confirmed = confirm(message);
    if (confirmed == '1') {
        sendTo(url);
    }
}

function confirmActionAndPost(frmName, message) {
    var confirmed;
    confirmed = confirm(message);
    if (confirmed == '1') {
        document.forms[frmName].submit();
    }
}

function sendTo(url) {
    if (timer != null) window.clearTimeout(timer);
    window.location.href = url;
}

function showHideNav() {

    var navTd = getElemById(navTdId);
    if (navTd != null) {
        if (navTd.style.display == 'none') {
            navTd.style.display = 'block';
            navSessionBean.setNavVisible();
        } else {
            navTd.style.display = 'none';
            navSessionBean.setNavHidden();
        }
    }
    
    $('#nav').toggleClass('medium-12', 'medium-10');
}

function setDefaultReadPermissions(searchable, individual_write, manager_write, selected, itemsToCheck)
{
    var checked = (selected.checked == 1);
    if (checked)
    {
        setCheckedDisabled(itemsToCheck, checked);
    }
    else
    {
        if (selected == searchable)
        {
            if ((individual_write != undefined) && (individual_write.checked == 0))
            {
                if (itemsToCheck[1] != null)
                {
                    setCheckedDisabled(itemsToCheck[1], checked);
                }
            }
            if ((manager_write != undefined) && (manager_write.checked == 0))
            {
                if (itemsToCheck[0] != null)
                {
                    setCheckedDisabled(itemsToCheck[0], checked);
                }
            }
        }
        else
        {
            if (searchable.checked == 0) setCheckedDisabled(itemsToCheck, checked)

        }
    }
}


function setCheckedDisabled(itemsToCheck, checked)
{
    setChecked(itemsToCheck, checked);
    setDisabled(itemsToCheck, checked);
}


function setDisabled(field, value)
{
    try
    {
        if (field != null)
        {
            if (field.length != null)
            {
                for (var i = 0; i < field.length; i++)
                {
                    if (field[i] != undefined)
                    {
                        field[i].disabled = value;
                    }
                }
            }
            else
            {
                field.disabled = value;
            }
        }
    }
    catch (e)
    {
        alert("Error in setDisabled(). " + e.message);
    }
}

function writeSelected(read, write)
{
    if (write.checked == 1)
    {
        setChecked(read, 1);
        read.disabled = true;
    }
    else
    {
        read.disabled = false;
    }
}

function setBackgroundColour(select)
{
    var selectedIndex = select.selectedIndex;
    var selectedOption = select.options[selectedIndex];

    if (selectedOption != null) select.style.backgroundColor = selectedOption.style.backgroundColor;
}

function getElemById(id)
{
    return getElemByIdAndDoc(id, document, true);
}

function getElemByIdAndDoc(id, doc)
{
    var elem = null;
    if (id != null)
    {
        if (doc.getElementById != null)
        {
            elem = doc.getElementById(id);
        }
        else if (doc.all != null)
        {
            elem = doc.all[id];
        }
    }
    return elem;
}


function setChecked(field, value)
{
    try
    {
        if (field != null)
        {
            if (field.length != null)
            {
                for (var i = 0; i < field.length; i++)
                {
                    if (field[i] != undefined && !field[i].disabled)
                    {
                        field[i].checked = value;
                    }
                }
            }
            else
            {
                field.checked = value;
            }
        }
    }
    catch (e)
    {
        alert("Error in setChecked(). " + e.message);
    }
}

function checkAll(field)
{
    setChecked(field, true);
}

function uncheckAll(field)
{
    setChecked(field, false);
}

function placeFocus(body)
{
    placeFormFocus();
}

function placeFormFocus()
{
    try
    {
        if (document.forms.length > 0)
        {
            for (var f = document.forms.length - 1; f >= 0; f--)
            {
                var frm = document.forms[f];

                if (frm.elements.length > 0)
                {
                    for (var i = 0; i < frm.elements.length; i++)
                    {
                        var elm = frm.elements[i];

                        if ((elm.type == "text") || (elm.type == "textarea") ||
                            (elm.type == "checkbox") || (elm.type == "password"))
                        {
                            elm.focus();
                            return true;
                        }
                    }
                }
            }
        }
    }
    catch(e)
    {
    }
    return false;
}

function swapStyle(thing, oldStyle, newStyle)
{
    try
    {
        var nowClass = thing.className;
        thing.className = nowClass.replace(oldStyle, newStyle);
    }
    catch(e)
    {
        alert("Error setting style. " + e.message);
    }
}

function toggleTime(fromElement, toElement)
{
    var from = getElemById(fromElement);
    var target = getElemById(toElement);

    if (from.selectedIndex != 0)
    {
        if (target.selectedIndex == 0)
        {
            target.selectedIndex = 1;
        }
    }
    else
    {
        if (target.selectedIndex != 0)
        {
            target.selectedIndex = 0;
        }
    }
}

function setHiddenAndSubmit(formName, hiddenFieldId, val)
{
    var frm = document.forms[formName];
    var hiddenFld = getElemById(hiddenFieldId);
    hiddenFld.value = val;
    frm.submit();
}

function setHiddenField(hidFieldId, hidFldVal, hidFldNme)
{
    var hiddenFld = getElemById(hidFieldId);
    if (hiddenFld != null)
    {
        if (hidFldNme != null) hiddenFld.name = hidFldNme;
        hiddenFld.value = hidFldVal;
    }
}

function setNameValueAndSubmit(formNme, fldId, fldName, fldValue) {
    setHiddenField(fldId, fldValue, fldName);
    submitForm(document.forms[formNme]);
}


function switchTab(hiddenFieldId, newActiveTabId)
{
    var hiddenField = getElemById(hiddenFieldId);
    var currentTabId = hiddenField.value;

    if (currentTabId != newActiveTabId)
    {

        swapStyle(getElemById(currentTabId), 'tab_active_tab', 'tab_inactive_tab');
        getElemById(currentTabId + "_span").style.display = "none";


        swapStyle(getElemById(newActiveTabId), 'tab_inactive_tab', 'tab_active_tab');
        getElemById(newActiveTabId + "_span").style.display = "inline";

        hiddenField.value = newActiveTabId;

        placeFormFocus();
    }
}

function showHideDivs(formElemName, readonlyElemsId, disableVar) {
    var formElem = document.forms[formElemName];
    var divElemArray = formElem.getElementsByTagName("div");

    for (var i = 0; i < divElemArray.length; i++) {
        var divElem = divElemArray[i];
        if (divElem.className == "open") {
            divElem.className = "closed";
        } else {
            divElem.className = "open"
        }
    }

    if (readonlyElemsId != null) {
        var formElemArray = formElem.elements;
        for (i = 0; i < formElemArray.length; i++) {
            var childFormElem = formElemArray[i];
            if (childFormElem.name == readonlyElemsId) {
                childFormElem.disabled = disableVar;
            }
        }
    }
}

/**
 * Used to check the number of characters typed into a text box
 */

function CheckFieldLength(fn, wn, rn, mc) {
    var len = fn.value.length;
    if (len > mc) {
        fn.value = fn.value.substring(0, mc);
        len = mc;
    }
    document.getElementById(wn).innerHTML = len;
    document.getElementById(rn).innerHTML = mc - len;
}
function tabLoading(hideTab, loadingTab)
{
    var tab = getElemById(hideTab);
    tab.style.display = "none";
    var loading = getElemById(loadingTab);
    loading.style.display = "block";
}

function loading(node, label)
{
    getElemById(node).innerHTML = '<div class="loading" style="display:block;">' + label + '<br/><img src="/../images/loading_large.gif" alt="loading"/></div>';
}

function setValue(elemId, value)
{
    getElemById(elemId).value = value;
}

function setValueAndSubmit(formId, elemId, value) {
    setValue(elemId, value);
    document.forms[formId].submit();
    return false;
}

function setName(elemId, name) {
    getElemById(elemId).name = name;
}

function setNameAndSubmit(formId, elemId, name) {
    setName(elemId, name);
    document.forms[formId].submit();
    return false;
}

function setNameAndValue(elemId, name, value) {
    setName(elemId, name);
    setValue(elemId, value);
}

function clearField(elemId) {
    getElemById(elemId).value = '';
}

function clearAndSetField(elemId, originalElemId) {
    var fieldElem = getElemById(elemId);
    var originalField = getElemById(originalElemId);
    fieldElem.value = originalField.value;
}

function hideTable(tbleElemId, expandElemId, minimizeElemId) {
    createCookie(tbleElemId, 'invisible');
    getElemById(tbleElemId).style.display = 'none';
    getElemById(expandElemId).style.display = 'inline';
    getElemById(minimizeElemId).style.display = 'none';
}

function showTable(tbleElemId, expandElemId, minimizeElemId) {
    createCookie(tbleElemId, "visible");
    getElemById(tbleElemId).style.display = 'inline';
    getElemById(expandElemId).style.display = 'none';
    getElemById(minimizeElemId).style.display = 'inline';
}

function logoutOnTimeout() {
    sendTo(logoutUrl);
}

function calcage(secs, num1, num2) {
    var s = ((Math.floor(secs / num1)) % num2).toString();
    if (s.length < 2)
        s = "0" + s;
    return "<b>" + s + "</b>";
}

var timeOutPeriod = (Math.abs(CountStepper) - 1) * 1000 + 990;

function CountBack(secs) {

    if (secs < 0) {
        window.clearTimeout(countDownTimer);
        logoutOnTimeout();
        return;
    }

    var DisplayStr = DisplayFormat.replace(/%%M%%/g, calcage(secs, 60, 60));
    DisplayStr = DisplayStr.replace(/%%S%%/g, calcage(secs, 1, 60));

    document.getElementById("cntdwn").innerHTML = DisplayStr;
    if (CountActive)
        countDownTimer = window.setTimeout("CountBack(" + (secs + CountStepper) + ")", timeOutPeriod);
}

function warningToTimeout() {
    var warningMsg = getElemById('timeoutWarningMsgIdz').value;
    popupShowWarning(warningMsg, 'sessionExpWarning');
    window.clearTimeout(timer);
    CountBack(4 * 60);
}

function popupHideWarning(popupId, popupIFrame) {
    window.clearTimeout(timer);
    var warningPopup = getElemById(popupId);
    var warningPopupIFrame = getElemById(popupIFrame);
    popupHide(warningPopup, warningPopupIFrame);

}

function removeTimer() {
    ignoreTimer = true;
}

function pollTimeOut(timeoutAmount, message, url) {
    thisTimeoutAmount = timeoutAmount;
    timeOutWarningMessage = message;
    logoutUrl = url;

    if (!ignoreTimer) {
        window.clearTimeout(timer);
        window.clearTimeout(countDownTimer);
        currentTimeOutAmount = timeoutAmount - (5 * 60 * 1000);
        timer = window.setTimeout('warningToTimeout()', currentTimeOutAmount);
    }
}

function checkHiddenDivs(parentContainerId, buttonDivElemPrefix, tbleDivPrefix) {
    var parentContainer = getElemById(parentContainerId);

    if (parentContainer) {

        var childDivs = parentContainer.getElementsByTagName("DIV");
        for (var i = 0; i < childDivs.length; i++) {
            var childDiv = childDivs[i];
            var elemId = childDiv.id;
            if (elemId.indexOf(tbleDivPrefix) > -1) {

                if (readCookie(elemId) == 'invisible') {
                    childDiv.style.display = "none";

                    var buttonDivElemId = buttonDivElemPrefix + elemId.substring(2, elemId.length);
                    var buttonDivElem = getElemById(buttonDivElemId);
                    if (buttonDivElem) {
                        var expandSpan = buttonDivElem.getElementsByTagName("SPAN")[1];
                        var minimizeSpan = buttonDivElem.getElementsByTagName("SPAN")[2];
                        expandSpan.style.display = "inline";
                        minimizeSpan.style.display = "none";
                    }
                }
            }
        }
    }
}

function enableSelection(elemId1, elemId2) {
    getElemById(elemId1).disabled = false;
    getElemById(elemId2).disabled = false;
}

function getInnerHeight() {

    var y;
    if (self.innerHeight) {
        y = self.innerHeight;
    } else if (document.documentElement && document.documentElement.clientHeight) {
        y = document.documentElement.clientHeight;
    } else if (document.body) {
        y = document.body.clientHeight;
    }
    return(y);
}

function getScrollOffset() {
    var y;
    if (self.pageYOffset) {
        y = self.pageYOffset;
    } else if (document.documentElement && document.documentElement.scrollTop) {
        y = document.documentElement.scrollTop;
    } else if (document.body) {
        y = document.body.scrollTop;
    }
    return(y);
}

function copyText(sourceElem, targetElemId) {
    var elem1 = getElemById(targetElemId);
    if(!elem1.value) {
        elem1.value = sourceElem.options[sourceElem.selectedIndex].text;
    }
}
// variables to hold the zIndex and the resize counter for the current showing popup
var zIndex = 10000;
var startEventPos = new Array();
var startPosWindow = new Array();
var startWindowSize = new Array();
var onCloseFunction = null;


/**/
function findPosX(obj)
{
    var curleft = 0;

    if (obj.offsetParent) {
        while (obj.offsetParent) {
            curleft += obj.offsetLeft;
            obj = obj.offsetParent;
        }
    } else if (obj.offsetLeft) {
        curleft += obj.offsetLeft;
    }
    return curleft;
}

/**/
function findPosY(obj)
{
    var curtop = 0;

    if (obj.offsetParent) {
        while (obj.offsetParent) {
            curtop += obj.offsetTop;
            obj = obj.offsetParent;
        }
    } else if (obj.offsetTop) {
        curtop += obj.offsetTop;
    }
    return curtop;
}


/* drag event variable declarations */
var dragAllowed = false;
var dragStartX;
var dragStartY;
var mouseStartX;
var mouseStartY;

document.onmouseup = new Function("dragAllowed = false");

function startDragDrop(e)
{
    mouseStartX = e.clientX;
    mouseStartY = e.clientY;

    dragStartX = parseInt(displayedPopup.style.left);
    dragStartY = parseInt(displayedPopup.style.top);

    dragAllowed = true;

    document.onmousemove = doDrag;
}

function doDrag(e)
{
    if (dragAllowed) {
        if (e == null)  e = event;

        x = dragStartX + e.clientX - mouseStartX;
        y = dragStartY + e.clientY - mouseStartY;

        if (x >= 0) {
            displayedPopup.style.left = x + "px";
            displayedPopupIframe.style.left = x + "px";
        }
        if (y >= 0) {
            displayedPopup.style.top = y + "px";
            displayedPopupIframe.style.top = y + "px";
        }
        return false;
    }
}

/*
 Global variables for popups. We remember which popup is displayed (if any), and the
 two fields in the main page that are associated with the popup (The display value
 and the corresponding key.
 */

var targetElement = null;
var displayedPopup = null;
var displayedPopupId = null;
var displayedPopupIframe = null;
var displayedWindowContent = null;

var popupPartnerValueField = null;
var popupPartnerKeyField = null;
var onSelectFunction = null;

var initResizeCounter = -1;


function initResizeWindow(e)
{
    if (document.all) e = event;
    initResizeCounter = 0;
    startWindowSize = [displayedWindowContent.offsetWidth, displayedWindowContent.offsetHeight];
    startEventPos = [e.clientX, e.clientY];
    startResizeWindow();
    return false;

}

function startResizeWindow()
{
    if (initResizeCounter >= 0 && initResizeCounter <= 10) {
        initResizeCounter++;
        setTimeout('startResizeWindow()', 5);
    }
}

function moveWindow(e)
{
    if (document.all)e = event;

    if (initResizeCounter >= 10) {
        var newWidth = Math.max(startWindowSize[0] + e.clientX - startEventPos[0]);
        var newHeight = Math.max(startWindowSize[1] + e.clientY - startEventPos[1]);
        displayedPopup.style.width = newWidth + 'px';
        displayedWindowContent.style.height = newHeight + 'px';
        displayedPopupIframe.style.width = newWidth + 'px';
        displayedPopupIframe.style.height = (newHeight + 20) + 'px';
    }

    if (!document.all) return false;
}

function stopMove(e)
{
    if (document.all)e = event;
    initResizeCounter = -1;
}

function cancelEvent()
{
    return initResizeCounter == -1;
}

//
// Show a popup, moving it to be next to the control which invoked it.
//
// Parameters:
//		title:			String containing html to display in title of popup. Can be 'null' for no title
//		popupRoot: 		The control to anchor the top-left of the popup to. This control MUST be
//						an element that has a style applied to it.
//		popupId:		id of the element implementing the popup
//		partnerValueId:	id of the text field to recieve display text of selected item
//		partnerKeyId:	id of the text field to recieve key value of the selected item
//		onSelFunc:		JavaScript function to call when selection made. (Can be null)
//		allowClear:		true or false - display the Clear button or not.
//
function popupShow(title, popupRoot, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear, currElem)
{
    initialiseState(popupId);
    //    pTop = findPosY(popupRoot);
    //    pLeft = findPosX(popupRoot);
    //    var pTop = document.body.scrollTop + (document.body.clientHeight  / 2);
    //    var pLeft = document.body.scrollLeft + (document.body.clientWidth / 2);
    var pTop = getScrollOffset() + ( getInnerHeight() / 2 );
    var pLeft = document.body.clientWidth / 2;
    pLeft -= 100;
    pTop -= 100;
    
    popupShowTopLeft(title, pTop, pLeft, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear, currElem)
}

function popupShowWarning(title, popupId) {

    var popupTitle = getElemById(popupId + "Title");
    popupTitle.innerHTML = title;

    showHideClearButton(popupId, false);

    // midpoint of the screen
    var pTop = document.body.scrollTop + (document.body.clientHeight / 2);
    var pLeft = document.body.scrollLeft + (document.body.clientWidth / 2);

    pLeft -= 70;
    pTop -= 10;

    var popup = getElemById(popupId);

    displayedPopupIframe = getElemById(popupId + "Iframe");

    popup.style.top = pTop + "px";
    popup.style.left = pLeft + "px";

    displayedPopupIframe.style.top = pTop + "px";
    displayedPopupIframe.style.left = pLeft + "px";

    // set offset on iframe
    setOffSet(popup, displayedPopupIframe);

    // make visible
    popup.style.display = "inline";
    displayedPopupIframe.style.display = "inline";

    // Remember that we've displayed this popup
    displayedPopup = popup;
    displayedWindowContent = getElemById(popupId + 'Content');
}

function popupShowTopLeft(title, pTop, pLeft, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear, currElem)
{
    // Close any existing popup

    if (displayedPopup != null)
    {
        popupHide();
    }

    displayedPopupId = popupId;
    onSelectFunction = onSelFunc;

    // Get the popup and set its title
    var popupTitle = getElemById(popupId + "Title");
    popupTitle.innerHTML = title;

    showHideClearButton(popupId, allowClear);

    if (partnerValueId != null) popupPartnerValueField = getElemByIdAndDoc(partnerValueId, document, true);
    if (partnerKeyId != null) popupPartnerKeyField = getElemByIdAndDoc(partnerKeyId, document, true);

    windowPopupShow(popupId, pTop, pLeft, currElem);
}

//
// Hide a popup
//

function showHideClearButton(popupId, allowClear)
{
    var clearBtn = getElemById(popupId + "ClearBtn");

    if (allowClear)
    {
        clearBtn.style.display = 'inline';
    }
    else
    {
        clearBtn.style.display = 'none';
    }
}

function popupHide(popupToHide, popupIframeToHide)
{
    if (onCloseFunction) {
        eval(onCloseFunction);
        onCloseFunction = null;
    } else if (parent.onCloseFunction) {
        parent.eval(parent.onCloseFunction);
        parent.onCloseFunction = null;
    }

    if (popupToHide == null)
    {
        popupToHide = displayedPopup;
    }
    if (popupIframeToHide == null)
    {
        popupIframeToHide = displayedPopupIframe;
    }

    if (popupToHide != null)
    {
        popupToHide.style.display = 'none';
        popupIframeToHide.style.display = 'none';
        popupToHide = null;
        popupPartnerField = null;
    }
}

function windowPopupShow(popupId, pTop, pLeft, currElem) {


    var popup = getElemById(popupId);

    // This is a fudge to get around the bug in IE where combos are always displayed
    // on top of divisions. We place an Iframe, the same size as the popup, under the
    // popup. This sits over any combo, and hides them.

    displayedPopupIframe = getElemById(popupId + "Iframe");

    // Put the popup at the top of the display. This allows it to fully draw, and we
    // can then get its height and width. We then adjust its position so that ideally
    // it's top left is at top left of the root object, but adjusted so that the
    // entire popup is displayed within the window.
    popup.style.top = "0pt";
    popup.style.left = "0pt";

    // set position of elements
    setPosition(popup, displayedPopupIframe, pTop, pLeft);

    // set offset on iframe
    setOffSet(popup, displayedPopupIframe);

    // make visible
    popup.style.display = "inline";
    displayedPopupIframe.style.display = "inline";

    // Remember that we've displayed this popup
    displayedPopup = popup;
    displayedWindowContent = getElemById(popupId + 'Content');

    if (currElem) {
        // get the windows original scroll positions
        var scrollX = document.body.scrollLeft;
        var scrollY = document.body.scrollTop;
        currElem.focus();
        currElem.scrollIntoView(true);
        window.scrollTo(scrollX, scrollY);
    }
}

// set offset on iframe
function setOffSet(popup, displayedPopupIframe)
{
    displayedPopupIframe.style.width = popup.offsetWidth + "px";
    displayedPopupIframe.style.height = popup.offsetHeight + "px";
}

// calculate x and y positions for popup and popupiframe
function setPosition(popup, displayedPopupIframe, pTop, pLeft)
{
    // calculate max height
    var displayMax = document.body.clientHeight + document.body.scrollTop;
    if ((pTop + popup.offsetHeight) > displayMax)
    {
        pTop = displayMax - popup.offsetHeight;
    }

    // calculate max width
    displayMax = document.body.clientWidth + document.body.scrollLeft;
    if ((pLeft + popup.offsetWidth) > displayMax)
    {
        pLeft = displayMax - popup.offsetWidth;
    }

    // set x and y positions
    popup.style.top = pTop + "px";
    popup.style.left = pLeft + "px";

    displayedPopupIframe.style.top = pTop + "px";
    displayedPopupIframe.style.left = pLeft + "px";
}

//
// User has completed a selection in a popup. Transfer the selected value and key
// to the required fields in the main page and hide the popup.
//

function popupServerOK(popupId, selectedValue, selectedKey)
{
    var doc = window.parent.document;

    var popupPartnerValueName = getElemByIdAndDoc(popupId + 'popupLabelField', doc).value;
    var popupPartnerDisplayName = getElemByIdAndDoc(popupId + 'popupDisplayField', doc).value;
    var popupPartnerKeyName = getElemByIdAndDoc(popupId + 'popupIdField', doc).value;
    var popupOnSelect = getElemByIdAndDoc(popupId + 'popupOnSelect', doc).value;

    var popupPartnerValueField = getElemByIdAndDoc(popupPartnerValueName, doc);
    var popupPartnerDisplayField = getElemByIdAndDoc(popupPartnerDisplayName, doc);
    var popupPartnerKeyField = getElemByIdAndDoc(popupPartnerKeyName, doc);

    var popup = getElemByIdAndDoc(popupId, doc);
    var popupIframe = getElemByIdAndDoc(popupId + "Iframe", doc);


    if (popupPartnerValueField != null)
    {
        popupPartnerValueField.value = selectedValue;
    }

    if (popupPartnerKeyField != null)
    {
        popupPartnerKeyField.value = selectedKey;
    }

    if (popupPartnerDisplayField != null)
    {
        popupPartnerDisplayField.value = selectedValue;
    }

    popupHide(popup, popupIframe);


    if (popupOnSelect != 'undefined')
    {
        eval(popupOnSelect);
    }

}

//
// User has completed a selection in a popup. Transfer the selected value and key
// to the required fields in the main page and hide the popup.
//

function popupOK(selectedValue, selectedKey)
{
    if (popupPartnerValueField != null)
    {
        popupPartnerValueField.value = selectedValue;
    }

    if (popupPartnerKeyField != null)
    {
        popupPartnerKeyField.value = selectedKey;
    }

    popupHide();

    if (onSelectFunction != null && onSelectFunction != "")
    {
        //onSelectFunction();
        eval(onSelectFunction);
    }
}


//
// Used by colour picker to select a foreground colour given a background colour
//

function foregroundColour(backgroundColour)
{
    var result;

    var r = parseInt(backgroundColour.substr(1, 2), 16);
    var g = parseInt(backgroundColour.substr(3, 2), 16);
    var b = parseInt(backgroundColour.substr(5, 2), 16);

    if (g < 0x66)
    {
        // Reds and Blues have white text
        result = "white";
    }
    else if ((g >= 0xcc) || (r >= 0xcc))
    {
        // Light Greens and oranges have black text
        result = "black";
    }
    else
    {
        // Everything else has white text
        result = "white"
    }

    return result;
}

//
// Used by colour popup to display the highlighted colour..
//

var colourDispField = null;

function showColour(colour)
{
    if (colourDispField == null)
    {
        colourDispField = getElemById("colourDispField");
    }

    colourDispField.innerHTML = colour;
    colourDispField.style.backgroundColor = colour;
    colourDispField.style.color = foregroundColour(colour);
}

//
// User has completed a selection in a colour selector. Transfer the selected colour
// to the required key field and change the background colour of the value field to
// the colour.
//

function selectColour(colour)
{
    if (popupPartnerValueField != null)
    {
        popupPartnerValueField.style.backgroundColor = colour;
        popupPartnerValueField.style.color = foregroundColour(colour);
    }

    if (popupPartnerKeyField != null)
    {
        popupPartnerKeyField.value = colour + ":" + foregroundColour(colour);
    }

    popupHide();
}

//
// This (recursive) function is used to clean up the html of a tree popup prior to
// displaying it. We remove any extraneous whitespace between the main elements of
// the tree display. This is caused by linefeeds, tabs and spaces between the
// elements in the html source, and if left in causes the tree to be incorrectly
// spaced. Removing these also makes it easier to locate the elements used to
// expand and collaps tree nodes.
//
// The current selection from the tree is highlighted, and any previously highligh
// entry un-highlighted.
//
// Return the <div> element for the current selection if there is one.
//

function cleanTree(elem, currentSelection)
{
    var i;
    var cld;
    var result = null;

    for (i = elem.childNodes.length; i > 0; i--)
    {
        cld = elem.childNodes.item(i - 1);

        if (cld.nodeType == 3) // Text
        {
            //if ((elem.nodeType == 1) &&  // Element
            //			((elem.nodeName == "SPAN") || (elem.nodeName == "DIV")))
            //{
            //	elem.removeChild(cld);
            //}
        }
        else if (cld.nodeType == 8)    // Comment
        {
            elem.removeChild(cld);
        }
        else
        {
            var x = cleanTree(cld, currentSelection);

            if (x != null)
            {
                result = x;
            }
        }
    }

    // Set the highlight style on the current item and remove on others.

    var itm = "item_" + currentSelection;

    if ((elem.nodeType == 1) && (elem.nodeName == "A"))
    {
        if (elem.id == itm)
        {
            elem.className = "treeItem treeCurrentItem";
            result = elem.parentNode;
        }
        else
        {
            elem.className = "treeItem";
        }
    }
    return result;
}

//
// Open branches of the tree to the specified node.
//

function openTreeTo(elem)
{
    // Find the "treeLevel" containing the given node.

    if ((elem != null) && (elem.className == "treeItem"))
    {
        var p2 = elem.parentNode;

        if ((p2 != null) && (p2.className == "treeLevel") &&
            (p2.style.display != 'inline'))
        {
            // The tree branch is "closed", so open it. Get the node immediatly above it,
            // which will be the folder to expand.

            var s1 = p2.previousSibling;

            if ((s1 != null) && (s1.className == "treeItem"))
            {
                // Find the <span> containing the folders

                var c1 = s1.firstChild;

                while ((c1 != null) && (c1.nodeName != "SPAN"))
                {
                    c1 = c1.nextSibling;
                }

                if (c1 != null)
                {
                    // Open this branch and then its parent...

                    toggleTreeFolder(c1);
                    openTreeTo(s1);
                } else {
                    // s1 is the element to get the y value from need to scroll to the selected (highlighted element)                    
                    //alert("found a y position: " + findPosY(s1));
                }
            }
        }
    }
}

//
// Dislpay a tree popup. The tree is cleaned (see above) prior to display, and if there's
// a current selection, the tree expanded to highlight the selected entry.
//

function popupShowTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear)
{
    var popup = getElemById(popupId + "Content");

    popupPartnerKeyField = getElemById(partnerKeyId);

    var currentElemId = popupPartnerKeyField.value;

    if (!currentElemId) {
        currentElemId = getElemById('defaultNavOUId').value;
    }

    var currentElem = cleanTree(popup, currentElemId);

    if (currentElem != null)
    {
        openTreeTo(currentElem);
    }

    popupShow(title, popupRoot, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear, currentElem);
}

//
// Dislpay a tree popup. The tree is cleaned (see above) prior to display, and if there's
// a current selection, the tree expanded to highlight the selected entry.
//

function popupShowServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear, displayFieldId)
{
    initialiseState(popupId);
    var pTop = getScrollOffset() + ( getInnerHeight() / 2 );
    var pLeft = document.body.clientWidth / 2;
    pLeft -= 100;
    pTop -= 100;
//    var scrollTop = document.body.scrollTop;
//    if (scrollTop > 0) {
//        scrollTop = scrollTop / 2;
//    }
//    var pTop = scrollTop + (document.body.clientHeight / 2) - 60;
//    var pLeft = document.body.scrollLeft + (document.body.clientWidth / 2);

    //pLeft -= 100;
    popupShowServerTreeTopLeft(title, pTop, pLeft, popupId, partnerValueId, partnerKeyId, 0, onSelFunc, allowClear, displayFieldId);
}

function showSubjectServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, allowClear)
{
    popupShowServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, null, allowClear)
}

function showOrgainisationServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, allowClear)
{
    popupShowTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, null, allowClear)
}

function showPositionServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, allowClear)
{
    popupShowServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, null, allowClear)
}

function showBlogPopup(title, popupRoot, popupId, partnerValueId, partnerKeyId)
{
    popupShowBlog(title, popupRoot, popupId, partnerValueId, partnerKeyId)
}

function registerCloseFunc(onCloseFunc)
{
    onCloseFunction = onCloseFunc;
}


/*
 * initialises the variables needed for the popups
 */
function initialiseState(popupId) {

    var resizeImage = getElemByIdAndDoc(popupId + 'ResizeWindow', document, true);
    if (resizeImage) resizeImage.onmousedown = initResizeWindow;

    initWindows();
}

function popupShowServerTreeTopLeft(title, pTop, pLeft, popupId, partnerValueId, partnerKeyId, iteration, onSelFunc, allowClear, displayFieldId)
{
    var idfield = getElemById(popupId + "popupIdField");
    var labelfield = getElemById(popupId + "popupLabelField");
    var displayfield = getElemById(popupId + "popupDisplayField");

    idfield.value = partnerKeyId;
    labelfield.value = partnerValueId;
    displayfield.value = displayFieldId;

    var onSelect = getElemById(popupId + "popupOnSelect");
    if (onSelFunc != null)
    {
        onSelect.value = onSelFunc;
    }
    else
    {
        onSelect.value = "";
    }

    var docIframe = window.frames[popupId + 'contentIframe'].document;
    var divelem = getElemByIdAndDoc(popupId, docIframe, true);
    if (divelem == null) return;

    var idselected = getElemById(partnerKeyId).value;

    //    if(!idselected) {
    //        idselected = getElemById('defaultNavOUId').value;
    // todo look at ou pickers which should pass in the default!! otherwise no default
    //    }

    var currentElem = cleanTree(divelem, idselected);

    if (currentElem != null)
    {
        openTreeTo(currentElem);
        popupShowTopLeft(title, pTop, pLeft, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear, currentElem);
    }
    else if (idselected && iteration < 2)
    {
        iteration = iteration + 1;
        var iframe = getElemById(popupId + 'contentIframe');
        var iframeSrc = iframe.src;
        var index = iframeSrc.indexOf('&initialLeaf=');
        if (index > 0)
        {
            iframeSrc = iframeSrc.substring(0, index);
        }
        iframeSrc = iframeSrc + "&initialLeaf=" + idselected;
        iframe.src = iframeSrc;

        window.setTimeout("popupShowServerTreeTopLeft('" + title + "'," + pTop + "," + pLeft + ",'" + popupId + "','" + partnerValueId + "','" + partnerKeyId + "'," + iteration + ",'" + onSelFunc + "'," + allowClear + ",'" + displayFieldId + "')", 1000);

    }
    else
    {
        popupShowTopLeft(title, pTop, pLeft, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear);
    }
}


//
//  The tree is cleaned (see above) and expanded to highlight the selected entry.
//


//
//  The tree is cleaned (see above) and expanded to highlight the selected entry.
//


function treeOpenNode(treeId, value)
{
    var tree = getElemById(treeId);
    var currentElem = cleanTree(tree, value);

    if (currentElem != null)
    {
        openTreeTo(currentElem);
        var next = currentElem.nextSibling;
        if (next != null)
        {
            next.style.display = 'inline';
        }
        currentElem.scrollIntoView(true);
    }
}

//
// The following functions control the operation of the tree popup.
//

//
// Expand/collapse a node of a tree by toggling it's display properties.
//

function toggleTreeFolder(nd)
{
    // load images used so we get correct paths
    var openFolder = new Image();
    openFolder.src = "../images/tree/OpenFolder.gif";
    var closedFolder = new Image();
    closedFolder.src = "../images/tree/ClosedFolder.gif";
    var closedBranch = new Image();
    closedBranch.src = "../images/tree/ClosedBranch.gif";
    var openBranch = new Image();
    openBranch.src = "../images/tree/OpenBranch.gif";
    var lastClosedBranch = new Image();
    lastClosedBranch.src = "../images/tree/LastClosedBranch.gif";
    var lastOpenBranch = new Image();
    lastOpenBranch.src = "../images/tree/LastOpenBranch.gif";

    var branch = nd.parentNode;

    var branchImgElement = nd.childNodes[0];
    var folderImgElement = nd.childNodes[1];

    var isOpenBranch = (branchImgElement.src == openBranch.src);
    var isLastOpenBranch = (branchImgElement.src == lastOpenBranch.src);

    var isClosedBranch = (branchImgElement.src == closedBranch.src);
    var isLastClosedBranch = (branchImgElement.src == lastClosedBranch.src);

    // next element - will be hidden / shown as appropriate
    var elmt = branch.nextSibling;

    if (isOpenBranch || isLastOpenBranch)
    {
        // switching from open to closed - change branch image and folder image and element style
        if (isLastOpenBranch)
        {
            branchImgElement.src = lastClosedBranch.src;
        }
        else
        {
            branchImgElement.src = closedBranch.src;
        }

        folderImgElement.src = closedFolder.src;

        // hide next element
        if (elmt != null)
        {
            elmt.style.display = 'none';
        }

    }
    else if (isClosedBranch || isLastClosedBranch)
    {
        // switching from closed to open - change branch image and folder image and element style
        if (isLastClosedBranch)
        {
            branchImgElement.src = lastOpenBranch.src;
        }
        else
        {
            branchImgElement.src = openBranch.src;
        }

        folderImgElement.src = openFolder.src;

        // show next element
        if (elmt != null)
        {
            elmt.style.display = 'inline';
        }
    }
}

//
// Calendar functions
//

var ISODateFormat = "yyyy-mm-dd";

var yearPopup;
var yearButton;
var monthPopup;
var monthButton;
var monthSelected;
var yearSelected;
var dateSelected;
var CalInterval;
var CalTimeout;
var nStartingYear;
var dateNow;
var monthNow;
var yearNow;

function gotoToday()
{
    monthSelected = monthNow;
    yearSelected = yearNow;
    constructCalendar();
    //  TS-2225 Populate field with today's date.
    selectDate(dateNow);
}

function hideCalendar()
{
    if (monthPopup != null)
    {
        monthPopup.style.visibility = "hidden";
    }

    if (yearPopup != null)
    {
        yearPopup.style.visibility = "hidden";
    }
}

function padZero(num)
{
    return (num < 10) ? '0' + num : num;
}

function constructVisibleDate(d, m, y)
{
    return constructDate(VisibleDateFormat, d, m, y);
}

function constructISODate(d, m, y)
{
    return constructDate(ISODateFormat, d, m, y);
}

function constructDate(fmt, d, m, y)
{
    sTmp = fmt;
    sTmp = sTmp.replace("dd", "<e>");
    sTmp = sTmp.replace("d", "<d>");
    sTmp = sTmp.replace("<e>", padZero(d));
    sTmp = sTmp.replace("<d>", d);
    sTmp = sTmp.replace("mmm", "<o>");
    sTmp = sTmp.replace("mm", "<n>");
    sTmp = sTmp.replace("m", "<m>");
    sTmp = sTmp.replace("<m>", m + 1);
    sTmp = sTmp.replace("<n>", padZero(m + 1));
    sTmp = sTmp.replace("<o>", shortMonthName[m]);
    sTmp = sTmp.replace("yyyy", y);
    sTmp = sTmp.replace("yy", padZero(y % 100));
    return sTmp;
}

function closeCalendar()
{
    hideCalendar();

    popupOK(constructVisibleDate(dateSelected, monthSelected, yearSelected),
            constructISODate(dateSelected, monthSelected, yearSelected));
}

// Month Pulldown

function StartDecMonth()
{
    CalInterval = setInterval("decMonth()", 80);
}

function StartIncMonth()
{
    CalInterval = setInterval("incMonth()", 80);
}

function selectDate(dt)
{
    dateSelected = dt;
    closeCalendar();
}

function incMonth()
{
    monthSelected++;

    if (monthSelected > 11)
    {
        monthSelected = 0;
        yearSelected++;
    }

    constructCalendar();
}

function decMonth()
{
    monthSelected--;

    if (monthSelected < 0)
    {
        monthSelected = 11;
        yearSelected--;
    }

    constructCalendar()
}

function constructMonth()
{
    sHTML = "<table cellspacing=0 onmouseover='clearTimeout(CalTimeout)' onmouseout='clearTimeout(CalTimeout);CalTimeout=setTimeout(\"popDownMonth()\",100);event.cancelBubble=true'>";

    for (i = 0; i < 12; i++)
    {
        sName = fullMonthName[i];
        sHTML += "<tr><td class='CalDropdownNormal' id='m" + i + "' onmouseover='this.className=\"CalDropdownSelect\"' onmouseout='this.className=\"CalDropdownNormal\"' onclick='monthSelected=" + i + ";constructCalendar();popDownMonth();event.cancelBubble=true'>" + sName + "</td></tr>";
    }

    sHTML += "</table>";

    monthPopup.innerHTML = sHTML;
}

function popUpMonth()
{
    monthPopup.style.left = monthButton.offsetLeft + "px";
    monthPopup.style.top = (monthButton.offsetTop - 26) + "px";
    monthPopup.style.visibility = "visible";
}

function popDownMonth()
{
    monthPopup.style.visibility = "hidden";
}

// Year Pulldown

function incYear()
{
    // year display till 2200.

    if (nStartingYear <= 2194)
    {
        nStartingYear++;
        constructYear();
    }
}

function decYear()
{
    // year display from 1800

    if (nStartingYear > 1807)
    {
        nStartingYear--;
        constructYear();
    }
}

function selectYear(nYear)
{
    yearSelected = nStartingYear + parseInt(nYear);
    constructCalendar();
    popDownYear();
}

function constructYear()
{
    for (i = 0; i < 7; i++)
    {
        getElemById("y" + i).innerHTML = (i + nStartingYear);
    }
}

function popDownYear()
{
    clearInterval(CalInterval);
    clearTimeout(CalTimeout);

    yearPopup.style.visibility = "hidden";
}

function popUpYear()
{
    nStartingYear = yearSelected - 3;
    constructYear();

    yearPopup.style.left = yearButton.offsetLeft + "px";
    yearPopup.style.top = (yearButton.offsetTop - 26) + "px";
    yearPopup.style.visibility = "visible";
}

// Calendar body

function constructCalendar()
{
    var startDate = new Date(yearSelected, monthSelected, 1);
    var endDate = new Date(yearSelected, monthSelected + 1, 1);
    endDate = new Date(endDate - (24 * 60 * 60 * 1000));
    numDaysInMonth = endDate.getDate();

    sHTML = "<table border='0' cellspacing='0'><tr>";

    for (i = 0; i < 7; i++)
    {
        sHTML += "<td class='CalDayName'>" + dayName[i] + "</td>";
    }

    datePointer = 1 - startDate.getDay() + weekStartAt;

    for (r = 0; r < 6; r++)
    {
        sHTML += "</tr><tr>";

        for (i = 0; i < 7; i++)
        {
            if ((datePointer <= 0) || (datePointer > numDaysInMonth))
            {
                sHTML += "<td>&nbsp;";
            }
            else
            {
                var sStyle = "CalNormalDay";
                //regular day

                // today

                if ((datePointer == dateNow) && (monthSelected == monthNow) && (yearSelected == yearNow))
                {
                    sStyle = "CalCurrentDay";
                }

                sHTML += "<td class='CalNormal' onmouseover='this.className=\"CalSelect\"' onmouseout='this.className=\"CalNormal\"' onclick='selectDate(" + datePointer + ");'>";
                sHTML += "<span class='" + sStyle + "'>" + datePointer + "</span>";
            }

            sHTML += "</td>";
            datePointer++;
        }
    }

    sHTML += "</tr></table>";

    getElemById("CalBody").innerHTML = sHTML;
    monthButton.innerHTML = fullMonthName[monthSelected];
    yearButton.innerHTML = yearSelected;
}


//
// Dislpay a Calendar popup
//
//  var popupRoot=getElemById(popupRootId);
function popupShowCalendarX(title, popupRootId, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear) {
    var popupRoot = getElemById(popupRootId);
    popupShowCalendar(title, popupRoot, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear);
}

function popupShowCalendar(title, popupRoot, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear)
{


    today = new Date();
    dateNow = today.getDate();
    monthNow = today.getMonth();
    yearNow = today.getYear();

    if (yearNow < 1000)
    {
        yearNow += 1900;
    }

    yearButton = getElemById("spanYear");
    yearPopup = getElemById("datePickerYear");

    monthButton = getElemById("spanMonth");
    monthPopup = getElemById("datePickerMonth");

    // display todays date

    document.getElementById("CalToday").innerHTML =
    dayName[(today.getDay() - weekStartAt == -1) ? 6 : (today.getDay() - weekStartAt)] +
    ", " + dateNow + " " + fullMonthName[monthNow].substring(0, 3) + " " + yearNow;

    // Determin what seperator character is being used by the date format

    formatChar = " ";
    aFormat = ISODateFormat.split(formatChar);

    if (aFormat.length < 3)
    {
        formatChar = "/";
        aFormat = ISODateFormat.split(formatChar);

        if (aFormat.length < 3)
        {
            formatChar = ".";
            aFormat = ISODateFormat.split(formatChar);

            if (aFormat.length < 3)
            {
                formatChar = "-";
                aFormat = ISODateFormat.split(formatChar);

                if (aFormat.length < 3)
                {
                    alert("Invalid date format");
                    // invalid date format
                    formatChar = "";
                }
            }
        }
    }

    // See if there is a date in the id field already.

    tokensChanged = 0;

    if (formatChar != "")
    {
        // use user's date

        aData = getElemById(partnerKeyId).value.split(formatChar);

        for (i = 0; i < 3; i++)
        {
            if ((aFormat[i] == "d") || (aFormat[i] == "dd"))
            {
                dateSelected = parseInt(aData[i], 10);
                tokensChanged++;
            }
            else if ((aFormat[i] == "m") || (aFormat[i] == "mm"))
            {
                monthSelected = parseInt(aData[i], 10) - 1;
                tokensChanged++;
            }
            else if (aFormat[i] == "yyyy")
                {
                    yearSelected = parseInt(aData[i], 10);
                    tokensChanged++;
                }
                else if (aFormat[i] == "mmm")
                    {
                        for (j = 0; j < 12; j++)
                        {
                            if (aData[i] == fullMonthName[j])
                            {
                                monthSelected = j;
                                tokensChanged++;
                            }
                        }
                    }
        }
    }

    // If no valid date already in field, use todays date.

    if ((tokensChanged != 3) || isNaN(dateSelected) || isNaN(monthSelected) || isNaN(yearSelected))
    {
        dateSelected = dateNow;
        monthSelected = monthNow;
        yearSelected = yearNow;
    }

    constructCalendar();
    constructMonth();

    popupShow(title, popupRoot, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear);
}

function initWindows(e) {

    document.body.onmouseup = stopMove;
    document.body.onmousemove = moveWindow;
    document.body.ondragstart = cancelEvent;
    document.body.onselectstart = cancelEvent;
}

// End of file

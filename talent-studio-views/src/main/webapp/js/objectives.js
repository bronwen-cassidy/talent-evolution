// objective specific functions

function addObjective() {
    var frm = document.forms['objsetfrmid'];
    var target = getElemById('targetId');
    target.name = "_target2";
    target.value = "_target2";
    frm.submit();
}

function removeObjective(objectiveIndex) {
    var frm = document.forms['objsetfrmid'];
    var target = getElemById('targetId');
    target.name = "_target3";
    target.value = "_target3";

    var deleteElem = getElemById('deleteIdx');
    deleteElem.value = objectiveIndex;
    frm.submit();
}

function submitCancel() {
    var frm = document.forms['objsetfrmid'];
    var hiddFld = getElemById('cancelId');
    hiddFld.name = "_cancel";
    hiddFld.value = "_cancel";
    frm.submit();
}

function hideElement(elemId) {
    var doc = parent.document;
    if (!doc) doc = document;
    var hiddenElem = getElemByIdAndDoc(elemId, doc, true);
    hiddenElem.style.display = 'none';
}

function showElement(hiddenDivId) {
    var doc = parent.document;
    if (!doc) doc = document;
    var hiddenElem = getElemByIdAndDoc(hiddenDivId, doc, true);
    hiddenElem.style.display = "inline";
}

function confirmSetValueAndSubmit(frm, message, hiddenFldId, value, finishFieldId) {
    var confirmed = confirm(message);
    if (confirmed == '1') {
        getElemById(hiddenFldId).value = value;
        if(finishFieldId) {
            var finishField = getElemById(finishFieldId);
            finishField.name="_finish";
            finishField.value="_finish";
        }
        document.forms[frm].submit();
    }
}

function setParentObjectiveInfo(listElementId, hiddenFieldId, targetAreaId, itemId) {
    // find the objective
    var listElem = getElemById("ab" + listElementId);
    var description = listElem.innerHTML;

    var hiddenField = getElemById(hiddenFieldId);
    hiddenField.value = listElementId;

    var targetAreaElem = getElemById(targetAreaId);
    targetAreaElem.innerHTML = description;

    var hiddenDescElem = getElemById(targetAreaId + "a");
    hiddenDescElem.value = description;
}

function swapLiStyle(elem, borderStyleName) {
    elem.style.borderStyle = borderStyleName;
}

function updateNumberField(targetId, sourceId) {
    var source = getElemById(sourceId);
    var target = getElemById(targetId);
    var num = target.innerHTML;
    if (num) {
        target.innerHTML = parseInt(num) + parseInt(source.value);
    } else {
        target.innerHTML = parseInt(source.value);
    }
}

var approveObjectiveCallback = function(success) {
    if (success) {
        alert("Successfully updated objective");
    } else {
        // display some error message
    }
}

/*
* approves an objective, will have a callback to determine success or failure this will load the td with the status field
*/
function approveObjective(itemId, message) {
    // call out to the confirm
    var confirmed = confirm(message);
    if (confirmed == '1') {
        objectivesBean.approveObjective(itemId, approveObjectiveCallback);
    }
}

/*
 * submits the form when an organisation unit has been selected/changed so the correct objectives can be loaded and displayed. 
*/
function ouObjectivesSubmit() {
    document.forms['viewOuObjectives'].submit();
}

function clearObjectiveTextFields(hiddenFieldId, parentTableId, blogCommentFieldId, index) {

    var hiddenFld = getElemById(hiddenFieldId);
    var blogCommentFld = getElemById(blogCommentFieldId);
    var blogCommentHiddenFld = getElemById(blogCommentFieldId + "a");

    hiddenFld.value = "";
    blogCommentFld.innerHTML = "";
    blogCommentHiddenFld.value = "";
}

function addSelected(targetSelectElemId, sourceSelectId) {

    var targetSelectElem = getElemById(targetSelectElemId);
    // get the multiselect element
    var sourceSelectElem = getElemById(sourceSelectId);
    var opts = sourceSelectElem.options;
    for (var i = 0; i < opts.length; i++) {
        var selectElem = opts[i];
        if (selectElem.selected) {
            // loop through the target elements options so we do not duplicate entries
            var hasEntry = false;
            for (var x = 0; x < targetSelectElem.options.length; x++) {
                if (targetSelectElem.options[x].value == selectElem.value) {
                    hasEntry = true;
                    break;
                }
            }
            if (!hasEntry) targetSelectElem.options[targetSelectElem.options.length] = new Option(selectElem.text, selectElem.value);
        }
    }
}

function assignSelectedToAll(selectId, targetTblId) {

    var tble = getElemById(targetTblId);
    var allSelectBoxes = tble.getElementsByTagName("SELECT");

    for (var i = 0; i < allSelectBoxes.length; i++) {
        addSelected(allSelectBoxes[i].id, selectId);
    }
}

function removeSelected(targetSelectElemId, sourceElemId) {

    var sourceSelectElem = getElemById(sourceElemId);
    var childElems = sourceSelectElem.options;

    var selectElem = getElemById(targetSelectElemId);
    // remove the selected options
    for (var i = 0; i < selectElem.options.length; i++) {
        var selectedOption = selectElem.options[i];
        if (selectedOption.selected) {
            // uncheck the checkbox
            for (var j = 0; j < childElems.length; j++) {
                if (childElems[j].value == selectedOption.value) childElems[j].selected = false;                
            }
            selectElem.options[i] = null;
            i--;
        }
    }
}

function removeAllSelections(tbleId, divId) {

    clearSelections(tbleId);
    clearCheckedAssessors(divId);
}

// clears all options from all select boxes within the given table given by the tbleId (table id)
function clearSelections(tbleId) {

    var tble = getElemById(tbleId);
    var allSelectBoxes = tble.getElementsByTagName("SELECT");

    for (var i = 0; i < allSelectBoxes.length; i++) {
        var selectElem = allSelectBoxes[i];
        clearOptions(selectElem);
    }
}

// removes all options from a select box the selectElem being the element not an ID
function clearOptions(selectElem) {

    for (var j = 0; j < selectElem.options.length; j++) {
        selectElem.options[j] = null;
        j--;
    }
}

function clearCheckedAssessors(divId) {

    var selectElem = getElemById(divId);
    var childElems = selectElem.options;

    for (var k = 0; k < childElems.length; k++) {
        childElems[k].selected = false;
    }
}

function saveAssessors(tbleId) {

    // get all the select boxes
    var tble = getElemById(tbleId);
    var selectElems = tble.getElementsByTagName("SELECT");


    for (var i = 0; i < selectElems.length; i++) {
        var users = new Array();
        var selectElem = selectElems[i];
        var selectedElemName = selectElem.name;

        var objectiveId = selectedElemName.substring(selectedElemName.lastIndexOf("_") + 1, selectedElemName.length)

        for (var j = 0; j < selectElem.options.length; j++) {
            var opt = selectElem.options[j];
            var userId = opt.value;
            users.push(userId);
        }

        objectivesBean.saveAssessors(objectiveId, users);
    }
}

// called when the popup is closed in order to reset the assessors information
// targetTableId the table containing the list of selected assessors for each objective
// the hidden field that contains the objectiveSet from which we access the current set of objectives
function resetAssessors(targetTableId, objSetFieldId, userSelectorDiv) {

    var tble = getElemById(targetTableId);
    clearCheckedAssessors(userSelectorDiv);

    // get all the select elements
    var selectElems = tble.getElementsByTagName("SELECT");
    // the select element's name is post-fixed with the objective id
    var i = 0;

    for (; i < selectElems.length; i++) {

        var selectElem = selectElems[i];
        clearOptions(selectElem);
        var objectiveId = selectElem.name.substring(objSetFieldId.length);

        objectivesBean.findAssessors(objectiveId, function(str) {

            var index = 0;
            for (var prop in str) {
                var userObjIdArray = prop.split(':');
                var userId = userObjIdArray[0];
                //var foundElem = getElemById(objSetFieldId + userObjIdArray[1]);
                var elemName = objSetFieldId + userObjIdArray[1];
                var foundElem = document.getElementsByName(elemName)[0];
                foundElem.options[index] = new Option(str[prop], userId);
                index++;
            }
        });
    }
}
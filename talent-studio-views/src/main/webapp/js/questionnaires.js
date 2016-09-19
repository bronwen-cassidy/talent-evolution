var DYNAMIC_LINE_ITEM_ELEMENT_TEXT = 1;
var DYNAMIC_LINE_ITEM_ELEMENT_INTEGER_POSITIVEINTEGER = 2;
var DYNAMIC_LINE_ITEM_ELEMENT_TEXTAREA_TEXTBOX = 3;
var DYNAMIC_LINE_ITEM_ELEMENT_DATE = 4;
var DYNAMIC_LINE_ITEM_ELEMENT_SELECT_STRUCT = 5;
var DYNAMIC_LINE_ITEM_ELEMENT_RADIO = 6;
var DYNAMIC_LINE_ITEM_ELEMENT_CHECKBOX = 7;
var DYNAMIC_LINE_ITEM_ELEMENT_MULTISELECT = 8;
var DYNAMIC_LINE_ITEM_ELEMENT_ORGANISATION = 9;
var DYNAMIC_LINE_ITEM_ELEMENT_SUBJECT = 10;
var DYNAMIC_LINE_ITEM_ELEMENT_POSITION = 11;
var DYNAMIC_LINE_ITEM_ELEMENT_COMMENT = 12;
var DYNAMIC_LINE_ITEM_DISABLE_ROW = 13;
var DYNAMIC_LINE_ITEM_POSITION = 14;
var errorCount = 0;

function loadUserName(userId, noDetailsMessage) {
    subjectBean.getFullName(userId, { callback:function(fullName) {

        if (fullName == null) {
            fullName = noDetailsMessage;
        }
        var nameDivElem = getElemById('username');
        if (nameDivElem != null) {
            nameDivElem.innerHTML = fullName;
        }
    }});
}


function saveUpdateDeleteQuestionnaireBlogComments(fieldId, queId, daId, attrIdField, dynamicPosition, errorId) {
    var textValue = getElemById(fieldId).value;
    var attrElem = getElemById(attrIdField);
    var errorElem = getElemById(errorId);
    var queDefId = getElemById("queDefIdxx").value;
    var attrId = attrElem.value;
    if (attrId == 'null') attrId = '';
    clearError(errorElem);
    questionnaireBean.saveUpdateDeleteQuestionnaireBlogComments(queId, daId, attrId, dynamicPosition, textValue, queDefId,
            { callback:function(attributeResult) {

                attrElem.value = attributeResult.attributeId;
                displayError(attributeResult, errorElem);
            },async:false
            });
}

function saveUpdateDeleteQuestionnaireDate(fieldId, queId, daId, attrIdField, errorId) {

    var dateFld = getElemById(fieldId);
    var dateValue = dateFld.value;
    var attrElem = getElemById(attrIdField);
    var errorElem = getElemById(errorId);
    var attrId = attrElem.value;
    var dynamicPosition = parseLevel(dateFld.name);
    if (attrId == 'null') attrId = '';
    var queDefId = getElemById("queDefIdxx").value;
    clearError(errorElem);
    questionnaireBean.saveUpdateDeleteQuestionnaireDate(queId, daId, attrId, dynamicPosition, dateValue, queDefId,
            { callback:function(attributeResult) {
                attrElem.value = attributeResult.attributeId;
                displayError(attributeResult, errorElem);
            },async:false
            });
}

function saveUpdateDeleteQuestionnaireList(fieldId, queId, daId, attrIdField, errorId) {

    var selectElem = getElemById(fieldId);
    var pickerListValue = selectElem.value;
    var attrElem = getElemById(attrIdField);
    var errorElem = getElemById(errorId);
    var dynamicPosition = parseLevel(selectElem.name);
    var attrId = attrElem.value;
    if (attrId == 'null') attrId = '';
    var queDefId = getElemById("queDefIdxx").value;
    clearError(errorElem);
    questionnaireBean.saveUpdateDeleteQuestionnaireList(queId, daId, attrId, dynamicPosition, pickerListValue, queDefId,
            { callback:function(attributeResult) {
                attrElem.value = attributeResult.attributeId;
                displayError(attributeResult, errorElem);
                displaySumEnums(attributeResult);
            },async:false
            });
}

function saveUpdateDeleteQuestionnairePosition(fieldId, queId, daId, attrIdField, errorId) {
    saveUpdateDeleteQuestionnaireSubject(fieldId, queId, daId, attrIdField, errorId);
}

function saveUpdateDeleteQuestionnaireOrganisation(fieldId, queId, daId, attrIdField, errorId) {
    saveUpdateDeleteQuestionnaireSubject(fieldId, queId, daId, attrIdField, errorId);
}

function saveUpdateDeleteQuestionnaireSubject(fieldId, queId, daId, attrIdField, errorId) {

    var pickerListValue = getElemById(fieldId).value;
    var attrElem = getElemById(attrIdField);
    var dynamicPosition = parseLevel(attrElem.name);
    var errorElem = getElemById(errorId);
    var queDefId = getElemById("queDefIdxx").value;
    var attrId = attrElem.value;
    if (attrId == 'null') attrId = '';

    clearError(errorElem);
    questionnaireBean.saveUpdateDeleteQuestionnaireSubject(queId, daId, attrId, dynamicPosition, pickerListValue, queDefId,
            { callback:function(attributeResult) {

                attrElem.value = attributeResult.attributeId;
                displayError(attributeResult, errorElem);
            },async:false
            });
}

function deleteQuestionnaireAttribute(attributeId) {
    questionnaireBean.deleteQuestionnaireAttribute(attributeId);
}

function saveDeleteQuestionnaireCheckBox(fieldId, queId, daId, attrIdField, errorId) {

    var checkbox = getElemById(fieldId);
    var idValue = checkbox.value;
    var attrElem = getElemById(attrIdField);
    var dynamicLevel = parseLevel(attrElem.name);
    var errorElem = getElemById(errorId);
    var attrId = attrElem.value;
    if (attrId == 'null') attrId = '';
    var queDefId = getElemById("queDefIdxx").value;
    var action = "insert";
    if (!checkbox.checked) {
        action = "delete";
    }
    clearError(errorElem);
    questionnaireBean.saveDeleteQuestionnaireCheckBox(queId, daId, attrId, idValue, action, queDefId, dynamicLevel,
            { callback:function(attributeResult) {
                attrElem.value = attributeResult.attributeId;
                displayError(attributeResult, errorElem);
                displaySumEnums(attributeResult);
            },async:false
            });
}

function saveUpdateDeleteQuestionnaireMultiSelect(fieldId, queId, daId, attrIdField, errorId) {
    var listValues = getElemById(fieldId).options;
    var newArray = new Array();

    for (var i = 0; i < listValues.length; i++) {
        var selectElem = listValues[i];
        if (selectElem.selected) {
            newArray[i] = selectElem.value;
        }
    }

    var attrElem = getElemById(attrIdField);
    var dynamicPosition = parseLevel(attrElem.name);
    var errorElem = getElemById(errorId);
    var queDefId = getElemById("queDefIdxx").value;
    var attrId = attrElem.value;
    if (attrId == 'null') attrId = '';
    clearError(errorElem);

    questionnaireBean.saveUpdateDeleteQuestionnaireMultiSelect(queId, daId, attrId, dynamicPosition, newArray, queDefId,
            { callback:function(attributeResult) {
                attrElem.value = attributeResult.attributeId;
                displayError(attributeResult, errorElem);
                displaySumEnums(attributeResult);
            },async:false
            });
}

function saveUpdateDeleteQuestionnaire(fieldId, queId, daId, attrIdField, errorId) {
    var currentElem = getElemById(fieldId);
    var dynamicPosition = parseLevel(currentElem.name);
    saveUpdateDeleteQuestionnaireGeneric(fieldId, queId, daId, attrIdField, dynamicPosition, errorId);
}

function saveUpdateQuestionnaireAttributeDisabled(triggerElem, queId, lineItemId, dynamicPosition) {
    var checked = false;
    if (triggerElem.checked) {
        checked = true;
    }
    var queDefId = getElemById("queDefIdxx").value;
    questionnaireBean.saveUpdateQuestionnaireAttributeDisabled(checked, queId, lineItemId, dynamicPosition, queDefId,
            { callback:function(attributeResult) {
                displaySumEnums(attributeResult);

            },async:false
            });
}

var completedComit = true;

function saveUpdateDeleteQuestionnaireGeneric(fieldId, queId, daId, attrIdField, dynamicPosition, errorId) {
    var textValue = getElemById(fieldId).value;
    var attrElem = getElemById(attrIdField);
    var errorElem = getElemById(errorId);
    var queDefId = getElemById("queDefIdxx").value;
    var attrId = attrElem.value;
    if (attrId == 'null') attrId = '';
    clearError(errorElem);

    if (completedComit) {
        completedComit = false;
        try {
            questionnaireBean.saveUpdateDeleteQuestionnaireGeneric(queId, daId, attrId, dynamicPosition, textValue, queDefId,
                    { callback:function(attributeResult) {
                        attrElem.value = attributeResult.attributeId;
                        displayError(attributeResult, errorElem);
                        displaySumEnums(attributeResult);

                    },async:false
                    });
        } catch(e) {

            alert("Error occured please try again later.");
        }
    }
    completedComit = true;
}

function displayError(attributeResult, errorElem) {
    if (attributeResult.errorMessage) {
        alert(attributeResult.errorMessage);
        errorElem.innerHTML = attributeResult.errorMessage;
        errorElem.style.display = 'inline';
        errorCount++;
    }
    // reset the javascript polling info
    pollTimeOut(thisTimeoutAmount, timeOutWarningMessage, logoutUrl);
}

function displaySumEnums(attributeResult) {
    for (var ans in attributeResult.answers) {
        var answer = attributeResult.answers[ans];
        var sumEnumElem = getElemById("SUM_ENUM" + answer.id);
        if (sumEnumElem) {
            sumEnumElem.innerHTML = answer.value;
        }
    }
    var lastUpdate = getElemById("LASTUP");
    if (lastUpdate) {
        if (attributeResult.dateModified != null) {
            lastUpdate.innerHTML = attributeResult.dateModified;
        }
    }
    var lastUpdateBy = getElemById("LASTUPBY");
    if (lastUpdateBy) {
        if (attributeResult.modifiedBy != null) {
            lastUpdateBy.innerHTML = attributeResult.modifiedBy;
        }
    }
}

function clearError(errorElem) {
    if (errorElem) {
        errorElem.innerHTML = '';
        errorElem.style.display = 'none';
        errorCount = 0;
    }
}

var updateRepublishStatus = function(republishResults) {

    var divContent = "Republished at: " + republishResults.completedDate.toLocaleString();

    var usersAdded = getUserList(republishResults.usersAdded);
    var usersAddedTotal = getArrayTotal(republishResults.usersAdded);
    divContent += '<br/>';

    if (usersAddedTotal != 0)       divContent += '<a title="' + usersAdded + '">';

    divContent += usersAddedTotal + ' users added.';
    if (usersAddedTotal != 0) divContent += '</a>';
    divContent += '<br/>';

    var usersRemoved = getUserList(republishResults.usersRemoved);
    var usersRemovedTotal = getArrayTotal(republishResults.usersRemoved);


    if (usersRemovedTotal != 0)  divContent += '<a title="' + usersRemoved + '">';
    divContent += usersRemovedTotal + ' users removed.';
    if (usersRemovedTotal != 0) divContent += '</a>';

    var resultsDiv = getElemById('republishSummary_' + republishResults.queId);

    if (resultsDiv != null) {
        resultsDiv.innerHTML = divContent;
    }
};

function getArrayTotal(array)
{
    if (array != null) return array.length;
    else return 0;
}

function getUserList(array)
{
    var text = "";
    if (array != null && array.length > 0)
    {
        for (var i = 0; i < array.length; i++)
        {
            text += array[i];
            if (i != (array.length - 1)) text += ", ";
            else text += ".";
        }
    }
    return text;
}

function republishWorkflow(queId, userId, republishMessage, republishAtMsg, usersAddedMsg, usersRemovedMsg) {
    var nameDivElem = getElemById('republishSummary_' + queId);
    if (nameDivElem != null) {
        nameDivElem.innerHTML = republishMessage;
    }
    questionnaireBean.getRepublishResults(queId, userId, { callback:function(republishResults) {

        var divContent = republishAtMsg + ": " + republishResults.completedDate.toLocaleString();

        var usersAdded = getUserList(republishResults.usersAdded);
        var usersAddedTotal = getArrayTotal(republishResults.usersAdded);
        divContent += '<br/>';

        if (usersAddedTotal != 0)       divContent += '<a title="' + usersAdded + '">';

        divContent += usersAddedTotal + ' ' + usersAddedMsg;
        if (usersAddedTotal != 0) divContent += '</a>';
        divContent += '<br/>';

        var usersRemoved = getUserList(republishResults.usersRemoved);
        var usersRemovedTotal = getArrayTotal(republishResults.usersRemoved);


        if (usersRemovedTotal != 0)  divContent += '<a title="' + usersRemoved + '">';
        divContent += usersRemovedTotal + ' ' + usersRemovedMsg;
        if (usersRemovedTotal != 0) divContent += '</a>';

        var resultsDiv = getElemById('republishSummary_' + republishResults.queId);

        if (resultsDiv != null) {
            resultsDiv.innerHTML = divContent;
        }

    }});
}

function removeOptions(selectbox)
{
    var i;
    for(i=selectbox.options.length-1;i>=0;i--)
    {
        selectbox.remove(i);
    }
}

function testLinkableOptions() {
    // on first load all options are shown none are hidden so now we just need to hide the ones that do not have a selected parent
    $('select' + ' option.linked').each(function () {
        var requiresIds = $(this).attr('requires');

        if(requiresIds) {
            // handle dynamic line items
            var dynamicIndex = '';
            var newRequiresId = requiresIds;
            // check to see if the string end with an _ we must remove this
            if(requiresIds.indexOf('_') >= 0) {

                // grab this index and store it
                dynamicIndex = requiresIds.substring(requiresIds.indexOf('_'), requiresIds.length);
                newRequiresId = requiresIds.substring(0, requiresIds.indexOf('_') );
            }
            //console.log("newRequiresIds is now " + newRequiresId);
            var requiresArray = newRequiresId.split(',');
            var foundCount = 0;

            for (var i = 0; i < requiresArray.length; i++) {

                if($("option[linkId='" + requiresArray[i] + dynamicIndex +"']").is(":selected") || $("option[linkId='" + requiresArray[i] +"']").is(":selected")) {
                    foundCount++;
                }
            }

            if(parseInt(foundCount) != requiresArray.length) {
                //console.log("hiding the option with linkId " + $(this).attr('linkId'));
                $(this).remove();
            }
        }
    });
}

function addDynamicLineItemRow(tableId, uniqueGeneratorFieldId, queId) {

    var tblBody = getElemById(tableId).tBodies[0];

    var hiddenElem = getElemById(uniqueGeneratorFieldId);
    var index = hiddenElem.value;
    var length = tblBody.rows.length;

    var newRow = tblBody.rows[length - 2].cloneNode(true);

    var newTds = newRow.getElementsByTagName("TD");
    hiddenElem.value = parseInt(index) + 1;
    var increaseByOne = 1;

    var daId = 0;

    var eventNumber = 0;
    var lineItem;
    var dynamicLevel = getLevel(newTds) + 1;
    showDeleteRowButton(newRow);

    assignRowId(newRow);

    for (var i = 0; i < newTds.length; i++) {
        var newTd = newTds[i];
        var cellElems = newTd.getElementsByTagName('*');
        daId = getDynamicNodeId(cellElems);
        lineItem = getDynamicLineItem(cellElems);
        eventNumber = 0;
        increaseByOne++;
        var managerWriteOnly = getIsManagerWriteOnlyState(cellElems);

        for (var j = 0; j < cellElems.length; j++) {
            var currentElem = cellElems[j];

            var elemId = currentElem.id;

            setAttributeIdToEmpty(currentElem);

            if (eventNumber == 0)eventNumber = getDynamicEventState(cellElems);

            if (currentElem.nodeName == 'INPUT' || currentElem.nodeName == 'SELECT') {
                var itemIndex = eval(index) + increaseByOne;
                setElementNameId(currentElem, itemIndex, dynamicLevel);

                currentElem.disabled = managerWriteOnly;

                if (currentElem.nodeName == 'SELECT') {

                    // todo update the linkId and referenceId
                    var options = currentElem.options;

                    //we will now replace the new select with the complete stored on

                    var ops = selectMap[elemId];
                    if(ops) {
                        //$('#' + elemId).html(ops);
                        options = ops.clone(true);
                        removeOptions(currentElem);
                    }

                    for(var m = 0; m < options.length; m++) {
                        var linkId = options[m].getAttribute("linkId");
                        var requireIds = options[m].getAttribute("requires");
                        var postfix = "_" + (parseInt(dynamicLevel) + 1);
                        //var info = options[m].text;
                        //var val = options[m].value;                        
                        if(linkId) {
                            if(linkId.indexOf("_") >= 0) {
                                // remove the postfix and add a new one
                                linkId = linkId.substring(0, linkId.indexOf('_'));
                            }
                            options[m].setAttribute("linkId", linkId + postfix);
                            options[m].setAttribute("id", "pp_" + linkId + postfix);
                        }
                        // now requiresId          
                        if(requireIds) {
                            // todo this will always clear the options!! so nothing further to do except we now need to add to the selectMap!! all linkables!!
                            // todo need to trigger the javascript call
                            if(requireIds.indexOf("_") >= 0) {
                                requireIds = requireIds.substring(0, requireIds.indexOf('_'));
                            }
                            options[m].setAttribute("requires", requireIds + postfix);
                        }
                        //options[m].text = info;
                        //options[m].value = val;
                        if(ops) {
                            currentElem.add(options[m]);
                        }
                    }
                    if(ops) {
                        // see if we have the new select in
                        if(!selectMap[currentElem.id]) {
                            selectMap[currentElem.id] = options;
                        }
                    }
                    currentElem.options[0].selected = 'true';
                    // need to remove the cloned select options and replace them with ops                    
                    addDynamicLineItemRowEvent(currentElem, queId, daId, eventNumber, lineItem, tableId, newRow.id);


                } else if (currentElem.type == 'text') {
                    resetElementTextAndValue(currentElem);
                    addDynamicLineItemRowEvent(currentElem, queId, daId, eventNumber, lineItem, tableId, newRow.id);

                } else if (currentElem.type == 'checkbox') {
                    currentElem.checked = false;
                    addDynamicLineItemRowEvent(currentElem, queId, daId, eventNumber, lineItem, tableId, newRow.id);
                } else if (currentElem.type == 'radio') {
                    currentElem.checked = false;
                    addDynamicLineItemRowEvent(currentElem, queId, daId, eventNumber, lineItem, tableId, newRow.id);
                } else if (currentElem.type == 'button') {
                    addDynamicLineItemRowEvent(currentElem, queId, daId, eventNumber, lineItem, tableId, newRow.id);
                }

            } if (currentElem.nodeName == 'TEXTAREA') {

                var itemIndex = eval(index) + increaseByOne;
                currentElem.disabled = managerWriteOnly;
                setElementNameId(currentElem, itemIndex, dynamicLevel);
                addDynamicLineItemRowEvent(currentElem, queId, daId, eventNumber, lineItem, tableId, newRow.id);

                currentElem.selected = false;
                resetElementTextAndValue(currentElem);

            } else if (currentElem.nodeName == 'IMG') {
                if (currentElem.className == 'spellClass') {
                    currentElem.style.display = 'none';
                }

            } else {
                /*  var currentElemId = currentElem.id;
                 if (currentElemId) {
                 var itemIndex = eval(index) + increaseByOne;

                 setElementId(currentElem, itemIndex, dynamicLevel);
                 resetElementTextAndValue(currentElem);
                 currentElem.innerHTML = '';
                 }
                 */
            }
        }
    }
    var lastRow = tblBody.rows[length - 1];
    tblBody.insertBefore(newRow, lastRow);
    hiddenElem.value = parseInt(index) + 1;
    // todo try enter here add the function call
    testLinkableOptions();
}

function showDeleteRowButton(newRow) {

    var newTds = newRow.getElementsByTagName("TD");
    var lastTd = newTds[newTds.length - 1];
    var cellElems = lastTd.getElementsByTagName('INPUT');
    for (var j = 0; j < cellElems.length; j++) {
        var currentElem = cellElems[j];
        if (currentElem.style.display == 'none') {
            currentElem.style.display = 'inline';
        }
    }
}

function assignRowId(newRow) {
    var name = "litm_row_";
    var array = newRow.id.split(name);

    var idvalue = array[1];
    var newRowValue = parseInt(idvalue) + 1;
    newRow.id = name + newRowValue;

}

function resetElementTextAndValue(currentElem) {
    currentElem.text = '';
    currentElem.value = '';
}

function setElementNameId(currentElem, itemIndex, dynamicLevel) {

    var currentElemName = currentElem.name;
    currentElem.setAttribute("name", increaseNameWithValue(currentElemName, itemIndex, dynamicLevel));
    setElementId(currentElem, itemIndex, dynamicLevel);
}

function setElementId(currentElem, itemIndex, dynamicLevel) {
    var currentElemId = currentElem.id;
    currentElem.setAttribute("id", increaseValueId(currentElemId, itemIndex, dynamicLevel));
}

function addDynamicLineItemRowEvent(currentElem, queId, daId, eventNumber, lineItem, tableId, rowId) {
    switch (eventNumber) {
        case DYNAMIC_LINE_ITEM_ELEMENT_TEXT :
            currentElem.onblur = function() {

                saveUpdateDeleteQuestionnaire(currentElem.id, queId, daId, currentElem.id + '_attid', currentElem.id + '_error');

            };
            break;
        case DYNAMIC_LINE_ITEM_ELEMENT_INTEGER_POSITIVEINTEGER :
            currentElem.onblur = function() {
                saveUpdateDeleteQuestionnaire(currentElem.id, queId, daId, currentElem.id + '_attid', currentElem.id + '_error');

            };
            break;
        case DYNAMIC_LINE_ITEM_ELEMENT_TEXTAREA_TEXTBOX :
            currentElem.onblur = function() {
                saveUpdateDeleteQuestionnaire(currentElem.id, queId, daId, currentElem.id + '_attid', currentElem.id + '_error');
            };
            break;
        case DYNAMIC_LINE_ITEM_ELEMENT_DATE :

            var dateElems = currentElem.id.split("_")[0];
            var dateBtn = currentElem.id.split("_")[1];

            var isoId = dateElems + "_iso";
            var dispId = dateElems + "_disp";
            var partnerBtn = isoId + "_partbtn";

            if (dateBtn == 'date') {
                currentElem.onclick = function() {
                    var params = '\'' + isoId + '\'' + ',' + queId + ',' + daId + ',' + '\'' + dispId + '_attid' + '\'' + ',' + '\'' + dispId + '_error' + '\'';
                    var callbackfunc = 'saveUpdateDeleteQuestionnaireDate(' + params + ');';
                    popupShowCalendarX('Select Date', partnerBtn, 'calendarPopup', dispId, isoId, callbackfunc, true);
                };

            }
            break;
        case DYNAMIC_LINE_ITEM_ELEMENT_SELECT_STRUCT :
            currentElem.onchange = function() {
                saveUpdateDeleteQuestionnaireList(currentElem.id, queId, daId, currentElem.id + '_attid', currentElem.id + '_error');
            };
            break;
        case DYNAMIC_LINE_ITEM_ELEMENT_RADIO :

            var attId = currentElem.id.split("_")[0];
            currentElem.onblur = function() {
                saveUpdateDeleteQuestionnaire(currentElem.id, queId, daId, attId + '_attid', currentElem.id + '_error');
            };
            break;
        case DYNAMIC_LINE_ITEM_ELEMENT_CHECKBOX :
            currentElem.onclick = function() {
                saveDeleteQuestionnaireCheckBox(currentElem.id, queId, daId, currentElem.id + '_attid', currentElem.id + '_error');
            };

            break;
        case DYNAMIC_LINE_ITEM_ELEMENT_MULTISELECT :
            break;
        case DYNAMIC_LINE_ITEM_ELEMENT_ORGANISATION :
            break;
        case DYNAMIC_LINE_ITEM_ELEMENT_SUBJECT :
            break;
        case DYNAMIC_LINE_ITEM_ELEMENT_POSITION :
            break;
        case DYNAMIC_LINE_ITEM_ELEMENT_COMMENT :
            break;
        case DYNAMIC_LINE_ITEM_DISABLE_ROW:
            currentElem.onclick = function() {
                disableRow(lineItem.id, currentElem, queId, lineItem.value);
            };
            break;
        case DYNAMIC_LINE_ITEM_POSITION:

            currentElem.onclick = function() {
                deleteLineItemRow(queId, lineItem.value, tableId, rowId);
            };

            break;
    }
}

function increaseValueId(s, replacementNumber, dynamicLevel) {
    var NumberChars = "0123456789";
    var charChars = "";

    if (s) {
        var indexElemNum = false;
        for (var i = 0; i < s.length; i++) {
            if (NumberChars.indexOf(s.charAt(i)) != -1) {
                if (!indexElemNum) {
                    var arr = getNumber(s, i);
                    charChars += dynamicLevel + '' + eval(eval(arr[0]) + eval(replacementNumber));
                    i = arr[1];

                    indexElemNum = true;
                } else {
                    charChars += s.charAt(i);
                }
            } else {
                charChars += s.charAt(i);
            }
        }
    }
    return charChars;
}

function getDynamicEventState(cellElems) {
    return eval(getNodeValue(cellElems, 'eventJsId').value);
}

function getIsManagerWriteOnlyState(cellElems) {
    return eval(getNodeValue(cellElems, 'managerWriteOnly').value);
}

function getDynamicNodeId(cellElems) {
    return eval(getNodeValue(cellElems, 'daId').value);
}

function getDynamicLineItem(cellElems) {
    return getNodeValue(cellElems, 'lineItemId');
}

function setAttributeIdToEmpty(element) {
    if (element) {
        var id = element.id;
        if (id) {

            if (id.match("_attid")) {
                element.setAttribute("value", "");
            }
        }
        element.disabled = false;
    }
}

function getDynamicNodeIdFromSelection(id, cellElems) {
    return eval(getNodeValue(cellElems, id + '_daId').value);
}

function getNodeValue(cellElems, name) {
    if (cellElems) {
        for (var j = 0; j < cellElems.length; j++) {
            var currentElem = cellElems[j];

            if (currentElem.nodeName == 'INPUT') {
                if (currentElem.name == name) {
                    return currentElem;
                }
            }
        }
        return "";
    }
}

function getLevel(newTds) {
    var dynamicLevel = 0;

    for (var i = 0; i < newTds.length; i++) {
        var newTd = newTds[i];
        var cellElems = newTd.getElementsByTagName('*');

        for (var j = 0; j < cellElems.length; j++) {
            var currentElem = cellElems[j];
            if (currentElem) {
                if (isWrappedAttribute(currentElem)) {
                    dynamicLevel = parseLevel(currentElem.name);
                    if (dynamicLevel > 0) return dynamicLevel;
                }
            }
        }
    }
    return eval(dynamicLevel);
}

function isWrappedAttribute(currentElem) {
    if (currentElem.name) {
        var subString;
        if (currentElem.name.length > 24) {
            subString = currentElem.name.substring(0, 24);
            if (subString == 'wrappedDynamicAttributes') {
                return true;
            }
        }
    }
    return false;
}

function parseLevel(s) {

    var NumberChars = "0123456789";

    var numberChars = "";
    var occuredNum = false;
    if (s) {
        for (var i = s.length - 1; i > 0; i--) {
            if (NumberChars.indexOf(s.charAt(i)) == -1) {
                if (occuredNum) i = 0;
            } else {

                numberChars += s.charAt(i);
                occuredNum = true;
            }
        }
        var reversedNumber = "";
        for (i = 0; i <= numberChars.length; i++) {
            reversedNumber = numberChars.charAt(i) + reversedNumber;
        }
        return eval(reversedNumber);
    }
    return 0;
}


function decreaseDynamicPositionIndex(s) {

    var NumberChars = "0123456789";
    var charChars = "";

    if (s) {
        var questionIndexFound = false;
        var lineItemIndexFound = false;

        for (var i = 0; i < s.length; i++) {
            if (NumberChars.indexOf(s.charAt(i)) != -1) {
                if (!questionIndexFound) {
                    var arr = getNumber(s, i);
                    charChars += eval(eval(arr[0]));
                    i = arr[1];
                    questionIndexFound = true;
                } else {
                    if (!lineItemIndexFound) {
                        var arr = getNumber(s, i);
                        var number = eval(arr[0]) - 1;
                        charChars += number;
                        i = arr[1];
                        lineItemIndexFound = true;
                    } else {
                        charChars += s.charAt(i);
                    }
                }
            } else {
                charChars += s.charAt(i);
            }
        }
    }
    return charChars;
}

function increaseNameWithValue(s, replacementNumber, dynamicLevel) {

    var NumberChars = "0123456789";
    var charChars = "";

    if (s) {
        var wDynanicAttIdFound = false;
        var wQueAttIdFound = false;
        for (var i = 0; i < s.length; i++) {
            if (NumberChars.indexOf(s.charAt(i)) != -1) {
                if (!wDynanicAttIdFound) {
                    var arr = getNumber(s, i);
                    charChars += eval(eval(arr[0]) + eval(replacementNumber));
                    i = arr[1];
                    wDynanicAttIdFound = true;
                } else {
                    if (!wQueAttIdFound) {
                        var arr = getNumber(s, i);
                        var number = dynamicLevel;

                        charChars += number;
                        i = arr[1];
                        wQueAttIdFound = true;
                    } else {
                        charChars += s.charAt(i);
                    }
                }
            } else {
                charChars += s.charAt(i);
            }
        }
    }
    return charChars;
}

function getNumber(s, index) {
    var NumberChars = "0123456789";
    var numberChars = "";
    var arr = new Array();
    for (var i = index; i < s.length; i++) {
        if (NumberChars.indexOf(s.charAt(i)) != -1) {
            numberChars += s.charAt(i);
        } else {
            arr[0] = eval(Math.abs(numberChars));
            arr[1] = i - 1;
            return arr;
        }
    }
    arr[0] = eval(Math.abs(numberChars));
    arr[1] = i;
    return arr;
}
function disableRow(rowId, triggerElem, queId, lineItemId) {

    disableRowBeforeSave(rowId, triggerElem);
    var dynamicPosition = parseLevel(triggerElem.name);
    saveUpdateQuestionnaireAttributeDisabled(triggerElem, queId, lineItemId, dynamicPosition);
}
function disableRowBeforeSave(rowId, triggerElem) {
    var rowElem = getElemById(rowId);

    var inputElems = rowElem.getElementsByTagName("INPUT");
    var selectElems = rowElem.getElementsByTagName("SELECT");
    var txtArea = rowElem.getElementsByTagName("TEXTAREA");

    var state = triggerElem.checked;
    for (var i = 0; i < inputElems.length; i++) {
        var inputElem = inputElems[i];
        if (inputElem != triggerElem && inputElem.className.indexOf("lockDown") > -1) {
            inputElem.disabled = state;
        }
    }
    for (var j = 0; j < selectElems.length; j++) {
        var selectElem1 = selectElems[j];
        if(selectElem1.className.indexOf("lockDown") > -1) {
            selectElem1.disabled = state;
        }
    }
    for (var k = 0; k < txtArea.length; k++) {
        var selectElem2 = txtArea[k];
        if(selectElem2.className.indexOf("lockDown") > -1) {
            selectElem2.disabled = state;
        }
    }
}

function deleteLineItemRow(queId, lineItemId, tableId, rowId) {

    var table = getElemById(tableId);
    var rows = table.getElementsByTagName('TR');
    var rowToDelete = getElemById(rowId);
    var deletedRowPos = getLevel(rowToDelete.getElementsByTagName("TD"));

    for (var i = 0; i < rows.length; i++) {
        var followingRow = rows[i];
        var dynamicPos = getLevel(followingRow.getElementsByTagName("TD"));
        if (dynamicPos > deletedRowPos) {
            var cellElems = followingRow.getElementsByTagName('*');
            for (var j = 0; j < cellElems.length; j++) {
                var currentElem = cellElems[j];
                if (currentElem) {
                    if (isWrappedAttribute(currentElem)) {

                        var newName = decreaseDynamicPositionIndex(currentElem.name);
                        currentElem.name = newName;
                    }
                }
            }
        }
    }
    table.deleteRow(rowToDelete.rowIndex);
    var queDefId = getElemById("queDefIdxx").value;
    questionnaireBean.deleteDynamicLineItemRow(queId, queDefId, deletedRowPos, lineItemId,
            { callback:function(attributeResult) {
                displaySumEnums(attributeResult);
            },async:false
            });
}

function verifyErrors(errorMsg) {
    if(errorCount > 0) {
        alert(errorMsg);
        return false;
    }
    return true;
}

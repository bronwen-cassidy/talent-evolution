// start population functions

function showCriteriaTree(index, title, popupRoot, popupId, partnerValueId, partnerKeyId, allowClear) {
    setTempIndex(index);
    popupShowServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, 'criteriaSelected()', allowClear);
}

function setTempIndex(index) {
    var frm = document.forms['temp'];
    frm.index.value = index;
}

function getTempIndex() {
    var frm = parent.document.forms['temp'];
    return frm.index.value;
}

function criteriaSelected() {

    var frm = parent.document.forms['population'];
    frm.index.value = getTempIndex();
    frm.action = '?_target1=1';
    frm.submit();
}

function removeLine(criteriaIndex) {
    var frm = document.forms['population'];
    frm.index.value = criteriaIndex;
    frm.action = '?_target2=2';
    frm.submit();
}

function addLine() {
    var frm = document.forms['population'];
    frm.action = '?_target1=1';
    frm.submit();
}
// end population functions

// start formula functions
function addOperand() {
   submitFormToTarget('function', '_target7', '7', 'pgTarget');
}

function saveFormula() {
    submitFormToTarget('function', '_target9', '9', 'pgTarget');
}

function editFormula(index) {
    submitTabularReportForm(index, '6');
}

function showFormulaTree(index, title, popupRoot, popupId, partnerValueId, partnerKeyId, allowClear) {
    setTempIndex(index);
    popupShowServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, null, allowClear);
}


function removeOperand(criteriaIndex) {
    submitFormToTarget('function', '_target8', '8', 'pgTarget','selectedColumnIndex',criteriaIndex);
}

// end formula functions

// crosstab report functions
function showHideRows(tableId, linkElem) {
    var tableVar = getElemById(tableId);
    // get the rows for the table looking for class hideable
    var hideableClass = "hideable";
    var rows = tableVar.getElementsByTagName("tr");
    var i;
    var found = 0;
    for(i = 0; i < rows.length; i++) {
        var row = rows[i];
        if(row.className == hideableClass) {
            if(row.style.display == "none") {
                row.style.display = "";
                found = 0;
            } else {
                row.style.display = "none";
                found = 1;
            }
        }
    }

    // set the link text
    var child = linkElem.firstChild;
    if (child != null) {
        if(found == 0) {
            child.src = "../images/minus.gif";
        } else {
            child.src = "../images/plus.gif";
        }
    }
}

// end crosstab report functions

// start metric functions
function showMetricCriteriaTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, allowClear) {
    popupShowServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, null, allowClear);
}

function showCountMetricCriteriaTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, allowClear) {
    popupShowServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, 'metricCriteriaSelected()', allowClear);
}

function metricCriteriaSelected() {
    var frm = parent.document.forms['metric'];
    // get the hidden field representing the target to submit to
    var hidFld = getElemByIdAndDoc('pgTarget', parent.document);
    hidFld.name = "_target2";
    hidFld.value = "2";
    frm.submit();
}

// end metric functions

// start report functions
function showColumnTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, displayFieldId, allowClear) {
    popupShowServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, null, allowClear, displayFieldId);
}

// end report functions

function submitFormToTarget(frmName, targetNme, targetVal, targetElemId, hidFieldId, hidFldVal, hidFldNme) {
    var frm = document.forms[frmName];
    var targetElem = getElemById(targetElemId);
    targetElem.name=targetNme;
    targetElem.value=targetVal;

    if (hidFieldId != null)
    {
        var target = getElemById(hidFieldId);
        if (target != null) target.value= hidFldVal;
        if (hidFldNme != null) target.name=hidFldNme;
    }

    frm.submit();
}

function handleWizardBack(frmName, targetId, targetVal, backElemId) {
    var frm = document.forms[frmName];
    var targetElem = getElemById(targetId);
    targetElem.name="_target" + targetVal;
    targetElem.value=targetVal;

    var backElem = getElemById(backElemId);
    backElem.name = "_back";
    backElem.value = "_back";

    frm.submit();
}

// start of tabular report functions

function showTabularColumnTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, displayFieldId, allowClear) {
    popupShowServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, 'tabularColumnSelected()', allowClear, displayFieldId);
}

function showChartColumnTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, displayFieldId, allowClear) {
    popupShowServerTree(title, popupRoot, popupId, partnerValueId, partnerKeyId, null, allowClear, displayFieldId);
}

function tabularColumnSelected() {

    var doc = parent.document;

    var targetNme = '_target1';
    var targetVal = '1';
    var targetElemId = 'pgTarget';
    var formName = 'reports';

    var frm = doc.forms[formName];
    var targetElem = getElemByIdAndDoc(targetElemId, doc);
    targetElem.name=targetNme;
    targetElem.value=targetVal;
    frm.submit();
}

function appraisalReportColumnSelected(sourceId, targetId) {
    var sourceElem = getElemById(sourceId);
    var targetElem = getElemById(targetId);
    targetElem.value = sourceElem.value;
}

function addTabularReportFormula() {
    submitFormToTarget('reports', '_target5', '5', 'pgTarget');
}

function addTabularReportColumn() {
	submitTabularReportForm('0', '1');
}

function deleteTabularReportColumn(index) {
    submitTabularReportForm(index, '4');
}

function deleteChartReportColumn(index) {
    var indexField = getElemById('deletedColumnIndex');
    indexField.value= index;        
    submitFormToTarget('reports', '_target1', '1', 'pgTarget');
}

function submitTabularReportForm(index, pageTargetValue) {
    var indexField = getElemById('selectedColumnIndex');
    indexField.value= index;
    var pageTargetName = "_target" + pageTargetValue;
    submitFormToTarget('reports', pageTargetName, pageTargetValue, 'pgTarget');
}

function populateReportPdfFields(){
    var frm = document.forms['_reportpdfexport'];
    // if this is a drilldown report there is no population select so this is null.
    var elem1 = getElemById('pop2');
    var elem2 = getElemById('ob2');
    var elem3 = getElemById('so2');

    var field1 = getElemById('pop_id2');
    var field2 = getElemById('ob2');
    var field3 = getElemById('so2');

    // ignore setting this field if it is a drilldown report
    if (elem1 != null) {
        assignMultiSelectElem(elem1, field1);
    }

    // order by may be null
    var orderBy = null;
    try {
        orderBy = elem2.options[elem2.selectedIndex].value;
    } catch (e) {}

    if (orderBy != null) {
        field2.value = orderBy;
    }

    field3.value = elem3.options[elem3.selectedIndex].value;

    frm.submit();
}
function populateCsvFields() {
    var frm = document.forms['_csvexport'];
    // if this is a drilldown report there is no population select so this is null.

    var elem1 = getElemById('pop2');
    var elem2 = getElemById('ob2');
    var elem3 = getElemById('so2');

    var field1 = getElemById('pop_id1');
    var field2 = getElemById('ob1');
    var field3 = getElemById('so1');

    // ignore setting this field if it is a drilldown report
    if (elem1 != null) {
        assignMultiSelectElem(elem1, field1);
    }

    // order by may be null
    var orderBy = null;
    try {
        orderBy = elem2.options[elem2.selectedIndex].value;
    } catch (e) {}

    if (orderBy != null) {
        field2.value = orderBy;
    }

    field3.value = elem3.options[elem3.selectedIndex].value;

    frm.submit();
}

function assignMultiSelectElem(elem1, field1) {
    var output = "";
    for(var i = 0; i < elem1.options.length; i++)
    if(elem1.options[i].selected) {
        output += elem1.options[i].value + ",";
    }
    // delete the last ,
    if(output.length > 0) {
        output = output.replace(/,$/,"");
    }
    field1.value = output;
}

function selectTabularReportColumnColour(index) {
    submitTabularReportForm(index, '2');
}

function popUpShowColourPicker(title, buttonId, popupId) {
    var popupRoot = getElemById(buttonId);
    popupShow(title, popupRoot, popupId);
}

//title, popupRoot, popupId, partnerValueId, partnerKeyId, onSelFunc, allowClear
function popupShowBlog(title, buttonId, popupId, partnerValId, partnerKeyId) {
    var popupRoot = getElemById(buttonId);
    popupShow(title, popupRoot, popupId, partnerValId, partnerKeyId, null, false);
}

function saveColours() {
    submitFormToTarget('reports', '_target3', '3', 'pgTarget');
}

function saveComment(paragraphFieldId, textAreaElemId) {
    // prepend the saved info to the paragraph tag selected
    var textAreaElem = getElemById(textAreaElemId);
    var paragraphElem = getElemById(paragraphFieldId);
    paragraphElem.innerHTML="<strong>" + textAreaElem.value + "</strong>";
}

// functions used when displaying legend after running a tabular report
function popupShowColourLegend(title, prefix, columnId, popupId) {
    var popupRoot = getElemById(prefix + columnId);
    popupShow(title, popupRoot, popupId);
}

function submitLegendForm(columnLinkPrefix, columnLinkIdValue, activeTabFieldId, activeTabValue) {
    var columnElem = getElemById('colx1');
    columnElem.value=columnLinkIdValue;
    if(activeTabFieldId != null) {
        var activeTabElem = getElemById(activeTabFieldId);
        activeTabElem.value = activeTabValue;
    }
    var popupIdPrefix = getElemById('popupIdPrefix');
    popupIdPrefix.value=columnLinkPrefix;
    submitFormToTarget('runreport', '_target1', '1', 'pgTarget');
}

function submitChartToTarget(targetNum, formName, targetId) {
    submitFormToTarget(formName, '_target' + targetNum, targetNum, targetId);
}

function submitChartFormValues(frmName, hiddenFldId, hiddenFldValue, indexHiddenFldId, indexFldValue) {
    var frm = document.forms[frmName];
    var hiddenFieldElem = getElemById(hiddenFldId);
    hiddenFieldElem.value = hiddenFldValue;
    if(indexHiddenFldId != null) {
    var indexElem = getElemById(indexHiddenFldId);
    indexElem.value = indexFldValue;
    }
    frm.submit();
}

function handleReportPageSubmit(pgStartId, pgNum, frmName) {
    var pageStartFld = getElemById(pgStartId);
    pageStartFld.value = pgNum;
    var frm = document.forms[frmName];
    frm.submit();
}

// end of tabular report functions

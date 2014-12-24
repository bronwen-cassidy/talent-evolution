// javascript file to handle functions for the dynamic attributes

// enable a checkbox
function enable(selectElem, checkBoxId) {

    var checkBoxElem = getElemById(checkBoxId);
    if (selectElem.options[selectElem.selectedIndex].value == 'DATE') {
        checkBoxElem.disabled = false;
    } else {
        checkBoxElem.disabled = true;
        checkBoxElem.checked = false;
    }
}

function updateCalculation(displayElemId, tableId) {

    var spanElem = getElemById(displayElemId);
    var targetTable = getElemById(tableId);
    var output = "Formula: ";
    // get each of the rows
    var cols = targetTable.getElementsByTagName("TD");
    for (var j = 0; j < cols.length; j++) {
        var col = cols[j];
        var elems = col.getElementsByTagName("*");
        for (var k = 0; k < elems.length; k++) {
            var elem = elems[k];
            if (elem.nodeName == "SELECT") {
                var selectedText = elem.options[elem.selectedIndex].text;
                if (selectedText != 'Please Select') output += selectedText + " ";
            } else if (elem.type == 'text') {
                output += elem.value + " ";
            }
        }
    }
    spanElem.innerHTML = output;
}

function clearSelectTextValue(elem) {
    if (elem.nodeName == 'SELECT') elem.selectedIndex = 0;
    else if (elem.type == 'text') elem.value = '';
}

function toggleEnabled(elem1Id, elem2Id, elem3Id, otherCheckboxId) {
    // find the clicked element different for ie and firefox
    var evt = window.event || arguments.callee.caller.arguments[0];
    var clickedElem = evt.target || evt.srcElement;

    var clickedElemName = clickedElem.name;
    // get the last character of the name it will give us the index to append
    var nameLength = clickedElemName.length - 1;

    var postFix = clickedElemName.charAt(nameLength);

    var other = getElemById(otherCheckboxId + postFix);
    other.checked = !clickedElem.checked;
    
    var elem1 = getElemById(elem1Id + postFix);
    var elem2 = getElemById(elem2Id + postFix);
    var elem3 = getElemById(elem3Id + postFix);

    elem1.disabled = !elem1.disabled;
    elem2.disabled = !elem2.disabled;
    elem3.disabled = !elem3.disabled;

    if (elem1.disabled) clearSelectTextValue(elem1);
    if (elem2.disabled) clearSelectTextValue(elem2);
    if (elem3.disabled) clearSelectTextValue(elem3);
}

function addRow(tableId, numExpFldId) {

    var tblBody = getElemById(tableId).tBodies[0];

    var hiddenElem = getElemById(numExpFldId);
    var index = hiddenElem.value;

    var newRow = tblBody.rows[0].cloneNode(true);

    // loop through the cells
    var newTds = newRow.getElementsByTagName("TD");

    // must match numCells and the length of new names
    for (var i = 0; i < newTds.length; i++) {
        var newTd = newTds[i];
        var cellElems = newTd.getElementsByTagName('*');
        for (var j = 0; j < cellElems.length; j++) {
            var currentElem = cellElems[j];
            if (currentElem.nodeName == 'INPUT' || currentElem.nodeName == 'SELECT') {
                // get the name and change the number to the index
                var currentElemName = currentElem.name;
                var currentElemId = currentElem.id;
                currentElem.setAttribute("name", currentElemName.replace('0', index));
                currentElem.setAttribute("id", currentElemId.replace('0', index));

                if (currentElem.nodeName == 'SELECT') {
                    currentElem.options[0].selected = 'true';
                    var formatSel = 'formatSelId' + index;
                    currentElem.disabled = (currentElem.id == formatSel);

                } else if (currentElem.type == 'text') {
                    currentElem.value = '';
                    currentElem.disabled = true;

                } else if (currentElem.type == 'checkbox') {
                    currentElem.checked = currentElem.id != ('txtChoice' + index);
                }
            }
        }
    }
    tblBody.appendChild(newRow);
    hiddenElem.value = parseInt(index) + 1;
}

// removes the last row in a table
function removeRow(tableId, numExpFldId) {

    // should have found the corrent element now
    var tableElem = getElemById(tableId);

    var rows = tableElem.getElementsByTagName('TR');
    var lastRowIndex = rows.length;
    if (lastRowIndex > 2) {
        tableElem.deleteRow(lastRowIndex - 1);
        // decrement the hidden elements value
        var hiddenElem = getElemById(numExpFldId);
        var index = hiddenElem.value;
        hiddenElem.value = parseInt(index) - 1;

    } else {
        alert("must have at least 2 attributes");
    }
}
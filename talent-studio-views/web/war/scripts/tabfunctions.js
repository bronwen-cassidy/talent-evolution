// Functions for tabs in browsing artefact views

    function concatActiveTab(actionTabFieldId, firstParam){
        var concatToAction = "";
        if (actionTabFieldId != null)
        {
           var actionTabField = getElemById(actionTabFieldId);
           if (firstParam == null )
           {
               concatToAction = "&";
           }
           concatToAction =  concatToAction + actionTabFieldId + "=" + actionTabField.value ;
        }
        return concatToAction;
    }

    function setHiddenFromSelect(selectField, hiddenFieldId, formName, actionTabFieldId) {
        var tempElement = getElemById(hiddenFieldId);
        tempElement.value = selectField.options[selectField.selectedIndex].value;
        var frm = document.forms[formName];
        frm.action="?_target1=1" + concatActiveTab(actionTabFieldId);
        frm.submit();
    }

    function setHiddenFromButton(hiddenFieldId, fieldValue, formName, valTarget , actionTabFieldId) {
        if (valTarget == null )
        {
            valTarget = "_target2=2";
        }

        setHiddenFieldValue(hiddenFieldId, fieldValue);

        var frm = document.forms[formName];
        frm.action = "?" + valTarget + concatActiveTab(actionTabFieldId);
        frm.submit();
    }


    function setHiddenFromList(nodeId, hiddenFieldId, formName, actionTabFieldId) {

        setHiddenFieldValue(hiddenFieldId, nodeId);
        var frm = document.forms[formName];
        frm.action="?_target4=4"+ concatActiveTab(actionTabFieldId);
        frm.submit();
    }

    function setTargetAndSubmit(target, formName, actionTabFieldId) {

        var frm = document.forms[formName];
        if (target == null)
        {
            frm.action = frm.action + "?" + concatActiveTab(actionTabFieldId,'first');
        }
        else
        {
            frm.action="?" + target + concatActiveTab(actionTabFieldId);
        }
        frm.submit();
    }

    function handleSearchSubmit(target, formName, actionTabFieldId) {

        var frm = document.forms[formName];
        if (target == null)
        {
            frm.action = frm.action + "?" + concatActiveTab(actionTabFieldId,'first');
        }
        else
        {
            frm.action="?" + target + concatActiveTab(actionTabFieldId);
        }
        return true;
    }

     function copyActionTabAndSubmit(formName, actionTabFieldId, actionTabCopyFieldId ) {

        var frm = document.forms[formName];
        var actionTabField = getElemById(actionTabFieldId);
        var actionTabCopyField = getElemById(actionTabCopyFieldId);

        if(actionTabCopyField && actionTabField) {
            actionTabCopyField.value = actionTabField.value;
        }
        frm.submit();
    }

    function setHiddenFromSelectAndSubmit(formName, selectElem, hiddenFieldId) {
        var frm = document.forms[formName];
        var hiddenFld = getElemById(hiddenFieldId);
        hiddenFld.value = selectElem.options[selectElem.selectedIndex].value;
        frm.submit();
    }

    function setHiddenFieldValue(hiddenFieldId, value) {
        var tempElement = getElemById(hiddenFieldId);
        if (tempElement != null) tempElement.value = value;
    }


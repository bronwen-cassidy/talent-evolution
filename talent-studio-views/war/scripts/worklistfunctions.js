var targetFieldId = 'tarId';

function respondNotification(notificationId) {
    var frm = document.forms[notificationId];
    frm.submit();
}

function respondNotificationWarning(notificationId, message) {
    confirmActionAndPost(notificationId, message);        
}

function postQuestionnaireTarget(formElement) {
    var frm = document.forms[formElement];
    frm.submit();
}

function setHiddenValuesAndPost(formName, targetElemId, targetElemValue, hidFieldId, hidFldVal) {
    var targetName = '_target' + targetElemValue;
    submitFormToTarget(formName, targetName, targetElemValue, targetElemId, hidFieldId, hidFldVal);
}

function handleDeleteImage(actionElemId, actionElemValue) {
    setHiddenValuesAndPost('questionnaireForm', targetFieldId, '10', actionElemId, actionElemValue);
}

function setSelectedEvaluatorAnswers(selectAnswerOptions) {
    var selectedValue = selectAnswerOptions.options[selectAnswerOptions.selectedIndex].value;
    setHiddenValuesAndPost('questionnaireForm', targetFieldId, '7', 'selGroupId', selectedValue);
}

function submitViewObjective(selectedObjectiveId) {
    setHiddenValuesAndPost('questionnaireForm', targetFieldId, '9', 'selObjectiveId', selectedObjectiveId);
}

function addDynamicLineItem(index) {
    setHiddenValuesAndPost('questionnaireForm', targetFieldId, '11', 'deleteImage', index);
}

function deleteDynamicLineItem(id, index) {
    setHiddenField('deleteImage', index);
    setHiddenValuesAndPost('questionnaireForm', targetFieldId, '12', 'dlId', id);
}

function addBlogComment(index) {
    setHiddenValuesAndPost('questionnaireForm', targetFieldId, '13', 'deleteImage', index);
}
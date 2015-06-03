<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<script language="javascript" type="text/javascript">
<!--
	function configItemReportColumn(frmName, hiddenFld, hiddenVal, targetFldId, targetFldValue, repIndex) {
		var frm = document.forms[frmName];
		var hFld = getElemById(hiddenFld);
		hFld.value = hiddenVal;
		var targetField = getElemById(targetFldId);
		targetField.name = '_target' + targetFldValue;
		targetField.value = targetFldValue;
        if(repIndex != null) {
            var reportIndexFld = getElemById('reportIndex');
            reportIndexFld.value = repIndex;
        }
		frm.submit();
	}
// -->
</script>

<zynap:tab defaultTab="${command.activeTab}" id="vset_info" url="javascript" tabParamName="activeTab">
    <fmt:message key="artefacts" var="displayConfigLabel"/>
    <zynap:tabName value="${displayConfigLabel}" name="displayconfig"/>

    <c:if test="${command.hasDisplayConfigItems}">
        <zynap:tabName value="${command.displayConfig.label}" name="displayitems"/>
    </c:if>

    <c:if test="${command.hasDisplayConfigItem}">
        <zynap:tabName value="${command.displayConfigItem.label}" name="displayitem"/>
    </c:if>

    <div id="displayconfig_span" style="display:<c:choose><c:when test="${command.activeTab == 'displayconfig'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <c:import url="../displayconfig/displayconfiglist.jsp"/>
    </div>

    <div id="displayitems_span" style="display:<c:choose><c:when test="${command.activeTab == 'displayitems'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <c:import url="../displayconfig/displayconfigitemslist.jsp"/>
    </div>

    <div id="displayitem_span" style="display:<c:choose><c:when test="${command.activeTab == 'displayitem'}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
        <c:import url="../displayconfig/displayconfigreportform.jsp"/>
    </div>

</zynap:tab>

<c:if test="${command.editing}">
    <c:choose>
        <c:when test="${command.personExecSummary}">
            <c:url value="/picker/execartefactviewpicker.htm" var="execPickerUrl">
                <c:param name="populationType" value="${command.reportType}"/>
                <c:param name="viewType" value="${command.viewType}"/>
            </c:url>
            <zynap:window elementId="columnTree" src="${execPickerUrl}"/>    
        </c:when>
        <c:otherwise>
            <c:url value="/picker/artefactviewpicker.htm" var="pickerUrl">
                <c:param name="populationType" value="${command.reportType}"/>
                <c:param name="viewType" value="${command.viewType}"/>
            </c:url>
            <zynap:window elementId="columnTree" src="${pickerUrl}"/>
        </c:otherwise>
    </c:choose>

</c:if>

<%@ include file="../includes/include.jsp"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%@include file="../includes/orgunitpicker.jsp"%>

<%-- organisation unit tree picker --%>
<zynap:actionbox>

    <c:set var="organisation" value="${command.organisationUnit}"/>
    <c:set var="hasOU" value="${organisation != null}"/>

    <fmt:message var="ouLabel" key="please.select"/>
    <c:if test="${hasOU}">
        <c:set var="ouLabel" value="${organisation.label}"/>
        <c:set var="ouId" value="${organisation.id}"/>
    </c:if>

    <zynap:form name="viewOuObjectives" method="post" action="listorgunitobjectives.htm">
        <zynap:message code="select.organisation.unit" var="ouMsg" javaScriptEscape="true"/>
        <c:set var="btnAction">popupShowTree('<c:out value="${ouMsg}"/>', this, 'ouPicker', 'nav_ou_disp', 'nav_ou_id' , 'ouObjectivesSubmit()')</c:set>

        <c:out value="${ouMsg}"/>&nbsp;:&nbsp;

        <span style="white-space: nowrap;"><input id="nav_ou_disp" type="text" class="input_text" style="width:220px;"
                value="<c:out value="${navigator.organisationUnitLabel}"/>"
                    name="navigator.organisationUnitLabel"
                    readonly="true"
             /><input type="button"
                    class="partnerbutton"
                    value="..." id="navOUPopup"
                    onclick="<c:out value="${btnAction}"/>"/></span>
        <input id="nav_ou_id1" type="hidden" name="organisationUnitId" value="<c:out value="${ouId}"/>" />
        <input id="nav_ou_id" type="hidden" name="navigator.organisationUnitId" value="<c:out value="${navigator.organisationUnitId}"/>"    />
        <input id="nav_notSubmit" type="hidden" name="navigator.notSubmit" value="false"/>
    </zynap:form>

</zynap:actionbox>

<%-- command is an instance of the ObjectiveSetFormBean --%>
<c:set var="hasCorporate" value="${command.publishedCorporateObjectiveSet != null}"/>

<%-- cannot do anything other than view objectives if there are no corporate objectives published --%>
<c:if test="${hasCorporate}">
    <zynap:actionbox>

        <c:set var="hasObjectives" value="${command.hasCurrentObjectives}"/>
        <c:if test="${!hasObjectives}">
            <zynap:actionEntry>
                <zynap:form method="get" action="addorganisationobjectives.htm" name="addBtnFrm">
                    <input type="hidden" id="selOuIdx" name="ou_id" value="<c:out value="${command.organisationUnitId}"/>"/>
                    <fmt:message key="add" var="addLabel"/>
                    <input class="actionbutton" type="button" name="objAddBtn" value="<c:out value="${addLabel}"/>" onclick="document.forms.addBtnFrm.submit(); disableButton(this);"/>
                </zynap:form>
            </zynap:actionEntry>
        </c:if>

    </zynap:actionbox>
</c:if>

<fmt:message key="organisation.goals" var="msg"/>
<zynap:infobox title="${msg}" id="objectivesBoxId">

    <zynap:historyLink var="viewUrl" url="vieworganisationobjectives.htm"/>
    <fmt:message key="generic.label" var="labelHeader"/>
    <fmt:message key="status" var="statusHeader"/>
    <fmt:message key="worklist.actions" var="headeraction" />
    <zynap:message code="delete.confirm.message" var="deletename"/>
    <zynap:message code="approve.confirm.message" var="approveMsg"/>


    <display:table name="${command.objectiveSets}" id="objId" htmlId="objTblId" sort="list" pagesize="20" class="pager" defaultsort="1" requestURI="">

        <display:column property="label" title="${labelHeader}" sortable="true" headerClass="sortable" href="${viewUrl}" paramProperty="id" paramId="id" class="pager" comparator="org.displaytag.model.RowSorter"/>
        <display:column title="${statusHeader}" sortable="true" headerClass="sortable" class="pager" sortProperty="status">
            <fmt:message key="${objId.status}"/>
        </display:column>
        <display:column class="pager" title="${headeraction}" headerClass="sorted">
            <%-- only display this column to delete or edit, delete or edit can only happen if the organisation unit objectives are status open --%>
            <c:if test="${objId.pending}">
                <a href="<c:url value="/admin/editorganisationobjectives.htm"><c:param name="ou_id" value="${objId.organisationUnit.id}"/></c:url>"><fmt:message key="edit"/></a>&nbsp;
                |&nbsp;
                <c:url var="deleteUrl" value="/admin/deleteorganisationobjectives.htm"><c:param name="id" value="${objId.id}"/></c:url>
                <a href="javascript:confirmAction('<c:out value="${deleteUrl}"/>', '<c:out value="${deletename}"/>');"><fmt:message key="delete"/></a>&nbsp;
                |&nbsp;
                <c:url var="approveUrl" value="/admin/approveorganisationobjectives.htm"><c:param name="id" value="${objId.id}"/></c:url>
                <a href="javascript:confirmAction('<c:out value="${approveUrl}"/>', '<c:out value="${approveMsg}"/>');"><fmt:message key="approve"/></a>
            </c:if>

        </display:column>

    </display:table>

</zynap:infobox>

<%@ page import="com.zynap.talentstudio.web.organisation.BrowseNodeWrapper"%>

<%@include file="../../includes/orgunitpicker.jsp"%>

<c:if test="${command.currentNodes != null}">

<script type="text/javascript">
     function goToPreviousSelected() {
        var frm = document.forms['navigationOrgUnitSection'];
        frm.submit();
    }
</script>

<zynap:actionbox id="browsebar">

    <c:if test="${!command.searching}">
        <c:set var="hasOuTree" value="${outree != null && !empty outree}"/>
    
        <zynap:form method="get" name="navigationOrgUnitSection" excludeHistory="true">
            <zynap:message code="select.organisation.unit" var="ouMsg" javaScriptEscape="true"/>
            <c:set var="btnAction">javascript:popupShowTree('<c:out value="${ouMsg}"/>', this, 'ouPicker', 'nav_ou_disp', 'nav_ou_id' , 'goToPreviousSelected()')</c:set>

            Org Unit&nbsp;:&nbsp;

            <span style="white-space: nowrap;"><input id="nav_ou_disp" type="text" class="input_text" style="width:220px;" 
                value="<c:out value="${navigator.organisationUnitLabel}"/>" 
                    name="navigator.organisationUnitLabel" 
                    readonly="true"
             /><input type="button"
                    class="partnerbutton" <c:if test="${!hasOuTree}">disabled</c:if> 
                    value="..." id="navOUPopup" 
                    onclick="<c:out value="${btnAction}"/>"/></span>
            <input id="nav_ou_id" type="hidden" name="navigator.organisationUnitId" value="<c:out value="${navigator.organisationUnitId}"/>"    />
            <input id="nav_notSubmit" type="hidden" name="navigator.notSubmit" value="false"/>
            <input id="menu_p" type="hidden" name="menu_p" />
        </zynap:form>
    </c:if>

    <c:set var="targetPage" value="_target2=2"/>
    <c:if test="${command.searching}">
        <c:set var="targetPage" value="_target5=5"/>
    </c:if>
    &nbsp;&nbsp;<c:out value="${BrowseType}"/>&nbsp;:&nbsp;
    <zynap:actionEntry>
        <input class="navbutton" type="button" id="first" value="<fmt:message key="browse.first"/>" <c:if test="${command.first == null}">disabled</c:if> onclick="javascript:setHiddenFromButton('nodeTargetId', '<%= BrowseNodeWrapper.FIRST_NODE %>', 'navigation','<c:out value="${targetPage}"/>','activeTab')"/>
        <input class="navbutton" type="button" id="prev" value="<fmt:message key="browse.previous"/>" <c:if test="${command.previous == null}">disabled</c:if> onclick="javascript:setHiddenFromButton('nodeTargetId', '<%= BrowseNodeWrapper.PREVIOUS_NODE %>', 'navigation','<c:out value="${targetPage}"/>','activeTab')"/>
        <c:if test="${command.nodesNumber!=null}">
            <span id="browseCount"><c:out value="${command.currentIndex}"></c:out>&nbsp;<fmt:message key="browse.of"/>&nbsp;<c:out value="${command.nodesNumber}"></c:out></span>
        </c:if>
        <input class="navbutton" type="button" id="next" value="<fmt:message key="browse.next"/>" <c:if test="${command.next == null}">disabled</c:if> onclick="javascript:setHiddenFromButton('nodeTargetId', '<%= BrowseNodeWrapper.NEXT_NODE %>', 'navigation','<c:out value="${targetPage}"/>','activeTab')"/>
        <input class="navbutton" type="button" id="last" value="<fmt:message key="browse.last"/>" <c:if test="${command.last == null}">disabled</c:if> onclick="javascript:setHiddenFromButton('nodeTargetId', '<%= BrowseNodeWrapper.LAST_NODE %>', 'navigation','<c:out value="${targetPage}"/>','activeTab')"/>
    </zynap:actionEntry>
    <c:if test="${!command.searching}">
        <zynap:actionEntry>
            <select name="nodeId" onChange="setHiddenFromSelect(this, 'hidden_node_id', 'navigation','activeTab');">
                <c:forEach var="node" items="${command.currentNodes}">
                    <option value="<c:out value="${node.id}"/>" <c:if test="${artefact.id == node.id}">selected</c:if>><c:out value="${node.label}"/></option>
                </c:forEach>
            </select>
        </zynap:actionEntry>
    </c:if>
</zynap:actionbox>

</c:if>

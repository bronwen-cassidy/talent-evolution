<%@ include file="../includes/include.jsp"%>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="back" action="/admin/listorgunitobjectives.htm">
            <fmt:message key="back" var="buttonLabel" />
            <input type="hidden" name="<%=ParameterConstants.ORG_UNIT_ID_PARAM%>" value="<c:out value="${command.organisationUnit.id}"/>"/>
            <zynap:button cssClass="actionbutton" name="backnme" value="${buttonLabel}" type="button" onclick="document.forms.back.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="organisation.goals.msg" var="msg"><fmt:param value="${command.organisationUnit.label}"/><fmt:param value="${command.objectiveSet.label}"/></fmt:message>
<c:set var="objectives" value="${command.objectiveSet.objectives}" scope="request"/>
<zynap:infobox title="${msg}">
   
    <table class="infotable">
        <c:forEach var="objective" items="${objectives}" varStatus="indexer">
            <tr>
                <td class="infolabel"><fmt:message key="generic.label"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><c:out value="${objective.label}"/></td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><div class="blogContent"><c:out value="${objective.description}"/></div></td>
            </tr>
            <tr><td colspan="2" class="infoheading">&nbsp;</td></tr>
        </c:forEach>
    </table>
</zynap:infobox>
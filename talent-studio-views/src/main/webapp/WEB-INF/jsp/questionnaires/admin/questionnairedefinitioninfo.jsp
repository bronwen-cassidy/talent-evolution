<%@ include file="../../includes/include.jsp" %>
<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants" %>

<zynap:actionbox id="defActions">
    <zynap:actionEntry>
        <input class="actionbutton" type="button" value="<fmt:message key="edit"/>" onclick="document.forms._edit.submit();"/>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <input class="actionbutton" type="button" value="<fmt:message key="delete"/>" onclick="document.forms._delete.submit();"/>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="questionnaire.definition.details" var="msg"/>
<zynap:infobox title="${msg}" id="defInfo">

    <table class="infotable" id="defInfoDetails">
        <tr>
            <td class="infolabel"><fmt:message key="generic.name"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${questionnaireDefinition.label}"/></td>
        </tr>

        <tr>
            <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><zynap:desc><c:out value="${questionnaireDefinition.description}"/></zynap:desc></td>
        </tr>
    </table>

</zynap:infobox>

<zynap:form method="get" name="_edit" action="/admin/editquestionnairedefinition.htm">
    <input type="hidden" name="<%= ParameterConstants.QUESTIONNAIRE_DEF_ID %>" value="<c:out value="${questionnaireDefinition.id}"/>"/>
</zynap:form>

<zynap:form method="get" name="_delete" action="/admin/confirmdeletequestionnairedefinition.htm">
    <input type="hidden" name="<%= ParameterConstants.QUESTIONNAIRE_DEF_ID %>" value="<c:out value="${questionnaireDefinition.id}"/>"/>
</zynap:form>


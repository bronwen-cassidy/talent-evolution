<%@ include file="../../includes/include.jsp" %>

<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<c:set var="orgUnit" value="${model.organisation}"/>
<c:set var="orgUnitId" value="${orgUnit.id}"/>
<c:set var="nodeId" value="${orgUnit.id}"/>
<c:set var="includeButtons" value="${model.includeButtons}"/>

<c:choose>
    <c:when test="${includeButtons}">
        <zynap:actionbox>
            <zynap:actionEntry>
                <fmt:message key="back" var="buttonLabel"/>
                <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
            </zynap:actionEntry>

            <%-- Save this page here so that we generate the back button first - otherwise the back button will point to this page again --%>
            <zynap:saveUrl/>
            <zynap:actionEntry>
                <zynap:form method="get" name="_edit" action="editorganisation.htm">
                    <input type="hidden" name="id" value="<c:out value="${orgUnitId}"/>"/>
                    <input class="actionbutton" name="_edit" type="button" value="<fmt:message key="edit"/>"
                           onclick="javascript:document.forms._edit.submit();"/>
                </zynap:form>
            </zynap:actionEntry>

            <%-- Ensure that delete button is not present for default org unit which cannot be deleted --%>
            <c:if test="${!orgUnit.default}">
                <zynap:actionEntry>
                    <zynap:form method="get" name="_delete" action="deleteorganisation.htm">
                        <input type="hidden" name="id" value="<c:out value="${orgUnitId}"/>"/>
                        <input class="actionbutton" name="_delete" type="submit" value="<fmt:message key="delete"/>"/>
                    </zynap:form>
                </zynap:actionEntry>
            </c:if>

        </zynap:actionbox>
    </c:when>
    <c:otherwise>
        <zynap:evalBack>
            <zynap:actionbox>
                <zynap:actionEntry>
                    <fmt:message key="back" var="buttonLabel"/>
                    <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
                </zynap:actionEntry>
            </zynap:actionbox>
        </zynap:evalBack>
    </c:otherwise>

</c:choose>

<fmt:message key="organisation.unit.information" var="orgUnitBoxTitle"/>
<zynap:infobox title="${orgUnitBoxTitle}">
    <table class="infotable" cellspacing="0">
        <tr>
            <td class="infolabel"><fmt:message key="generic.name"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${orgUnit.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="parent.organisation"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <c:choose>
                    <c:when test="${orgUnit.parentLabel == null}">
                        <fmt:message key="is.top.organisation"/>
                    </c:when>
                    <c:otherwise>
                        <c:out value="${orgUnit.parentLabel}"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <%--<tr>--%>
            <%--<td class="infolabel"><fmt:message key="admin.position.active"/>&nbsp;:&nbsp;</td>--%>
            <%--<td class="infodata"><fmt:message key="${orgUnit.active}"/></td>--%>
        <%--</tr>--%>
        <tr>
            <td class="infolabel"><fmt:message key="admin.position.comments"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><zynap:desc><c:out value="${orgUnit.comments}"/></zynap:desc></td>
        </tr>

        <c:if test="${not empty orgUnit.wrappedDynamicAttributes}">            
            <c:forEach var="attr" items="${orgUnit.wrappedDynamicAttributes}" varStatus="attrIndex">
                <tr>
                    <td class="infolabel">
                        <c:out value="${attr.label}"/>&nbsp;:&nbsp;
                    </td>
                    <td class="infodata">
                        <c:set var="attrType" value="${attr.attributeDefinition.type}"/>
                        <c:set var="criteria" value="${attr}"/>
                        <c:set var="attrValueId" value="${attr.attributeValue.id}"/>
                        <%@ include file="../attributes/viewattributesnippet.jsp"%>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
    </table>
</zynap:infobox>

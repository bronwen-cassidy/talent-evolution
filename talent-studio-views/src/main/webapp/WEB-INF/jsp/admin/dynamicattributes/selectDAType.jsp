<%@ include file="../../includes/include.jsp" %>

<fmt:message key="da.wizard.0" var="msg"/>
<zynap:infobox title="${msg}">

    <zynap:form name="selecttype" method="post">
        <input type="hidden" name="<%=ParameterConstants.ARTEFACT_TYPE%>" value="<c:out value="${artefactType}"/>"/>
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel">
                    <fmt:message key="artefact.type"/>:
                </td>
                <td class="infodata">
                    <fmt:message key="${artefactType}" var="type"/>
                    <c:out value="${type}"/>
                </td>
            </tr>
            <tr>
                <td class="infolabel">
                    <fmt:message key="select.type"/>:
                </td>
                <td class="infodata">
                    <spring:bind path="command.type">
                        <select name="<c:out value="${status.expression}"/>" onchange="enable(this, 'calcCheckId');">
                            <c:forEach var="type" items="${types}">
                                <option value="<c:out value="${type.valueId}"/>" <c:if test="${command.type == type.valueId}">selected</c:if>>
                                    <c:out value="${type.label}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infolabel">
                    <fmt:message key="calculated.field"/>:
                </td>
                <td class="infodata">
                    <spring:bind path="command.calculated">
                        <input id="calcCheckId" type="checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${status.value}">checked</c:if> <c:if test="${!status.value &&  command.type != 'DATE'}">disabled</c:if>/>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <c:choose>
                        <c:when test="${artefactType == 'S'}">
                            <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>"
                                   onClick="location.href='<c:url value="/admin/listsubjectDA.htm"></c:url>'"/>
                        </c:when>
                        <c:otherwise>
                            <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>"
                                   onClick="location.href='<c:url value="/admin/listpositionDA.htm"></c:url>'"/>
                        </c:otherwise>
                    </c:choose>
                    <input class="inlinebutton" name="_target1" type="submit" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

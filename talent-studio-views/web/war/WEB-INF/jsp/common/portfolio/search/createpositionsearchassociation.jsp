<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="position.association.title" var="boxtitle"/>
<zynap:infobox title="${boxtitle}">
    <c:if test="${error != null}">
        <div class="error"><c:out value="${error}"/></div>
    </c:if>

    <zynap:form method="post" name="_create">

    <table class="infotable">
        <tr>
            <td class="infodata">
                <table class="pager" cellspacing="0">
                    <thead>
                        <tr>
                            <th class="sorted"><fmt:message key="position"/></th>
                            <th class="sorted"><fmt:message key="association.type"/></th>                        
                        </tr>
                    </thead>
                    <c:forEach var="assoc" items="${command.unassignedAssociations}" varStatus="status" >
                        <tr>
                            <td class="infolabel">
                                <c:out value="${assoc.position.title}"/>
                            </td>
                            <td class="infodata">
                                <spring:bind path="command.unassignedAssociations[${status.index}].qualifier.id">
                                    <select name="<c:out value="${status.expression}"/>">
                                        <option value="" <c:if test="${assoc.qualifier.id == null}">selected</c:if>><fmt:message key="please.select"/></option>
                                        <c:forEach var="cat" items="${categories}">
                                            <option value="<c:out value="${cat.id}"/>" <c:if test="${cat.id == assoc.qualifier.id}">selected</c:if>>
                                                <c:out value="${cat.label}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <%@include file="../../../includes/error_message.jsp" %>
                                </spring:bind>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:forEach var="assignedAssoc" items="${command.assignedAssociations}">
                        <tr>
                            <td class="infolabel">
                                <c:out value="${assignedAssoc.position.title}"/>
                            </td>
                            <td class="infodata">
                                <fmt:message key="already.defined"/> :
                                <c:out value="${assignedAssoc.qualifier.label}"/>
                             </td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
        <tr>
            <td class="infobutton">
                <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                <c:if test="${!empty command.unassignedAssociations}">
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </c:if>
            </td>
        </tr>
    </table>
    </zynap:form>
</zynap:infobox>

<zynap:form name="_cancel" method="post">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

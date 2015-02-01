
    <tr>
        <td class="infolabel"><fmt:message key="da.struct.refersto"/>&nbsp;:&nbsp;*</td>
        <spring:bind path="command.refersTo">
            <td class="infodata">
                <select name="<c:out value="${status.expression}"/>">
                    <option value="" <c:if test="${command.refersTo == null}">selected</c:if>><fmt:message key="please.select"/></option>
                    <c:forEach var="lookupType" items="${lookups}">
                        <option value="<c:out value="${lookupType.typeId}"/>"
                            <c:if test="${command.refersTo == lookupType.typeId}">
                                selected
                            </c:if>
                        >
                        <c:out value="${lookupType.label}"/>
                        </option>
                    </c:forEach>
                </select>
                <%@ include file="../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </tr>
   

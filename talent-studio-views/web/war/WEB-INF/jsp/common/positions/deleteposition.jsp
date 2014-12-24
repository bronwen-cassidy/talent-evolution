<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="Delete Position">
    <!-- delete confirm message-->
    <div class="infomessage"><fmt:message key="confirm.delete.position"/></div>

    <table class="infotable" cellspacing="0">
        <!-- Title-->
        <tr>
            <td class="infoheading" colspan="2"><fmt:message key="position.to.delete"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="admin.position.title"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${command.title}"/></td>
        </tr>

        <c:if test="${!empty positions}">
            <tr>
                <td class="infoheading" colspan="2"><fmt:message key="positions.tobe.deleted"/></td>
            </tr>
            <tr>
                <td class="infodata" colspan="2">
                    <ul>
                        <c:forEach var="position" items="${positions}">
                            <li><c:out value="${position.label}"/>&nbsp;<img alt="" src="../images/reportsTo.gif"/>&nbsp;<c:out value="${position.parent.label}"/></li>
                        </c:forEach>
                    </ul>
                </td>
            </tr>
        </c:if>
        <tr>
            <td class="infobutton"/>
            <td class="infobutton">
                <zynap:form method="post" name="_delete">
                    <input type="submit" class="inlinebutton" name="_cancel" value="<fmt:message key="cancel"/>"/>
                    <input type="button" class="inlinebutton" name="_delete" value="<fmt:message key="confirm"/>" onclick="javascript:document.forms._delete.submit();"/>
                    <spring:bind path="command">
                        <%@include file="../../includes/error_message.jsp"%>
                    </spring:bind>
                </zynap:form>            
            </td>
        </tr>
    </table>
</zynap:infobox>


<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="reports" method="post" encType="multipart/form-data">
    <input id="pgTarget" type="hidden" name="" value="-1"/>
    <input id="backId" type="hidden" name="" value="-1"/>
    <table class="infotable" cellspacing="0" cellpadding="0">
        <tr>
            <td>
                <table class="crosstabtable" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="crosstabaxisheader">
                            <table width="100%">
                                <tr>
                                    <td class="crosstabcolumntitle"><c:out value="${command.horizontalColumn.label}"/></td>
                                </tr>
                                <tr>
                                    <td class="crosstabrowtitle"><c:out value="${command.verticalColumn.label}"/></td>
                                </tr>
                            </table>
                        </td>
                        <c:forEach var="hHeading" items="${command.horizontalHeadings}">
                            <td class="crosstabcolumnheader"><c:out value="${hHeading}"/></td>
                        </c:forEach>
                     </tr>
                    <%-- each one of these items is a list --%>
                    <c:set var="cellInfos" value="${command.cellInfos}"/>
                    <c:forEach var="cellInfo" items="${cellInfos}" varStatus="verticalIndex">
                        <tr>
                            <td class="crosstabrowheader"><c:out value="${command.verticalHeadings[verticalIndex.index]}"/></td>

                            <c:forEach var="cell" items="${cellInfo}" varStatus="horizontalIndex">
                                <td class="crosstabrowheader">
                                    <spring:bind path="command.cellInfos[${verticalIndex.index}][${horizontalIndex.index}].label">
                                        <input type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                        <%@include file="../../../includes/error_messages.jsp" %>
                                    </spring:bind>
                                </td>
                            </c:forEach>

                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
        <tr>
            <td class="infobutton" align="center">
                <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                <input class="inlinebutton" name="_back" type="button" value="<fmt:message key="wizard.back"/>" onclick="handleWizardBack('reports', 'pgTarget', '2', 'backId');"/>
                <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
            </td>
        </tr>
    </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>
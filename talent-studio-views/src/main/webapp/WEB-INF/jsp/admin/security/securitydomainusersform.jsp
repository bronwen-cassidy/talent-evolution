<%@ include file="../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="_add" method="post">
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infodata" colspan="2">
                    <%--<c:import url="tree/individualpickerinc.jsp"/>--%>

                    <input class="inlinebutton" type="button" name="SetAll" value="<fmt:message key="setall"/>" onClick="checkAll(document._add.userIds)"/>
                    <input class="inlinebutton" type="button" name="ClearAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(document._add.userIds)"/>
                    <table cellpadding="0" cellspacing="0">
                        <c:forEach var="us" items="${command.users}" varStatus="usIndexer">
                            <spring:bind path="command.userIds">
                                <c:if test="${usIndexer.index != 0 && usIndexer.index % 5 == 0}"></tr></c:if>
                                <c:if test="${usIndexer.index == 0 || usIndexer.index % 5 == 0}"><tr></c:if>
                                <td>
                                    <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" value="<c:out value="${us.value.id}"/>" <c:if test="${us.selected}">checked</c:if>/>
                                    &nbsp;
                                    <c:out value="${us.value.loginInfo.username}"/>
                                    &nbsp;
                                </td>
                            </spring:bind>
                        </c:forEach>
                    </table>
                </td>
            </tr>

            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="button" name="_back" value="<fmt:message key="wizard.back"/>" onclick="javascript:handleWizardBack('_add', 'pgTarget', '1', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_target3" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

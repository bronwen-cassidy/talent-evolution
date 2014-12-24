<%@ include file="../includes/include.jsp" %>

<zynap:infobox title="${title}">
    <zynap:form name="addinfo" method="post" encType="multipart/form-data">
        <table cellspacing="0" class="infotable">
            <tr>
                <td class="infoheading" colspan="2">
                    <fmt:message key="user.details"/>
                </td>
            </tr>
            <%-- Enter the user Name --%>
            <tr>
                <td class="infolabel"><fmt:message key="admin.add.user.username"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.loginInfo.username">
                <td class="infodata"><input type="text" maxlength="200" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    <%@ include file="../includes/error_message.jsp" %>
                </td>
                </spring:bind>
            </tr>
            <%-- enter the password --%>
            <c:if test="${add != null}">
                <tr>
                    <td class="infolabel"><fmt:message key="admin.add.user.password"/>&nbsp;:&nbsp;*</td>
                    <spring:bind path="command.loginInfo.password">
                    <td class="infodata"><input type="password" maxlength="50" class="input_password" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>">
                        <%@ include file="../includes/error_message.jsp" %>
                    </td>
                    </spring:bind>
                </tr>
                <!-- repeat the password -->
                <tr>
                    <td class="infolabel"><fmt:message key="repeat.new.password"/>&nbsp;:&nbsp;*</td>
                    <spring:bind path="command.loginInfo.repeatedPassword">
                    <td class="infodata">
                        <input type="password" maxlength="50" class="input_password" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../includes/error_message.jsp" %>
                    </td>
                    </spring:bind>
                </tr>
            </c:if>
            <%-- core details --%>
            <%@ include file="../common/users/coredetailsform.jsp"%>
            <%-- is Active --%>
            <tr>
                <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.active">
                        <td class="infodata"><input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.active}">checked</c:if>/>
                            <%@ include file="../includes/error_message.jsp" %>
                        </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="assign.home.page.group"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.groupId">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${command.groupId == null}">selected="true"</c:if>>&nbsp;</option>
                            <c:forEach var="group" items="${command.groups}">
                                <option value="<c:out value="${group.id}"/>" <c:if test="${group.id == command.groupId}">selected="true" </c:if>><c:out value="${group.label}"/></option>
                            </c:forEach>
                        </select>
                        <%@ include file="../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            <%-- assign roles --%>
            <tr>
                <td class="infoheading" colspan="2">
                    <fmt:message key="admin.user.roles"/>
                </td>
            </tr>
            <tr>
                <td class="infolabel">
                    <fmt:message key="access.role"/>&nbsp;:&nbsp;
                </td>
                <td class="infodata">
                    <c:import url="../common/roles/assignroles.jsp"/>
                </td>
            </tr>
            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="addinfo" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form name="_cancel" method="post">
    <input type="hidden" name="<%=ParameterConstants.CANCEL_PARAMETER%>" value="true"/>
</zynap:form>

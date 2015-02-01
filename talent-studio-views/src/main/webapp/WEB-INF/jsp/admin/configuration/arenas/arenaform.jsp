<%@ include file="../../../includes/include.jsp" %>

<zynap:infobox title="${title}">

    <spring:bind path="command">
        <%@include file="../../../includes/error_messages.jsp" %>
    </spring:bind>

    <zynap:form name="_add" method="post" encType="multipart/form-data">
        <table cellspacing="0" class="infotable">

            <%--<tr>--%>
                <%--<td class="infolabel"><fmt:message key="generic.arena"/>&nbsp;:&nbsp;</td>--%>
                <%--<td class="infodata"><c:out value="${command.description}"/></td>--%>
            <%--</tr>--%>

            <tr>
                <td class="infolabel"><fmt:message key="generic.label"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.label">
                    <td class="infodata">
                        <input type="text" maxlength="200" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>

            <tr>
                <td class="infolabel"><fmt:message key="arena.timeouts.minutes"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.sessionTimeout">
                    <td class="infodata">
                        <input type="text" maxlength="3" class="input_number" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>

            <tr>
                <td class="infolabel"><fmt:message key="sort.order"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.sortOrder">
                    <td class="infodata">
                        <input type="text" maxlength="4" class="input_number" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>

            <%-- disable checkbox for hiding arena if necessary --%>
            <tr>
                <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.active">
                        <td class="infodata"><input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.active}">checked="checked"</c:if> <c:if test="${!command.hideable}">disabled="disabled"</c:if>/>
                            <%@ include file="../../../includes/error_message.jsp" %>
                        </td>
                </spring:bind>
            </tr>

            <%-- include ability to choose views for arenas --%>
            <c:if test="${command.hideable}">
                <tr>
                    <td class="infoheading" colspan="2"><fmt:message key="arena.assign.displayconfigitems"/></td>
                </tr>

                <spring:bind path="command.arenaDisplayConfigItemIds">
                    <c:forEach var="arenaDisplayConfigItem" items="${command.arenaDisplayConfigItems}" varStatus="indexer" >
                        <tr>
                            <td class="infolabel"><c:out value="${arenaDisplayConfigItem.label}"/>&nbsp;:&nbsp;*</td>
                            <td class="infodata">
                                <select name="<c:out value="${status.expression}"/>">
                                    <option value="" <c:if test="${arenaDisplayConfigItem.selectedItemId == null}">selected</c:if>><fmt:message key="please.select"/></option>
                                    <c:forEach var="displayConfigItem" items="${arenaDisplayConfigItem.displayConfigItems}">
                                        <c:if test="${displayConfigItem.active}">
                                            <option <c:if test="${displayConfigItem.id == arenaDisplayConfigItem.selectedItemId}">selected</c:if> name="<c:out value="${displayConfigItem.label}"/>" value="<c:out value="${displayConfigItem.id}"/>">
                                                <c:out value="${displayConfigItem.label}"/>
                                            </option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </c:forEach>
                </spring:bind>
            </c:if>

            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" id="_cancel" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" id="_save" name="_save" value="<fmt:message key="save"/>"/>
                </td>
            </tr>

        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form name="_cancel" method="post">
    <input type="hidden" name="<%=ParameterConstants.CANCEL_PARAMETER%>" value="true"/>
</zynap:form>

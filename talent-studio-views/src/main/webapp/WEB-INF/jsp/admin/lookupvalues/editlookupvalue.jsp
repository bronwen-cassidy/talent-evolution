<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="Edit Selection Value">
    <zynap:form method="post" name="_edit" encType="multipart/form-data">
        <div class="infomessage">
            <fmt:message key="lookupvalue.for">
                <fmt:param value="${command.label}"/>
                <fmt:param value="${command.lookupType.label}"/>
            </fmt:message>
        </div>

        <table class="infotable" cellspacing="0">
            <!-- Short Description -->
                        
            <tr>
                <td class="infolabel"><fmt:message key="lookupvalue.label"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.label">
                    <td class="infodata"><input type="text" maxlength="100" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <!-- Description -->
            <tr>
                <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.description">
                    <td class="infodata"><textarea name="description" rows="4"><c:out value="${status.value}"/></textarea>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>

            <!-- Sort Order -->
            <tr>
                <td class="infolabel"><fmt:message key="sort.order"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.sortOrder">
                    <td class="infodata"><input type="text" maxlength="7" class="input_number" name="sortOrder" value="<c:out value="${status.value}"/>"/>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>

            <!-- Active - depends on whether this is a system lookupType type-->
            <c:if test="${!command.system}">
                <tr>
                    <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
                    <spring:bind path="command.active">
                        <td class="infodata"><input type="checkbox" class="input_checkbox" name="active" <c:if test="${command.active}">checked</c:if>/>
                            <%@ include file="../../includes/error_message.jsp" %>
                        </td>
                    </spring:bind>
                </tr>
            </c:if>
            <c:if test="${command.system}">
                <tr>
                    <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><input type="checkbox" class="input_checkbox" name="active" DISABLED <c:if test="${command.active}">checked</c:if>/></td>                                    
                </tr>
            </c:if>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
			        <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="_edit" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel" action="/admin/viewlookupvalue.htm">
    <input type="hidden" name="_cancel" value="_cancel"/>
    <input type="hidden" name="<%=ParameterConstants.LOOKUP_ID%>" value="<c:out value="${command.id}"/>"/>
</zynap:form>

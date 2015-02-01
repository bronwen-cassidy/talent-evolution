<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="${title}">
    <zynap:form method="post" name="_add" encType="multipart/form-data">
        <table class="infotable" cellspacing="0">
            <!-- Label - DB generates id from this-->
            <tr>
                <td class="infolabel"><fmt:message key="generic.name"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.label">
                    <td class="infodata"><input type="text" maxlength="80" name="label" value="<c:out value="${status.value}"/>"/>
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
            <!-- Active -->
            <tr>
                <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
                <spring:bind path="command.active">
                    <td class="infodata"><input type="checkbox" class="input_checkbox" name="active" <c:if test="${command.active}">checked</c:if>/>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
			        <input class="inlinebutton" type="button" value="<fmt:message key="cancel"/>" name="_cancel" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="_add" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel" action="/admin/listlookuptypes.htm">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

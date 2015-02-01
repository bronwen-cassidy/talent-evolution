<%@ include file="../includes/include.jsp"%>

<zynap:infobox title="${title}" >

	<div class="infomessage">
		<fmt:message key="confirm.delete.user"/>&nbsp;<fmt:message key="confirm.message"/>
	</div>

    <table class="infotable" id="userdetails" cellspacing="0">
        <%-- this parameter indicates that you do not want to display the contact details --%>
        <c:set var="coreDetail" value="${model.artefact.coreDetail}"/>
        <%@include file="../common/users/viewcoredetails.jsp"%>
        <c:if test="${model.dVError != null}">
            <tr>
                <td class="error" colspan="2"><fmt:message key="${model.dVError}"/></td>
            </tr>
        </c:if>
        <tr>
            <td class="infobutton" align="center">
                <zynap:form method="post" name="_cancel">
                    <input type="hidden" name="<%=ParameterConstants.CANCEL_PARAMETER%>" value="true"/>
                    <input type="hidden" name="<%=ControllerConstants.PREV_URL%>" value="<c:out value="${model.prevUrl}"/>"/>
                    <input class="inlinebutton" type="submit" value="<fmt:message key="cancel"/>" name="_cancel"/>
                </zynap:form>
                <c:if test="${model.dVError == null}">
                    <zynap:form method="post" name="_confirm">
                        <input type="hidden" name="<%=ParameterConstants.CONFIRM_PARAMETER%>" value="true"/>
                        <input type="hidden" name="<%=ControllerConstants.PREV_URL%>" value="<c:out value="${model.prevUrl}"/>"/>
                        <input class="inlinebutton" type="button" value="<fmt:message key="confirm"/>" name="_confirm" onclick="javascript:document.forms._confirm.submit();"/>
                    </zynap:form>
                </c:if>
            </td>
        </tr>
    </table>

</zynap:infobox>
<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>
<zynap:actionbox>
    <zynap:actionEntry>
        <fmt:message key="back" var="buttonLabel" />
        <zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
    </zynap:actionEntry>

    <zynap:actionEntry>
        <fmt:message key="edit" var="editButtonLabel"/>
        <zynap:form name="edit" method="get" action="edituser.htm" >
            <input type="button" class="actionbutton" id="btn_editUser" name="edit" value="<fmt:message key="edit"/>" onclick="javascript:document.forms.edit.submit();"/>
            <input type="hidden" name="<%=ParameterConstants.USER_ID%>" value="<c:out value="${userId}"/>"/>
        </zynap:form>
    </zynap:actionEntry>

    <zynap:actionEntry>
        <zynap:form name="_reset" method="get" action="resetpassword.htm">
            <input class="actionbutton" type="button" value="<fmt:message key="changepwd.title"/>" name="_change" onclick="javascript:document.forms._reset.submit();"/>
            <input type="hidden" name="<%=ParameterConstants.USER_ID%>" value="<c:out value="${userId}"/>"/>
        </zynap:form>
    </zynap:actionEntry>

    <zynap:actionEntry>
        <zynap:form name="_deleteuser" method="get" action="deleteuser.htm">
            <input class="actionbutton" type="button" value="<fmt:message key="delete"/>" name="_deleteuser" onclick="javascript:document.forms._deleteuser.submit();"/>
            <input type="hidden" name="<%=ParameterConstants.USER_ID%>" value="<c:out value="${userId}"/>"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

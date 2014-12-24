<%@ page import="static com.zynap.talentstudio.web.common.ParameterConstants.*" %>
<zynap:actionbox id="actions_user">
    <zynap:actionEntry>
        <fmt:message key="edit" var="editButtonLabel"/>
        <zynap:artefactForm name="edit" method="get" action="editsubjectedituser.htm" tabName="activeTab" buttonMessage="${editButtonLabel}" buttonId="btn_edituser" artefactId="${artefact.id}" >
            <input type="hidden" name="<%=USER_ID%>" value="<c:out value="${userId}"/>"/>
        </zynap:artefactForm>
    </zynap:actionEntry>

    <zynap:actionEntry>
        <zynap:artefactForm name="_reset" method="get" action="editsubjectresetpassword.htm" tabName="activeTab" buttonMessage="Change Password" buttonId="btn_userpasswd" artefactId="${artefact.id}" >
            <input type="hidden" name="<%=USER_ID%>" value="<c:out value="${userId}"/>"/>
        </zynap:artefactForm>
    </zynap:actionEntry>

    <zynap:actionEntry>
        <zynap:artefactForm name="_deleteuser" method="get" action="editsubjectdeleteuser.htm" tabName="activeTab" buttonMessage="Delete" buttonId="btn_deleteuser" artefactId="${artefact.id}" >
            <input type="hidden" name="<%=USER_ID%>" value="<c:out value="${userId}"/>"/>
        </zynap:artefactForm>
    </zynap:actionEntry>
</zynap:actionbox>
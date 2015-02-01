<%@ page import="static com.zynap.talentstudio.web.common.ParameterConstants.*" %>
<zynap:actionbox id="actions_addsubuser">

    <zynap:actionEntry>
        <fmt:message key="add" var="addButtonLabel"/>
        <zynap:artefactForm name="edit" method="get" action="editsubjectadduser.htm" tabName="activeTab" buttonMessage="${addButtonLabel}" buttonId="btn_adduser" artefactId="${artefact.id}" >
            <input type="hidden" name="<%=USER_ID%>" value="<c:out value="${userId}"/>"/>
        </zynap:artefactForm>
    </zynap:actionEntry>

</zynap:actionbox>
<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="${title}">

    <zynap:form name="add" method="post" encType="multipart/form-data">

        <spring:bind path="command">
            <%@ include file="../../includes/error_message.jsp" %>    
        </spring:bind>

        <table id="attribs" class="infotable" cellspacing="0">

            <tr>
                <td class="infomessage"><fmt:message key="data.upload.info"/></td>
            </tr>


            <tr>
                <td class="infodata">
                    <spring:bind path="command.file">
                        <input class="input_text" type="file" name="<c:out value="${status.expression}"/>" value="file already uploaded"/>
                        <%@ include file="../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>

            <tr>
                <td class="infobutton" colspan="4">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms.cncl.submit();"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </td>
            </tr>

        </table>

    </zynap:form>

</zynap:infobox>

<zynap:form method="get" name="cncl" action="/admin/home.htm"/>
<zynap:window elementId="blogComment${index}">


        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infodata">
                    <textarea id="tappx<c:out value="${index}"/>" rows="3" cols="45" name="<c:out value="${status.expression}"/>"><c:out value="${command.wrappedDynamicAttributes[index].lastComment}"/></textarea>
                </td>
            </tr>
            <tr>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="clearTextField('bcpartnerval<c:out value="${index}"/>', 'tappx<c:out value="${index}"/>'); popupHide(); "/>
                    <input class="inlinebutton" type="button" name="_savecomment" value="<fmt:message key="add"/>" onclick="saveComment('bcpartnerval<c:out value="${index}"/>', 'tappx<c:out value="${index}"/>'); popupHide();"/>
                </td>
            </tr>
        </table>
    
</zynap:window>
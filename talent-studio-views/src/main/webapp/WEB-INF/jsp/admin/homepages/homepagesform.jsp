<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="${title}">

    <zynap:form name="add" method="post" encType="multipart/form-data">

        <input id="deleteImageIndex" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value=""/>

        <spring:bind path="command">
            <%@ include file="../../includes/error_message.jsp" %>    
        </spring:bind>

        <div class="infomessage"><fmt:message key="securitydomain.homepage.warning"/></div>

        <table class="infotable">
            <thead>
            <tr>
                <th></th>
                <th><fmt:message key="group.name"/></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td class="infodata">
                    <label for="group_txt"><fmt:message key="homepage.group"/>&nbsp;:&nbsp;*</label>
                </td>
                <td class="infodata">
                    <form:input path="command.groupLabel" id="group_txt"/>
                    <form:errors path="command.groupLabel"/>
                </td>
            </tr>
            </tbody>
        </table>
        
        <table id="attribs" class="infotable" cellspacing="0">

            <thead>
                <tr>
                    <th><fmt:message key="generic.label"/></th>
                    <th><fmt:message key="homepage.tab"/></th>
                    <th><fmt:message key="homepage.url"/></th>
                    <th><fmt:message key="homepage.fileupload"/></th>
                </tr>
            </thead>
            
            <tbody>
                <c:forEach items="${command.homePages}" var="homepage" varStatus="counter">
                <tr>
                    <td class="infolabel">
                        <c:out value="${homepage.arenaLabel}"/>
                    </td>

                    <td class="infodata">
                        <form:select path="command.homePages[${counter.index}].selectedTabView">
                            <form:option value="" label=" "/> 
                            <form:options items="${command.homePageTabViews}" itemValue="key" itemLabel="value"/>
                        </form:select>
                    </td>
    
                    <td class="infodata">
                        <spring:bind path="command.homePages[${counter.index}].url">
                            <input class="input_text" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                            <%@ include file="../../includes/error_message.jsp" %>
                        </spring:bind>
                    </td>
    
                    <td class="infodata">
                        <spring:bind path="command.homePages[${counter.index}].uploadHomePage">
                            <input class="input_text" type="file" name="<c:out value="${status.expression}"/>" value="file already uploaded"/>
                            <c:if test="${command.homePages[counter.index].hasUpload == true}">
                                <fmt:message key="securitydomain.homepage.uploaded"/>&nbsp;&nbsp;&nbsp;&nbsp;<input class="deleteButton" type="button" value="Delete" name="_delete" onclick="submitFormToTarget('add', '_target1', '1', 'pgTarget', 'deleteImageIndex', '<c:out value="${counter.index}"/>', 'deleteImageIndex');"/>
                            </c:if>
                            <%@ include file="../../includes/error_message.jsp" %>
                        </spring:bind>
                    </td>
                </tr>
            </c:forEach>
                <tr>
                    <td class="infobutton" colspan="4">
                        <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms.cnclHomePages.submit();"/>
                        <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                    </td>
                </tr>
            </tbody>
        </table>
    </zynap:form>

</zynap:infobox>

<zynap:form method="post" name="cnclHomePages">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

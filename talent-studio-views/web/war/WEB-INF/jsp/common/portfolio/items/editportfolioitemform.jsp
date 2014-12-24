<%@ include file="../../../includes/include.jsp"%>

<fmt:message var="msg" key="portfolioitem.label">
    <fmt:param value="${command.node.label}"/>
</fmt:message>

<zynap:infobox title="${msg}">

    <zynap:form name="edititem" method="post" encType="multipart/form-data">
        <input type="hidden" name="<%=ParameterConstants.ITEM_ID %>" value="<c:out value="${command.id}"/>"/>
        <input type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM%>" value="<c:out value="${command.node.id}"/>"/>
        <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.SAVE_COMMAND%>"/>

        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="item.title"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.label">
                    <td class="infodata"><input type="text" maxlength="100" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="content.type"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><c:out value="${command.contentType.label}"/></td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="content.subtype"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><fmt:message key="content.subtype.${command.contentSubType}"/></td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="generic.comments"/>&nbsp;:&nbsp;</td>
                    <spring:bind path="command.comments">
                        <td class="infodata">
                            <textarea rows="4" cols="40" name="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
                            <%@include file="../../../includes/error_message.jsp" %>
                        </td>
                </spring:bind>
            </tr>

            <%-- Content types --%>
            <c:if test="${command.upload}">
                <tr>
                    <td class="infolabel"><fmt:message key="upload.file"/>&nbsp;:&nbsp;</td>
                    <spring:bind path="command.file">
                    <td class="infodata">
                        <c:out value="${command.origFileName}"/><br/>
                        <input type="file" class="input_file" name="<c:out value="${status.expression}"/>"/>
                        <%@include file="../../../includes/error_message.jsp" %>
                    </td>
                    </spring:bind>
                </tr>
            </c:if>
            <c:if test="${command.text}">
                <tr>
                    <td class="infolabel"><fmt:message key="textual.info"/>&nbsp;:&nbsp;*</td>
                    <spring:bind path="command.uploadedText">
                        <td class="infodata"><textarea name="<c:out value="${status.expression}"/>" rows="15" cols="40"><c:out value="${status.value}"/></textarea>
                            <%@include file="../../../includes/error_message.jsp" %>
                        </td>
                    </spring:bind>
                </tr>
            </c:if>
            <c:if test="${command.URL}">
                <tr>
                    <td class="infolabel"><fmt:message key="url.value"/>&nbsp;:&nbsp;*</td>
                    <spring:bind path="command.url">
                        <td class="infodata"><input type="text" maxlength="500" class="input_link" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                            <%@include file="../../../includes/error_message.jsp" %>
                        </td>
                    </spring:bind>
                </tr>
            </c:if>
            <tr>
                <td class="infolabel">
                    <fmt:message key="questionnaire.access_permissions"/>
                    &nbsp;:&nbsp;</td>
                <td class="infodata">
                    <table>
                        <tr>
                            <td></td>
                            <th><fmt:message key="portfolio.permissions.read"/></th>
                            <th><fmt:message key="portfolio.permissions.write"/></th>
                        </tr>

                          <tr>
                            <td><fmt:message key="portfolio.permissions.searchable"/> :</td>
                            <td colspan="2">
                                <spring:bind path="command.publicRead">
                                            <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                                                    <c:if test="${status.value}">checked</c:if>
                                                    onclick="setDefaultReadPermissions(document.edititem.publicRead, document.edititem.individualWrite, document.edititem.managerWrite, document.edititem.publicRead, new Array(document.edititem.managerRead, document.edititem.individualRead))"
                                                    />
                                            <%@ include file="../../../includes/error_message.jsp" %>
                                </spring:bind>
                            </td>
                         </tr>
                         <%-- only display permission options if the user is the owner of the portfolio item --%>
                         <c:if test="${command.owner}">
                            <%-- if the user is editing an item in their own profile then do not show them the individual r/w options --%>
                                <c:if test="${!command.myPortfolio}">
                                 <tr>
                                    <td>
                                        <fmt:message key="questionnaire.individual_permissions"/>
                                        &nbsp;:&nbsp;
                                    </td>

                                        <td>
                                            <spring:bind path="command.individualRead">
                                                <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                                                <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                                                        <c:if test="${status.value||command.individualWrite||command.publicRead}">checked</c:if>
                                                        <c:if test="${command.individualWrite||command.publicRead}">disabled</c:if>
                                                        />
                                                <%@ include file="../../../includes/error_message.jsp" %>
                                            </spring:bind>
                                        </td>
                                        <td>
                                            <spring:bind path="command.individualWrite">
                                                <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                                                <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                                                        <c:if test="${status.value}">checked</c:if>
                                                       onclick="setDefaultReadPermissions(document.edititem.publicRead, document.edititem.individualWrite, document.edititem.managerWrite, document.edititem.individualWrite, document.edititem.individualRead)"
                                                        />
                                                <c:set var="permissions_error" value="${status.errorMessage}"/>
                                            </spring:bind>
                                        </td>
                                    </tr>
                                </c:if>
                                <%-- display manager permissions regardless --%>
                                <tr>
                                    <td>
                                        <fmt:message key="questionnaire.manager_permissions"/>
                                        &nbsp;:&nbsp;</td>
                                    <td>
                                        <spring:bind path="command.managerRead">
                                            <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                                                    <c:if test="${status.value||command.managerWrite||command.publicRead}">checked</c:if>
                                                    <c:if test="${command.managerWrite||command.publicRead}">disabled</c:if>
                                                    />
                                            <%@ include file="../../../includes/error_message.jsp" %>
                                        </spring:bind>
                                    </td>
                                    <td>
                                        <spring:bind path="command.managerWrite">
                                            <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>"
                                                    <c:if test="${status.value}">checked</c:if>
                                                   onclick="setDefaultReadPermissions(document.edititem.publicRead, document.edititem.individualWrite, document.edititem.managerWrite, document.edititem.managerWrite, document.edititem.managerRead)"
                                                        />
                                            <%@ include file="../../../includes/error_message.jsp" %>
                                        </spring:bind>
                                    </td>
                                </tr>
                        </c:if>
                    </table>
                    <!-- display error for permissions condition -->
                    <c:if test="${not permissions_error}">
                        <div class="error">
                            <c:out value="${permissions_error}"/>
                        </div>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
            </td>
        </tr>
    </table>
    </zynap:form>
</zynap:infobox>

<%-- This url is supplied in the refdata so this is correct --%>
<zynap:form name="_cancel" method="post" action="${cancelView}">
    <input type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM%>" value="<c:out value="${command.node.id}"/>"/>
    <input type="hidden" name="<%= ParameterConstants.ITEM_ID %>" value="<c:out value="${command.id}"/>"/>
    <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.SAVE_COMMAND%>"/>
    <input type="hidden" name="_cancel" value="cancel"/>
</zynap:form>

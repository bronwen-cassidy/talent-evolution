<%@ page import="PortfolioItem" %>
<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="portfolio.add.item.title" var="msg"/>

<zynap:infobox title="${msg}">
<div class="infomessage"><fmt:message key="portfoilio.item.info"/></div>
<zynap:form name="additem" method="post" encType="multipart/form-data">

<table class="infotable" cellspacing="0">
    <tr>
        <td class="infolabel">
            <fmt:message key="content.type"/>&nbsp;:&nbsp;*
        </td>
        <spring:bind path="command.contentTypeId">
        <td class="infodata">
            <div>
                <select class="input_select" name="contentTypeId" id="contentTypeId">
                    <option value="">Please select</option>
                    <c:forEach var="item" items="${contenttypes}">
                        <option name="<c:out value="${status.expression}"/>" value="<c:out value="${item.id}"/>" <c:if test="${status.value == item.id}">selected</c:if>>
                            <c:out value="${item.label}"/>
                        </option>
                    </c:forEach>
                </select>
                <%@include file="../../../includes/error_message.jsp" %>
            </div>
        </td>
        </spring:bind>
    </tr>
    <tr>
        <td class="infolabel">
            <fmt:message key="content.subtype"/>&nbsp;:&nbsp;*
        </td>
        <spring:bind path="command.contentSubType">
            <td class="infodata">
                <div>
                    <select class="input_select" name="contentSubType" id="contentSubType" onchange="onContentSubTypeSelect('contentSubType');">
                        <option value="">Please select</option>
                        <c:forEach var="item" items="${contentsubtypes}">
                            <option name="<c:out value="${status.expression}"/>" <c:if test="${status.value == item}">selected</c:if> value="<c:out value="${item}"/>">
                                <fmt:message key="content.subtype.${item}"/>
                            </option>
                        </c:forEach>
                    </select>
                    <div id="subtypeError">
                        <%@include file="../../../includes/error_message.jsp" %>
                    </div>
                </div>
            </td>
        </spring:bind>
    </tr>

    <%-- Label --%>
    <tr>
        <td class="infolabel"><fmt:message key="item.title"/>&nbsp;:&nbsp;*</td>
        <spring:bind path="command.label">
            <td class="infodata">
                <input type="text" class="input_text" maxlength="100" name="<c:out value="${status.expression}"/>"
                       value="<c:out value="${status.value}"/>"/>
                <%@ include file="../../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </tr>
    <tr>
        <td class="infolabel"><fmt:message key="generic.comments"/>&nbsp;:&nbsp;</td>
        <spring:bind path="command.comments">
            <td class="infodata">
                <textarea rows="4" cols="40" name="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
                <%@ include file="../../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </tr>

    <spring:bind path="command.file">
        <c:set var="style" value="display:none;"/>
        <c:if test="${not empty status.errorMessage || command.upload}">
            <c:set var="style" value="display:inline;"/>
        </c:if>
        <tr id="portfolioItemUpload" style="<c:out value="${style}"/>">
            <td class="infolabel"><fmt:message key="upload.file"/>&nbsp;:&nbsp;*</td>

            <td class="infodata">
                <input type="file" class="input_file" name="<c:out value="${status.expression}"/>"/>
                <%@ include file="../../../includes/error_message.jsp" %>

            </td>
        </tr>
    </spring:bind>

    <spring:bind path="command.uploadedText">
        <c:set var="style" value="display:none; "/>
        <c:if test="${not empty status.errorMessage || not empty command.uploadedText}">
            <c:set var="style" value="display:inline;"/>
        </c:if>
        <tr id="portfolioItemText" style="<c:out value="${style}"/>">
            <td class="infolabel"><fmt:message key="textual.info"/>&nbsp;:&nbsp;*</td>

            <td class="infodata">
                <textarea name="<c:out value="${status.expression}"/>" rows="15" cols="60"><c:out value="${status.value}"/></textarea>
                <%@ include file="../../../includes/error_message.jsp" %>
            </td>

        </tr>
    </spring:bind>

    <spring:bind path="command.url">
        <c:set var="style" value="display:none;"/>
        <c:if test="${not empty status.errorMessage || not empty command.url}">
            <c:set var="style" value="display:inline;"/>
        </c:if>
        <tr id="portfolioItemUrl" style="<c:out value="${style}"/>">
            <td class="infolabel"><fmt:message key="url.value"/>&nbsp;:&nbsp;*</td>
            <td class="infodata">
                <input type="text" maxlength="500" class="input_link" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                <%@ include file="../../../includes/error_message.jsp" %>
            </td>
        </tr>
    </spring:bind>

    <tr>
        <td class="infolabel"><fmt:message key="questionnaire.access_permissions"/>&nbsp;:&nbsp;</td>
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
                                   onclick="setDefaultReadPermissions(document.additem.publicRead, document.additem.individualWrite, document.additem.managerWrite, document.additem.publicRead, new Array(document.additem.managerRead,document.additem.individualRead))"
                                    />
                            <%@ include file="../../../includes/error_message.jsp" %>
                        </spring:bind>
                    </td>
                </tr>
                    <%-- if the user is adding an item in their own profile then do not show them the individual r/w options --%>
                <c:if test="${!command.myPortfolio}">
                    <tr>
                        <td><fmt:message key="questionnaire.individual_permissions"/>&nbsp;:&nbsp;</td>
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
                                       onclick="setDefaultReadPermissions(document.additem.publicRead, document.additem.individualWrite, document.additem.managerWrite, document.additem.individualWrite, document.additem.individualRead)"
                                        />
                                <c:set var="permissions_error" value="${status.errorMessage}"/>
                            </spring:bind>
                        </td>
                    </tr>
                </c:if>
                    <%-- display manager permissions regardless --%>
                <tr>
                    <td><fmt:message key="questionnaire.manager_permissions"/>&nbsp;:&nbsp;</td>
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
                                   onclick="setDefaultReadPermissions(document.additem.publicRead, document.additem.individualWrite, document.additem.managerWrite, document.additem.managerWrite, document.additem.managerRead)"
                                    />
                            <%@ include file="../../../includes/error_message.jsp" %>
                        </spring:bind>
                    </td>
                </tr>
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
            <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>"
                   onclick="document.forms.cncl.submit();"/>
            <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
        </td>
    </tr>
</table>
</zynap:form>
</zynap:infobox>

<zynap:form name="cncl" method="post">

    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>



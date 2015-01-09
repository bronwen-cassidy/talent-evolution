<%@ page import="ISearchConstants"%>
<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:saveUrl/>

<zynap:actionbox>
    <zynap:actionEntry>
        <input class="actionbutton" type="button" value="<fmt:message key="add"/>" name="_add" onclick="document.forms._add.submit();"/>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="User Search">
    <zynap:form name="user" method="post" action="/admin/listuser.htm">
        <input type="hidden" name="<%=ParameterConstants.SEARCH_STARTED_PARAM%>" value="NO"/>        
        <div class="infomessage"><fmt:message key="search.users.message"/></div>

        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infoheading" colspan="2">
                    <fmt:message key="search.parameters"/>
                </td>
            </tr>
            <tr>
                <spring:bind path="command.userName">
                    <td class="infolabel"><fmt:message key="subject.search.username"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><input type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/></td>
                </spring:bind>
            </tr>
            <tr>
                <spring:bind path="command.prefName">
                    <td class="infolabel"><fmt:message key="subject.search.prefname"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <input type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <spring:bind path="command.firstName">
                    <td class="infolabel"><fmt:message key="subject.search.firstname"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <input type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <spring:bind path="command.secondName">
                    <td class="infolabel"><fmt:message key="subject.search.secondname"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <input type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <spring:bind path="command.groupId">
                    <td class="infolabel"><fmt:message key="group"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${status.value == null}"> selected</c:if>>&nbsp;</option>
                            <c:forEach var="group" items="${groups}">
                                <option value="<c:out value="${group.id}"/>" <c:if test="${status.value == group.id}"> selected</c:if>><c:out value="${group.label}"/></option>    
                            </c:forEach>
                        </select>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <spring:bind path="command.active">
                    <td class="infolabel"><fmt:message key="subject.search.active"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <select name="active">
                            <option value="<%=ISearchConstants.ACTIVE%>" <c:if test="${status.value == 'active'}"> selected</c:if>><fmt:message key="subject.search.active.option.active"/></option>
                            <option value="<%=ISearchConstants.ALL%>" <c:if test="${status.value == 'all'}"> selected</c:if>><fmt:message key="subject.search.active.option.all"/></option>
                            <option value="<%=ISearchConstants.INACTIVE%>" <c:if test="${status.value == 'inactive'}"> selected</c:if>><fmt:message key="subject.search.active.option.inactive"/></option>
                        </select>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="submit" value="<fmt:message key="search"/>" onclick="initiateSearch('user');"/>
                </td>
            </tr>
        </table>
    </zynap:form>

    <c:if test="${model.users != null}">
        <hr class="searchresults"/>

        <zynap:historyLink var="pageUrl" url="listuser.htm">
            <zynap:param name="hasResults" value="true"/>
        </zynap:historyLink>

        <zynap:historyLink var="viewUrl" url="viewuser.htm">
            <zynap:param name="hasResults" value="true"/>
        </zynap:historyLink>

        <%@ include file="../common/users/viewuserlist.jsp" %>

    </c:if>
</zynap:infobox>

<zynap:form name="_add" method="get" action="/admin/adduser.htm">
</zynap:form> 

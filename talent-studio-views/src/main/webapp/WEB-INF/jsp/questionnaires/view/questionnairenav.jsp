<%@include file="../../includes/include.jsp"%>
<%@ page import="com.zynap.talentstudio.web.organisation.BrowseNodeWrapper" %>

<zynap:actionbox id="browsebar">
    <zynap:actionEntry>
        <form method="post" action="" name="firstFrm">
            <input class="navbutton" type="button" id="first" value="<fmt:message key="browse.first"/>" <c:if test="${command.first == null}">disabled</c:if> onclick="document.forms.firstFrm.submit();"/>
            <input type="hidden" name="_target4" value="4"/>
        </form>
        <form method="post" action="" name="prevFrm">
            <input class="navbutton" type="button" id="prev" value="<fmt:message key="browse.previous"/>" <c:if test="${command.previous == null}">disabled</c:if> onclick="document.forms.prevFrm.submit();"/>
            <input type="hidden" name="_target3" value="3"/>
        </form>
        <c:if test="${command.numResults != -1}">
            <span id="browseCount"><c:out value="${command.currentIndex}"/>&nbsp;<fmt:message key="browse.of"/>&nbsp;<c:out value="${command.numResults}"/></span>
        </c:if>
        <form method="post" action="" name="nextFrm">
            <input class="navbutton" type="button" id="next" value="<fmt:message key="browse.next"/>" <c:if test="${command.next == null}">disabled</c:if> onclick="document.forms.nextFrm.submit();"/>
            <input type="hidden" name="_target2" value="2"/>
        </form>
        <form method="post" action="" name="lastFrm">
            <input class="navbutton" type="button" id="last" value="<fmt:message key="browse.last"/>" <c:if test="${command.last == null}">disabled</c:if> onclick="document.forms.lastFrm.submit();"/>
            <input type="hidden" name="_target5" value="5"/>
        </form>
    </zynap:actionEntry>

    <zynap:actionEntry>
        <form method="post" action="" name="selectFrm">
            <select name="workflowId" onChange="document.forms.selectFrm.submit();">
                <c:forEach var="qDto" items="${command.results}">
                    <option value="<c:out value="${qDto.workflowId}"/>" <c:if test="${command.workflowId == qDto.workflowId}">selected</c:if>><c:out value="${qDto.label}"/></option>
                </c:forEach>
            </select>
            <input type="hidden" name="_target1" value="1"/>
        </form>
    </zynap:actionEntry>
</zynap:actionbox>
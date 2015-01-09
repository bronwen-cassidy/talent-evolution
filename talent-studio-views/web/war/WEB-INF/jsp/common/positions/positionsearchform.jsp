<%@ page import="ISearchConstants"%>

<%@include file="../../includes/orgunitpicker.jsp"%>
<c:set var="hasOuTree" value="${outree != null && !empty outree}"/>

<fmt:message key="search.position" var="boxTitle" />
<zynap:infobox title="${boxTitle}" id="criteria">

<form name="position" method="post" action="" onsubmit="handleSearchSubmit('_target3=3','position');">

    <table id="searchcriteria" class="infotable" cellspacing="0">
        <tbody>
            <tr>
                <td class="infoheading" colspan="2"><fmt:message key="basic.search.parameters"/></td>
            </tr>
            <tr>
                <spring:bind path="filter.title">
                    <td class="infolabel"><fmt:message key="admin.position.title"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><input type="text" class="input_text" name="filter.title" value="<c:out value="${status.value}"/>"/></td>
                </spring:bind>
            </tr>
            <tr>
                <spring:bind path="filter.active">
                    <td class="infolabel"><fmt:message key="admin.position.active"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <select name="filter.active">
                            <option value="<%=ISearchConstants.ACTIVE%>" <c:if test="${status.value == 'active'}"> selected</c:if>><fmt:message key="subject.search.active.option.active"/></option>
                            <option value="<%=ISearchConstants.ALL%>" <c:if test="${status.value == 'all'}"> selected</c:if>><fmt:message key="subject.search.active.option.all"/></option>
                            <option value="<%=ISearchConstants.INACTIVE%>" <c:if test="${status.value == 'inactive'}"> selected</c:if>><fmt:message key="subject.search.active.option.inactive"/></option>
                        </select>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="search.orgunit"/>&nbsp;:&nbsp;</td>
                <spring:bind path="filter.orgUnitId">
                    <td class="infodata">
                       <zynap:message code="search.orgunit" var="ouMsg" javaScriptEscape="true"/>
                       <span style="white-space: nowrap;"><input id="oufld4" type="text" class="input_text" value="<c:out value="${filter.ouLabel}"/>" name="filter.ouLabel" readonly="true"/><input type="button" id="pick_ou" <c:if test="${!hasOuTree}">disabled</c:if> class="partnerbutton" value="..." onclick="popupShowTree('<c:out value="${ouMsg}"/>', this, 'ouPicker', 'oufld4', 'oufld5', null, true);"/></span>
                       <input id="oufld5" type="hidden" name="filter.orgUnitId" value="<c:out value="${filter.orgUnitId}"/>"/>
                   </td>
               </spring:bind>
            </tr>
            
            <tr>
                <td class="infolabel"><fmt:message key="report.population"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="filter.populationId">
                        <select id="pop2" name="<c:out value="filter.populationId"/>">
                            <option value="" <c:if test="${filter.populationId == null}">selected</c:if>> </option>
                            <c:forEach var="pop" items="${populations}">
                                <option value="<c:out value="${pop.id}"/>" <c:if test="${filter.populationId == pop.id}">selected</c:if>>
                                    <c:out value="${pop.label}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </spring:bind>
                </td>
            </tr>
        </tbody>

        <tbody>
            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton">
                    <input class="inlinebutton" type="submit" name="_target3" value="<fmt:message key="search"/>"/>
                </td>
            </tr>
        </tbody>
    </table>
</form>

</zynap:infobox>

<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>
<%@ include file="../../includes/include.jsp" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_back" method="get" action="listmetrics.htm">
           <input class="actionbutton" type="button" name="_back" value="<fmt:message key="back"/>" onclick="javascript:document.forms._back.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <input class="actionbutton" type="button" value="<fmt:message key="edit"/>" onclick="javascript:document.forms._edit.submit();"/>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <input class="actionbutton" type="button" value="<fmt:message key="delete"/>" onclick="javascript:document.forms._delete.submit();"/>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="metric.information" var="msg"/>

<zynap:infobox title="${msg}">
    <table class="infotable" cellspacing="0">
        <tr>
            <td class="infolabel"><fmt:message key="metric.label"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${model.report.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="type"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="${model.report.type}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="analysis.scope"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="scope.${model.report.access}"/></td>
        </tr>
         <tr>
            <td class="infolabel"><fmt:message key="report.description"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><zynap:desc><c:out value="${model.report.description}"/></zynap:desc></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="metric.attribute"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <c:out value="${model.report.attributeLabel}"/>
                <c:if test="${model.report.operator == 'count' && model.report.value != null}">
                    &nbsp;=&nbsp;
                    <c:set var="criteria" scope="request" value="${model.report}"/>
                    <c:set var="attrType" scope="request" value="${criteria.attributeDefinition.type}"/>
                    <%@ include file="../populations/viewcriteriasnippet.jsp" %>
                </c:if>
            </td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="scalar.operator"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="scalar.operator.${model.report.operator}"/></td>
        </tr>
    </table>
</zynap:infobox>

<zynap:form method="get" name="_edit" action="/analysis/editmetric.htm">
    <input type="hidden" name="<%= ParameterConstants.METRIC_ID %>" value="<c:out value="${model.report.id}"/>"/>
</zynap:form>

<zynap:form method="get" name="_delete" action="/analysis/deletemetric.htm">
    <input type="hidden" name="<%= ParameterConstants.METRIC_ID %>" value="<c:out value="${model.report.id}"/>"/>
</zynap:form>


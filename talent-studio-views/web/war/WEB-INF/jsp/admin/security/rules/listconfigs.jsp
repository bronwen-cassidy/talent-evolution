<%@ include file="../../../includes/include.jsp" %>

<c:url var="url" value="/admin/listsettings.htm"/>

<zynap:tab styleSheetClass="tab" defaultTab="${model.wrapper.targetConfig.id}" url="${url}">
	<c:forEach var="config" items="${model.wrapper.configs}">
        <zynap:tabName name="${config.id}" value="${config.label}"/>
	</c:forEach>

	<zynap:form name="edit" method="get" action="/admin/editrules.htm">

		<zynap:actionbox>
			<zynap:actionEntry>
				<input class="actionbutton" type="submit" name="edit" value="<fmt:message key="edit"/>" />
			</zynap:actionEntry>
		</zynap:actionbox>

		<c:if test="${model.wrapper.targetConfig != null}">
			<input type="hidden" name="<%=ParameterConstants.CONFIG_ID_PARAM%>" value="<c:out value="${model.wrapper.targetConfig.id}"/>"/>
		</c:if>

    </zynap:form>
    <zynap:infobox title="${title}">
        <table class="infotable" cellspacing="0">
            <c:forEach var="rule" items="${model.wrapper.targetConfig.rules}">
                <tr>
                    <td class="infolabel"><c:out value="${rule.description}"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <c:choose>
                            <c:when test="${rule.value == 'T' || rule.value == 'F'}">
                                <fmt:message key="${rule.value}"/>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${rule.value}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </zynap:infobox>
</zynap:tab>

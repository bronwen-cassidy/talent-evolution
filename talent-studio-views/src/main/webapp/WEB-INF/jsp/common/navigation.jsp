<%@ include file="../includes/include.jsp" %>

<script type="text/javascript">
    $(function() {
        
        var userId = getElemById('txyz_uid').value;
        
        $('#numInboxSpanId').each(function (i) {
            messageItemBean.getUnreadMessageCount(userId, { callback:function(num) {
                $('#numInboxSpanId').append(num);
            }});
        });
        $('#numQueSpanId').each(function (i) {
            messageItemBean.getQuestionnaireCount(userId, { callback:function(num) {
                $('#numQueSpanId').append(num);
            }});
        });
        $('#numAppSpanId').each(function (i) {
            messageItemBean.getAppraisalCount(userId, { callback:function(num) {
                $('#numAppSpanId').append(num);
            }});
        });
        $('#numAssSpanId').each(function (i) {
            messageItemBean.getAssessmentCount(userId, { callback:function(num) {
                $('#numAssSpanId').append(num);
            }});
        });
    });
    
</script>

<c:if test="${menus != null}">
    <table id="leftnavtable" cellspacing="0" cellpadding="0">
        <input type="hidden" id="txyz_uid" name="uid" value="<c:out value="${loggedInUserId}"/>"/>
        <c:forEach var="menuSection" items="${menus}">

            <tr class="navigation_no_hover" onmouseover="swapStyle(this, 'navigation_no_hover', 'navigation_hover');" onmouseout="swapStyle(this, 'navigation_hover', 'navigation_no_hover');">

                <c:set var="navId"><zynap:id>nav<c:out value="${menuSection.label}"/></zynap:id></c:set>

                <fmt:message key="menusection.${menuSection.label}" var="displayLabel" scope="request"/>
                <c:if test="${menuSection.homeArenaReportMenuSection}"><c:set var="displayLabel" value="${menuSection.label}" scope="request"/></c:if>

                <c:url value="${menuSection.url}" var="msUrl">
                    <c:param name="menu_p" value="${menuSection.primaryKey.id}"/>
                </c:url>

                <fmt:message var="jsDisplayLabel" key="loading.msg" scope="request"><fmt:param value="${displayLabel}"/></fmt:message>

                <td class="navigation_main_item" id="<c:out value="${navId}"/>" onclick="loading('nav','<zynap:message text="${jsDisplayLabel}" javaScriptEscape="true" htmlEscape="true"/>');location.href='<c:out value="${msUrl}"/>'">
                    <c:out value="${displayLabel}"/>
                </td>

            </tr>

            <c:forEach var="menuitem" items="${menuSection.sortedMenuItems}" varStatus="itemIndex">

                <c:url value="${menuitem.url}" var="menuItemUrl">
                    <c:param name="navigator.notSubmit" value="true"/>
                    <c:if test="${menuitem.reportMenuItem}">
                        <c:param name="id" value="${menuitem.reportId}"/>
                        <c:param name="displayConfigKey" value="${menuSection.id}"/>
                    </c:if>
                    <c:if test="${menuitem.reportingChartMenuItem}">
                        <c:param name="preferenceId" value="${menuitem.preferenceId}"/>
                        <c:param name="displayConfigKey" value="${menuSection.id}"/>
                    </c:if>
                </c:url>

                <tr class="navigation_no_hover" onmouseover="swapStyle(this, 'navigation_no_hover', 'navigation_hover');" onmouseout="swapStyle(this, 'navigation_hover', 'navigation_no_hover');">

                    <c:set var="navId"><zynap:id>nav<c:out value="${menuSection.label}"/><c:out value="${menuitem.label}"/></zynap:id></c:set>
                    <c:set var="miLabel" value="${menuitem.label}" scope="request"/>

                    <c:if test="${not menuitem.reportMenuItem && not menuitem.reportingChartMenuItem}">
                        <fmt:message key="${menuitem.label}" var="miLabel" scope="request"/>
                    </c:if>
                    <c:set var="mnItemId" value="ar_${itemIndex.index}"/>
                    <c:if test="${menuitem.inboxMenuItem}"><c:set var="mnItemId" value="numInboxSpanId"/></c:if>                    
                    <c:if test="${menuitem.questionnireMenuItem}"><c:set var="mnItemId" value="numQueSpanId"/></c:if>
                    <c:if test="${menuitem.appraisalMenuItem}"><c:set var="mnItemId" value="numAppSpanId"/></c:if>
                    <c:if test="${menuitem.assessmentMenuItem}"><c:set var="mnItemId" value="numAssSpanId"/></c:if>

					<fmt:message var="miJavascriptLabel" key="loading.msg" scope="request"><fmt:param value="${miLabel}"/></fmt:message>
                    <td class="navigation_sub_item"
                            id="<c:out value="${navId}"/>"
                            onclick="loading('nav','<zynap:message text="${miJavascriptLabel}" javaScriptEscape="true"/>'); location.href='<c:out value="${menuItemUrl}"/>'">
                        <span id="<c:out value="${mnItemId}"/>"><c:out value="${miLabel}"/></span>
                    </td>

                </tr>

            </c:forEach>
        </c:forEach>

    </table>
</c:if>

<%@ include file="../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="_add" method="post">
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="securitydomain.assign.roles"/>&nbsp;:&nbsp;</td>
                <td class="infodata">

                    <input class="inlinebutton" type="button" name="SetAll" value="<fmt:message key="setall"/>" onClick="checkAll(document._add.roleIds)"/>
                    <input class="inlinebutton" type="button" name="ClearAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(document._add.roleIds)"/>

                    <c:forEach var="rol" items="${command.roles}">
                        <spring:bind path="command.roleIds">
                            <div>
                                <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" value="<c:out value="${rol.value.id}"/>" <c:if test="${rol.selected}">checked</c:if>/>
                                &nbsp;
                                <c:out value="${rol.value.label}"/>
                            </div>
                        </spring:bind>
                    </c:forEach>
                </td>
            </tr>

            <tr>
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="button" name="_back" value="<fmt:message key="wizard.back"/>" onclick="javascript:handleWizardBack('_add', 'pgTarget', '0', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_target2" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

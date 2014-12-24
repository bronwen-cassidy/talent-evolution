<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

        <zynap:infobox title="Confirm Delete Organisation">
            <table class="infotable" cellspacing="0">
                <tr>
                    <td class="infolabel"><fmt:message key="organisation.unit"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><c:out value="${command.label}"/></td>
                </tr>
                <!-- associated positions for this organisation -->
                <tr>
                    <td class="infolabel"><fmt:message key="associated.positions"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <c:forEach var="position" items="${command.positions}">
                            <c:out value="${position.label}"/><br/>
                        </c:forEach>
                    </td>
                </tr>

                <tr>
                    <td class="infolabel" colspan="2">
                        <div class="infomessage"><fmt:message key="effected.associated.organisations"/></div>

                        <table cellspacing="0" class="pager">
                            <thead>
                                <tr>
                                    <th><fmt:message key="sub.organisation"/></th>
                                    <th><fmt:message key="sub.organisation.positions"/></th>
                                </tr>
                            </thead>
                            <c:forEach var="child" items="${command.children}">
                                <tr>
                                    <td class="infodata"><c:out value="${child.label}"/></td>
                                    <td class="infodata">
                                        <c:forEach var="assoc" items="${child.positions}">
                                            <c:out value="${assoc.title}"/><br/>
                                        </c:forEach>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="infobutton"></td>
                    <td class="infobutton">
                        <zynap:form method="post" name="_delete">
                            <input type="submit" class="inlinebutton" name="_cancel" value="<fmt:message key="cancel"/>"/>
                            <input type="submit" class="inlinebutton" name="_delete" value="<fmt:message key="confirm"/>"/>
                        </zynap:form>
                    </td>
                </tr>
            </table>
        </zynap:infobox>

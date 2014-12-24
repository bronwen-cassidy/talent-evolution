    <%@ include file="../includes/include.jsp"%>
    <%-- an assessemnt for for each individual objective assessment --%>
        <tr>
            <td class="infolabel"><fmt:message key="goal.review"/></td>
            <td class="infolabel">
                <c:choose>
                    <c:when test="${manager}">
                        <fmt:message key="staff.progress.comments"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="my.progress.comments"/>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="infolabel">
                <fmt:message key="staff.manager.progress.comments"/>
            </td>
        </tr>
        <tr>
            <td class="infodata">
                <div class="comments">
                    <c:out value="${objective.description}"/> 
                </div>
            </td>
            <td class="infodata">
                <c:choose>
                    <c:when test="${manager}">
                        <div class="comments">
                            <c:out value="${assessment.selfComment}"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <spring:bind path="command.${prefix}.selfComment">
                            <textarea rows="17" cols="27" name="<c:out value="${status.expression}"/>" <c:if test="${assessment.approved}">readonly="true"</c:if>><c:out value="${status.value}"/></textarea>
                        </spring:bind>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="infodata">
                <c:choose>
                    <c:when test="${manager}">
                        <spring:bind path="command.${prefix}.managerComment">
                            <textarea rows="17" cols="27" name="<c:out value="${status.expression}"/>" <c:if test="${assessment.approved}">readonly="true"</c:if>><c:out value="${status.value}"/></textarea>
                        </spring:bind>    
                    </c:when>
                    <c:otherwise>
                        <div class="comments">
                            <c:out value="${assessment.managerComment}"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="infolabel"></td>
            <td class="infolabel"><fmt:message key="self.rating"/></td>
            <td class="infolabel"><fmt:message key="manager.rating"/></td>
        </tr>
        <tr>
            <td class="infodata"></td>
            <td class="infodata">
                <spring:bind path="command.${prefix}.selfRating">
                    <select name="<c:out value="${status.expression}"/>" <c:if test="${manager || assessment.approved}">disabled="true"</c:if>>
                        <option value=""></option>
                        <c:forEach var="rating" items="${assessment.ratingValues}">
                            <option value="<c:out value="${rating.id}"/>" <c:if test="${assessment.selfRating == rating.id}">selected="true"</c:if>><c:out value="${rating.label}"/></option>
                        </c:forEach>
                    </select>
                </spring:bind>
            </td>
            <td class="infodata">
                <spring:bind path="command.${prefix}.managerRating">
                    <select name="<c:out value="${status.expression}"/>" <c:if test="${not manager || assessment.approved}">disabled="true"</c:if>>
                        <option value=""></option>
                        <c:forEach var="rating" items="${assessment.ratingValues}">
                            <option value="<c:out value="${rating.id}"/>" <c:if test="${assessment.managerRating == rating.id}">selected="true"</c:if>><c:out value="${rating.label}"/></option>
                        </c:forEach>
                    </select>
                </spring:bind>
            </td>
        </tr>    

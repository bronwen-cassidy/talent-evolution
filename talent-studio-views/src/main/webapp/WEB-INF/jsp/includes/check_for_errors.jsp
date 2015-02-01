<c:set var="hasErrors" value="false"></c:set>
<spring:hasBindErrors name="command">
     <c:set var="hasErrors" value="true"></c:set>
</spring:hasBindErrors>
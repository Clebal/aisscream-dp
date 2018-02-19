<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table class="table table-striped table-bordered table-hover" name="users" id="row" requestURI="actor/list.do" pagesize="5">
	
<%-- 	<display:column>
		<span>
				<spring:url var="urlEdit" value="actor/user/edit.do">
					<spring:param name="actorId" value="${row.getId()}" />
				</spring:url>
				<a href="${urlEdit }"> <spring:message code="actor.edit.send" /></a>
		</span>
	</display:column> --%>
	
	<spring:message code="actor.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" ></display:column>
	
	<spring:message code="actor.surname" var="surnameHeader" />
	<display:column property="surname" title="${surnameHeader}" ></display:column>
	
	<spring:message code="actor.format.date" var="momentFormat"/>
	<spring:message code="actor.birthdate" var="momentHeader" />
	<display:column title="${momentHeader}" >
		<fmt:formatDate value="${row.getBirthdate()}" pattern="${momentFormat}"/>
	</display:column>
	
	<spring:message code="actor.email" var="emailHeader" />
	<display:column property="email" title="${emailHeader}" ></display:column>
	
	<spring:message code="actor.phone" var="phoneHeader" />
	<display:column property="phone" title="${phoneHeader}" ></display:column>
	
	<spring:message code="actor.address" var="addressHeader" />
	<display:column property="address" title="${addressHeader}" ></display:column>
	
	<display:column>
		<span>
				<spring:url var="urlRendezvous" value="rendezvous/list.do">
					<spring:param name="userId" value="${row.getId()}" />
				</spring:url>
				<a href="${urlRendezvous }"> <spring:message code="actor.rendezvous" /></a>
		</span>
	</display:column>
	
	<display:column>
		<spring:url var="urlDisplay" value="actor/display.do">
			<spring:param name="actorId" value="${row.getId()}" />
		</spring:url>
		<a href="${urlDisplay }"> <spring:message code="actor.display" /></a>
	</display:column>
	
	
</display:table>


<spring:url var="urlCreate" value="actor/user/create.do">
</spring:url>
<jstl:if test="${puedeCrear}">
<a href="${urlCreate }"> <spring:message code="actor.create" /></a>
</jstl:if>
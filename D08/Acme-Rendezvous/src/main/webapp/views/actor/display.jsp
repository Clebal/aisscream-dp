<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


	
<div class="container">

	
	<p><span class="display"><spring:message code="actor.name"/></span><jstl:out value="  ${actor.getName()}" /></p>
	
	<p><span class="display"><spring:message code="actor.surname"/></span><jstl:out value="  ${actor.getSurname()}" /></p>
	
	<p><span class="display"><spring:message code="actor.email"/></span><jstl:out value="  ${actor.getEmail()}" /></p>
	
	<p><span class="display"><spring:message code="actor.phone"/></span><jstl:out value="  ${actor.getPhone()}" /></p>
	
	<p><span class="display"><spring:message code="actor.address"/></span><jstl:out value="  ${actor.getAddress()}" /></p>
	
	<p><span class="display"><spring:message code="actor.birthdate"/></span><jstl:out value="  ${actor.getBirthdate()}" /></p>
	
	
	<spring:url var="urlRendezvous" value="rendezvous/list.do">
	<spring:param name="userId" value="${actor.getId()}" />
	</spring:url>
	<p><a href="${urlRendezvous }"> <spring:message code="actor.rendezvous" /></a></p>
	
	<spring:url var="urlQuestion" value="question/list.do">
	<spring:param name="userId" value="${actor.getId()}" />
	</spring:url>
	<p><a href="${urlQuestion }"> <spring:message code="actor.question" /></a></p>
	
		
</div>

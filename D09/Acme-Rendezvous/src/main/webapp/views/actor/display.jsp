<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme"	tagdir="/WEB-INF/tags"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


	
<div class="container">

	
	<acme:display code="actor.name" value="${actor.getName()}"/>
	
	<acme:display code="actor.surname" value="${actor.getSurname()}"/>
	
	<acme:display code="actor.email" value="${actor.getEmail()}"/>
	
	<acme:display code="actor.phone" value="${actor.getPhone()}"/>
	
	<acme:display code="actor.address" value="${actor.getAddress()}"/>
	
	<acme:display code="actor.birthdate" value="${actor.getBirthdate()}" codeMoment="actor.format.moment"/>
	
	<jstl:if test="${isManager==false }">
		
		<spring:url var="urlRendezvousCreated" value="rendezvous/listByUser.do">
		<spring:param name="userId" value="${actor.getId()}" />
		</spring:url>
		<p><a href="${urlRendezvousCreated }"> <spring:message code="actor.rendezvous.created" /></a></p>
		
		<spring:url var="urlRendezvousAttendent" value="rendezvous/listByAttendant.do">
		<spring:param name="attendantId" value="${actor.getId()}" />
		</spring:url>
		<p><a href="${urlRendezvousAttendent }"> <spring:message code="actor.rendezvous.attendant" /></a></p>
	
	</jstl:if>
		
</div>

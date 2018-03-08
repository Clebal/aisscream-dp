<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme"	tagdir="/WEB-INF/tags"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


	
<div class="container">

	<jstl:if test="${request.getComments() != null}">
	<acme:display code="request.comments" value="${request.getComments()}"/>
	</jstl:if>
	
	<spring:url var="urlRendezvous" value="rendezvous/display.do">
	<spring:param name="rendezvousId" value="${request.getRendezvous().getId()}" />
	</spring:url>
	<p><a href="${urlRendezvous}"> <spring:message code="request.rendezvous" /></a></p>
	
	<spring:url var="urlCreditCard" value="creditCard/display.do">
	<spring:param name="creditCardId" value="${request.getCreditCard().getId()}" />
	</spring:url>
	<p><a href="${urlCreditCard}"> <spring:message code="request.creditCards" /></a></p>
	
	<spring:url var="urlServicio" value="servicio/display.do">
	<spring:param name="servicioId" value="${request.getServicio().getId()}" />
	</spring:url>
	<p><a href="${urlServicio}"> <spring:message code="request.services" /></a></p>
		
</div>
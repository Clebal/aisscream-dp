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
	
	<acme:display code="request.creditCards" value="${request.getCreditCard().getNumber()}"/>
	
	<spring:url var="urlRendezvous" value="rendezvous/display.do">
	<spring:param name="rendezvousId" value="${request.getRendezvous().getId()}" />
	</spring:url>
	<p><a href="${urlRendezvous}"> <spring:message code="request.rendezvous" /></a></p>
	
	<spring:url var="urlService" value="service/display.do">
	<spring:param name="serviceId" value="${request.getService().getId()}" />
	</spring:url>
	<p><a href="${urlService}"> <spring:message code="request.services" /></a></p>
		
</div>

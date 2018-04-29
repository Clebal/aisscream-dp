<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div class="container">

	<spring:message code="advertisement.alt" var="advertisementAlt"/>
	
	
	<acme:display code="advertisement.title" value="${advertisement.getTitle()}"/>
	
	<security:authorize access="hasRole('AGENT')">
		<acme:display code="advertisement.credit.card.number" value="${advertisement.getCreditCard().getNumber()}"/>
	</security:authorize>
	
	<acme:displayLink parametre="agentId" code="advertisement.agent" action="actor/agent/display.do" parametreValue="${advertisement.getAgent().getId()}" css="btn btn-primary"></acme:displayLink>		
	
</div>

	
	

<div class="container-square2">

	<acme:image value="${advertisement.getUrlBanner()}" alt="${advertisementAlt}" url="${advertisement.getUrlTarget()}"/>
	
</div>
		
	

	
	

	
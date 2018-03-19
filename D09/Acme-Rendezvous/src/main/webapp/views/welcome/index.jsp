<%--
 * index.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<div style="text-align: center">
	<h3>${welcomeMessage}</h3>
	<jstl:if test="${nameUser != null}">
		<h4><spring:message code="welcome.greeting.prefix" /> ${nameUser}<spring:message code="welcome.greeting.suffix" /></h4>
	</jstl:if>
	
	<spring:message code="welcome.format.moment" var="format"/>
	<fmt:parseDate value="${moment}" pattern="${format}" var="myDate"/>
	<p><spring:message code="welcome.greeting.current.time"/> <fmt:formatDate value="${myDate}" pattern="${format}"/></p>
</div>

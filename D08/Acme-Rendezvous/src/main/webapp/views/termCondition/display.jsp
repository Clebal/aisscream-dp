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
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div>

	<jstl:if test="${termCondition==null}">
		<spring:url var="urlEditSpanish" value="termCondition/edit.do">
			<spring:param name="code" value="es" />
		</spring:url>
		
		<spring:url var="urlEditEnglish" value="termCondition/edit.do">
			<spring:param name="code" value="en" />
		</spring:url>
			
		<p><a href="${urlEditSpanish}"><spring:message code="term.condition.edit.spanish" /></a></p>
		<p><a href="${urlEditEnglish}"><spring:message code="term.condition.edit.english" /></a></p>
		
		<p class="display"><spring:message code="term.condition.display.spanish" /></p>
		
		<jstl:out value="${termConditionSpanish}"></jstl:out>
		
		<p class="display"><spring:message code="term.condition.display.english" /></p>
		
		<jstl:out value="${termConditionEnglish}"></jstl:out>
		
	</jstl:if>
	
		<jstl:if test="${termConditionSpanish==null && termConditionEnglish==null}">
			<jstl:out value="${termCondition}"></jstl:out>
		</jstl:if>
	

</div>
<%--
 * submit.tag
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 

<%@ attribute name="property" required="true" %> 
<%@ attribute name="domain" required="true" %>
<%@ attribute name="formatDate" required="false" %>
<%@ attribute name="row" required="false" type="java.lang.Object" %>

<jstl:if test="${formatDate == null}">
	<jstl:set var="formatDate" value="false" />
</jstl:if>

<%-- Definition --%>
<spring:message code="${domain}.${property}" var="headerTitle" />

<jstl:if test="${property.equals('rendezvous')}">
	<display:column title="${headerTitle}">
		<a href="rendezvous/display.do?rendezvousId=${row.getRendezvous().getId()}"><jstl:out value="${row.getRendezvous().getName()}" /></a>
	</display:column>
</jstl:if>

<jstl:if test="${property.equals('user')}">
	<jstl:if test="${domain.equals('rsvp')}">
		<display:column title="${headerTitle}">
			<a href="actor/display.do?userId=${row.getAttendant().getId()}"><jstl:out value="${row.getAttendant().getName()}" /></a>
		</display:column>
	</jstl:if>
</jstl:if>

<jstl:if test="${!property.equals('rendezvous') && !property.equals('user')}">
	<jstl:if test="${formatDate == true}">
		<spring:message code="${domain}.format.moment" var="format"/>
		<display:column property="${property}" title="${headerTitle}" format="{0,date,${format}}" />
	</jstl:if>
	
	<jstl:if test="${formatDate == false}">
		<display:column property="${property}" title="${headerTitle}" />
	</jstl:if>
</jstl:if>

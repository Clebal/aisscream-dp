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

<%@ attribute name="url" required="true" %>
<%@ attribute name="haveRendezvousId" required="false" %>  
<%@ attribute name="objects" required="true" type="java.util.Collection" %> 

<jstl:if test="${haveRendezvousId == null}">
	<jstl:set var="haveRendezvousId" value="false" />
</jstl:if>
<%-- Definition --%>

<jstl:if test="${objects.size() > 0 }">

	<jstl:forEach var="i" begin="1" end="${pageNumber}">
	
		<spring:url var="urlNextPage" value="${url}">
		<jstl:if test="${haveRendezvousId==true }">
			<spring:param name="rendezvousId" value="${rendezvousId}" />
		</jstl:if>
			<spring:param name="page" value="${i}" />
		</spring:url>
			
		<jstl:if test="${page==i || page == 0}">
			<span  style='margin-right:10px;'><a href="${urlNextPage}" class='btn btn-danger'><jstl:out value="${i}"></jstl:out></a></span>
		</jstl:if>
		<jstl:if test="${page!=i && page != 0}">
			<span  style='margin-right:10px;'><a href="${urlNextPage}" class='btn btn-primary'><jstl:out value="${i}"></jstl:out></a></span>
		</jstl:if>
			
	</jstl:forEach>
	
</jstl:if>

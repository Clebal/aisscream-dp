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

<%@ attribute name="action" required="true" %>
<%@ attribute name="domain" required="true" %>
<%@ attribute name="id" required="true" %>

<%-- Definition --%>

<jstl:if test="${domain.equals('actor')}">

<spring:url value="${domain}/${action}.do" var="url">
	<spring:param name="${domain}Id" value="${id}" />
</spring:url>

</jstl:if>
<jstl:if test="${!domain.equals('actor')}">

<spring:url value="${domain}/user/${action}.do" var="url">
	<spring:param name="${domain}Id" value="${id}" />
</spring:url>

</jstl:if>

<display:column>
	<a href="${url}"><spring:message code="${domain}.${action}" /></a>
</display:column>

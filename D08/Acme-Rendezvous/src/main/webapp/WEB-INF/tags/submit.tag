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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 

<%@ attribute name="name" required="true" %> 
<%@ attribute name="code" required="true" %>
<%@ attribute name="cssClass" required="false" %>
<%@ attribute name="disabled" required="false" %>

<jstl:if test="${cssClass == null}">
	<jstl:set var="cssClass" value="" />
</jstl:if>

<jstl:if test="${disabled == null}">
	<jstl:set var="disabled" value="" />
</jstl:if>

<%-- Definition --%>

<button type="submit" name="${name}" ${disabled} class="btn btn-primary">
	<spring:message code="${code}" />
</button>

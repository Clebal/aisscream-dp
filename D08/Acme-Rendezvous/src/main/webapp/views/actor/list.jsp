<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme"	tagdir="/WEB-INF/tags"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table class="table table-striped table-bordered table-hover" name="users" id="row">
	
	<acme:column property="name" domain="actor"/>
	
	<acme:column property="surname" domain="actor"/>
	
	<acme:column property="birthdate" domain="actor" formatDate="true"/>
	
	<acme:column property="email" domain="actor"/>
	
	<acme:column property="phone" domain="actor"/>
	
	<acme:column property="address" domain="actor"/>
		
	<acme:columnLink domain="actor" action="display" id="${row.getId()}"/>
	
</display:table>

<div id="paginacion">
<acme:paginate url="${requestURI}" objects="${users}" page="${page}" pageNumber="${pageNumber}" />
</div>

<br><br>

<spring:url var="urlCreate" value="actor/user/create.do">
</spring:url>
<jstl:if test="${puedeCrear}">
<a href="${urlCreate }"> <spring:message code="actor.create" /></a>
</jstl:if>
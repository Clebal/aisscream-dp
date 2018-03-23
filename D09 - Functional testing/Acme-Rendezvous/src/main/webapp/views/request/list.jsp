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

<display:table class="table table-striped table-bordered table-hover" name="requests" id="row" requestURI="${requestURI}">
	
	<acme:columnLink action="delete" domain="request" id="${row.getId()}" />
	
	<acme:column property="comments" domain="request"/>
	
	<acme:columnLink domain="request" action="display" id="${row.getId()}"/>
	
</display:table>

<acme:paginate url="${requestURI}" objects="${requests}" pageNumber="${pageNumber}" page="${page}"/>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table class="table table-striped table-bordered table-hover" name="comments" id="row" requestURI="${requestURI}">
	
	<acme:column property="moment" domain="comment" formatDate="true"/>
	
	<acme:column property="text" domain="comment" />
	
	<acme:column property="user.name" domain="comment" />
	
	<acme:column property="bargain.productName" domain="comment" />
		
	<acme:columnLink action="display" domain="comment" id="${row.getId()}" />

</display:table>

<acme:paginate url="${requestURI}" objects="${comments}" pageNumber="${pageNumber}" page="${page}" />

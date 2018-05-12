<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table class="table table-striped table-bordered table-hover" name="tags" id="row" requestURI="${requestURI }" pagesize="5">

	<jstl:if test="${canEditOrCreate==true }">
	<acme:columnLink action="edit" domain="tag" actor="company" id="${row.getId()}" />
	</jstl:if>
	
	<acme:column domain="tag" property="name"/>
	
</display:table> 

<acme:paginate pageNumber="${pageNumber}" url="${requestURI}" objects="${tags}" page="${page}"/>

	<jstl:if test="${canEditOrCreate==true }">
	<spring:url var="urlCreate" value="tag/company/create.do"></spring:url>
	<p><a href="${urlCreate}"> <spring:message code="tag.create" /></a></p>
	</jstl:if>


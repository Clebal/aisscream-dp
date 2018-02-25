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

<!-- The announcements must be listed chronologically in descending order. -->
<display:table class="table table-striped table-bordered table-hover" name="announcements" id="row" defaultsort="2" requestURI="${requestURI}">
	
	<jstl:if test="${isCreator != null && isCreator}">
		<security:authorize access="hasRole('USER')">
			<acme:columnLink action="edit" domain="announcement" id="${row.getId()}" />
		</security:authorize>
	</jstl:if>
	
	<acme:column property="moment" domain="announcement" formatDate="true" />
	
	<acme:column property="title" domain="announcement" />

	<acme:column property="description" domain="announcement" />

	<acme:column property="rendezvous" domain="announcement" row="${row}" />
	
</display:table>

<jstl:if test="${announcements.size() > 0 }">

	<jstl:forEach var="i" begin="1" end="${pageNumber}">
	
		<spring:url var="urlNextPage" value="${requestURI}">
			<jstl:if test="${requestURI.equals('announcement/list.do') }">
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
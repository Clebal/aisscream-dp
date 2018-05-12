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

<display:table class="table table-striped table-bordered table-hover" name="rsvps" id="row" requestURI="${requestURI}">
	
	<jsp:useBean id="today" class="java.util.Date" />
	
	<jstl:if test="${!requestURI.equals('rsvp/list.do')}">
		<jstl:if test="${row.getRendezvous().getMoment()>=(today)  && !row.getRendezvous().getIsDeleted()}">
			<jstl:if test="${row.getStatus().equals('ACCEPTED') && !row.getRendezvous().getIsDeleted()}">
				<acme:columnLink action="cancel" domain="rsvp" id="${row.getId()}" />
			</jstl:if>
			
			<jstl:if test="${row.getStatus().equals('CANCELLED') && !row.getRendezvous().getIsDeleted()}">
				<acme:columnLink action="accept" domain="rsvp" id="${row.getId()}" />
			</jstl:if>
		</jstl:if>
		<jstl:if test="${row.getRendezvous().getMoment()<(today) || row.getRendezvous().getIsDeleted()}">
			<display:column>
			</display:column>
		</jstl:if>
	</jstl:if>
		
	<jstl:if test="${!requestURI.equals('rsvp/list.do')}">
		<acme:columnLink action="display" domain="rsvp" id="${row.getId()}" />
	</jstl:if>
	
	<jstl:if test="${requestURI.equals('rsvp/list.do')}">
		<acme:columnLink action="display" domain="rsvp" id="${row.getId()}" url="rsvp/display.do?rsvpId=${row.getId()}" />
	</jstl:if>
	
</display:table>

<jstl:if test="${!requestURI.equals('rsvp/list.do')}">
	<acme:paginate url="${requestURI}" objects="${rsvps}" pageNumber="${pageNumber}" page="${page}" />
</jstl:if>

<jstl:if test="${requestURI.equals('rsvp/list.do')}">
	<jstl:if test="${announcements.size() > 0 }">
	
		<jstl:forEach var="i" begin="1" end="${pageNumber}">
		
			<spring:url var="urlNextPage" value="${requestURI}">
				<spring:param name="rendezvousId" value="${rendezvousId}" />
				<spring:param name="page" value="${i}" />
			</spring:url>
				
			<jstl:if test="${page==i}">
				<span  style='margin-right:10px;'><a href="${urlNextPage}" class='btn btn-danger'><jstl:out value="${i}"></jstl:out></a></span>
			</jstl:if>
			<jstl:if test="${page!=i}">
				<span  style='margin-right:10px;'><a href="${urlNextPage}" class='btn btn-primary'><jstl:out value="${i}"></jstl:out></a></span>
			</jstl:if>
				
		</jstl:forEach>
		
	</jstl:if>
</jstl:if>

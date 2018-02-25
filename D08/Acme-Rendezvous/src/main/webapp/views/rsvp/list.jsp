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
	
	<jstl:if test="${row.getRendezvous().getMoment().compareTo(today) < 0 || !row.getRendezvous().getIsDeleted()}">
		<jstl:if test="${row.getStatus().equals('ACCEPTED')}">
			<acme:columnLink action="cancel" domain="rsvp" id="${row.getId()}" />
		</jstl:if>
		
		<jstl:if test="${row.getStatus().equals('CANCELLED')}">
			<acme:columnLink action="accept" domain="rsvp" id="${row.getId()}" />
		</jstl:if>
	</jstl:if>
	
	<acme:column property="status" domain="rsvp" />
	
	<jstl:if test="${requestURI.equals('rsvp/list.do')}">
		<acme:column property="user" domain="rsvp" />
	</jstl:if>

	<acme:column property="rendezvous" domain="rsvp" row="${row}" />
	
	<acme:columnLink action="display" domain="rsvp" id="${row.getId()}" />
		
</display:table>

<acme:paginate url="${requestURI}" objects="${rsvps}"/>

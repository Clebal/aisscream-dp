<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table class="table table-striped table-bordered table-hover" name="rsvps" id="row" requestURI="${requestURI}" pagesize="5" >
	
	<spring:url value="rsvp/user/edit.do" var="urlEdit">
		<spring:param name="rsvpId" value="${row.getId()}" />
	</spring:url>
	
	<jstl:if test="${row.getStatus().equals('ACCEPTED')}">
		<spring:url value="rsvp/user/cancel.do" var="urlCancel">
			<spring:param name="rsvpId" value="${row.getId()}" />
		</spring:url>
	
		<display:column>
			<a href="${urlCancel}"><spring:message code="rsvp.cancel" /></a>
		</display:column>
	</jstl:if>
	
	<jstl:if test="${row.getStatus().equals('CANCELLED')}">
		<spring:url value="rsvp/user/accept.do" var="urlAccept">
			<spring:param name="rsvpId" value="${row.getId()}" />
		</spring:url>
	
		<display:column>
			<a href="${urlAccept}"><spring:message code="rsvp.accept" /></a>
		</display:column>
	</jstl:if>
	
	<display:column>
		<a href="${urlEdit}"><spring:message code="rsvp.edit" /></a>
	</display:column>
	
	<spring:message code="rsvp.status" var="statusHeader" />
	<display:column property="status" title="${statusHeader}"></display:column>
	
	<spring:message code="rsvp.attendant" var="attendantHeader" />
	<display:column>
		<a href="actor/display.do?userId=${row.getAttendant().getId()}"><jstl:out value="${row.getAttendant().getName()} ${row.getAttendant().getSurname()}" /></a>
	</display:column>

	<spring:message code="rsvp.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"></display:column>
	
	<spring:message code="rsvp.rendezvous" var="rendezvousHeader" />
	<display:column title="${rendezvousHeader}">
		<a href="rendezvous/display.do?rendezvousId=${row.getRendezvous().getId()}"><jstl:out value="${row.getRendezvous().getTitle()}" /></a>
	</display:column>
	
	<display:column>
		<a href="rsvp/display.do?rsvpId=${row.getId()}"><spring:message code="rsvp.display" /></a>
	</display:column>
	
</display:table>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div>

	<acme:display code="rsvp.status" value="${rsvp.getStatus()}" />
		
	<p><span class="display"><spring:message code="rsvp.attendant" /></span>: <a href="actor/display.do?actorId=${rsvp.getAttendant().getId()}"><jstl:out value="${rsvp.getAttendant().getName()} ${rsvp.getAttendant().getSurname()}" /></a></p>

	<p><span class="display"><spring:message code="rsvp.rendezvous" /></span>: <a href="rendezvous/display.do?rendezvousId=${rsvp.getRendezvous().getId()}"><jstl:out value="${rsvp.getRendezvous().getName()}" /></a></p>

	<h2><spring:message code="rsvp.answers" /></h2>

	<c:forEach items="${questionAnswer}" var="entry">
  		<p style="font-size: 17.5px;"><span class="display"><spring:message code="rsvp.question" /> No <jstl:out value="${entry.key.getNumber()}" /></span>: <jstl:out value="${entry.key.getText()}" /></p>
  		<p><span class="display"><spring:message code="rsvp.answer" /></span>: <jstl:out value="${entry.value.getText()}" /></p>
	</c:forEach>

</div>
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

<div>

	<p><span class="display"><spring:message code="rsvp.status" /></span>: <jstl:out value="${rsvp.getStatus()}" /></p>

	<p><span class="display"><spring:message code="rsvp.attendant" /></span>: <a href="actor/display.do?userId=${rsvp.getAttendant().getId()}"><jstl:out value="${rsvp.getAttendant().getName()} ${rsvp.getAttendant().getSurname()}" /></a></p>

	<p><span class="display"><spring:message code="rsvp.rendezvous" /></span>: <a href="rendezvous/display.do?rendezvousId=${rsvp.getRendezvous().getId()}"><jstl:out value="${rsvp.getRendezvous().getTitle()}" /></a></p>

	<h3><spring:message code="rsvp.answers" /></h3>
		
	<c:forEach items="${questionAnswer}" var="entry">
  		<p style="font-size: 25px;"><span class="display"><spring:message code="rsvp.question" /> N. <jstl:out value="${entry.key.getNumber()}" /></span>: <jstl:out value="${entry.key.getText()}" /></p>
  		<p><span class="display"><spring:message code="rsvp.answer" /></span>: <jstl:out value="${entry.value.getText()}" /></p>
	</c:forEach>

</div>
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

<!-- The questions must be listed chronologically in descending order. -->
<display:table class="table table-striped table-bordered table-hover" name="rendezvouses" id="row" requestURI="${requestURI}" pagesize="5" >
	
	<spring:url value="question/user/edit.do" var="urlEdit">
		<spring:param name="questionId" value="${row.getId()}" />
	</spring:url>
	
	<display:column>
		<a href="${urlEdit}"><spring:message code="question.edit" /></a>
	</display:column>
	
	<spring:message code="question.number" var="numberHeader" />
	<display:column property="number" title="${numberHeader}"></display:column>

	<spring:message code="question.text" var="textHeader" />
	<display:column property="text" title="${textHeader}"></display:column>
	
	<spring:message code="question.rendezvous" var="rendezvousHeader" />
	<display:column>
		<a href="rendezvous/display.do?rendezvousId=${row.getRendezvous().getId()}"><jstl:out value="${row.getRendezvous().getTitle()}" /></a>
	</display:column>
	
</display:table>

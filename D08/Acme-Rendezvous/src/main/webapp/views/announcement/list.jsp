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

<!-- The announcements must be listed chronologically in descending order. -->
<display:table class="table table-striped table-bordered table-hover" name="rendezvouses" id="row" defaultsort="2" requestURI="${requestURI}" pagesize="5" >
	
	<spring:url value="announcement/user/edit.do" var="urlEdit">
	<spring:param name="announcementId" value="${row.getId()}" />
	</spring:url>
	
	<display:column>
		<a href="${urlEdit}"><spring:message code="announcement.edit" /></a>
	</display:column>
	
	<spring:message code="announcement.format.moment" var="momentFormat"/>
	<spring:message code="announcement.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}" format="{0,date,${momentFormat}}"></display:column>
	
	<spring:message code="announcement.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}"></display:column>

	<spring:message code="announcement.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"></display:column>
	
	<spring:message code="announcement.rendezvous" var="rendezvousHeader" />
	<display:column>
		<a href="rendezvous/display.do?rendezvousId=${row.getRendezvous().getId()}"><jstl:out value="${row.getRendezvous().getTitle()}" /></a>
	</display:column>
	
</display:table>

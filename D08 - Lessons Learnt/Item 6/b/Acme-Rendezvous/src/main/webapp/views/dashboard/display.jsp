<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


	<div>
	
		
		<div class="container">
			<acme:display code="dashboard.average.rendezvouses.per.user" value="${rendezvousesPerUser[0]}" formatNumber="true"/>
			
			<acme:display code="dashboard.standard.rendezvouses.per.user" value="${rendezvousesPerUser[1]}" formatNumber="true"/>
		 
		</div>
		<br/> 
		
		<div class="container">
			<acme:display code="dashboard.ratio.user.rendezvous.vs.no" value="${ratioUserRendezvousVsNo}" formatNumber="true"/>
			
		</div>
		<br/>
		
		<div class="container">
			<acme:display code="dashboard.average.users.per.rendezvous" value="${usersPerRendezvous[0]}" formatNumber="true"/>
			<acme:display code="dashboard.standard.users.per.rendezvous" value="${usersPerRendezvous[1]}" formatNumber="true"/>
			
			
		</div>
		<br/>
		
		<div class="container">
			<acme:display code="dashboard.average.rendezvouses.rsvpd.per.user" value="${rendezvousesRsvpdPerUser[0]}" formatNumber="true"/>
			<acme:display code="dashboard.standard.rendezvouses.rsvpd.per.user" value="${rendezvousesRsvpdPerUser[1]}" formatNumber="true"/>
			
		</div>
		<br/>
		
		<div class="container">
			<p class="display"><spring:message code="dashboard.rendezvouses.top"/></p>
			<jstl:forEach items="${rendezvousesTop}" var="row">
				<spring:url var="urlRendezvousesTop" value="rendezvous/display.do">
					<spring:param name="rendezvousId" value="${row.getId()}"></spring:param>
				</spring:url>
				<p style="margin-left: 15px;"><a href="${urlRendezvousesTop}" ><jstl:out value="${row.name}"></jstl:out></a></p>
			</jstl:forEach>
			
		</div>
		<br/>
		
		<div class="container">
			<acme:display code="dashboard.average.announcements.per.rendezvous" value="${announcementsPerRendezvous[0]}" formatNumber="true"/>
			<acme:display code="dashboard.standard.announcements.per.rendezvous" value="${announcementsPerRendezvous[1]}" formatNumber="true"/>
		
		</div>
		<br/>
		
		<div class="container">
		
			<spring:url var="urlRendezvousesNumberAnnouncement" value="rendezvous/administrator/listByNumberAnnouncements.do"/>
			<a href="${urlRendezvousesNumberAnnouncement}" ><spring:message code="dashboard.rendezvouses.number.announcement"/></a>
			
			
		</div>
		<br/>
		
		<div class="container">
			
			<spring:url var="urlRendezvousesLinked" value="rendezvous/administrator/listByLinkedNumber.do"/>
			<a href="${urlRendezvousesLinked}" ><spring:message code="dashboard.rendezvouses.linked"/></a>
			
		</div>
		<br/>
		
		<div class="container">
			<acme:display code="dashboard.average.questions.per.rendezvous" value="${questionsPerRendezvous[0]}" formatNumber="true"/>
			<acme:display code="dashboard.standard.questions.per.rendezvous" value="${questionsPerRendezvous[1]}" formatNumber="true"/>
			
		</div>
		<br/>
		
		<div class="container">
			<acme:display code="dashboard.average.answers.per.rendezvous" value="${answersPerRendezvous[0]}" formatNumber="true"/>
			<acme:display code="dashboard.standard.answers.per.rendezvous" value="${answersPerRendezvous[1]}" formatNumber="true"/>
			
		</div>
		<br/>
		
		<div class="container">
			<acme:display code="dashboard.average.replies.per.comment" value="${repliesPerComment[0]}" formatNumber="true"/>
			<acme:display code="dashboard.standard.replies.per.comment" value="${repliesPerComment[1]}" formatNumber="true"/>
			
		</div>
		<br/>
		
		

	
	</div>
	
	

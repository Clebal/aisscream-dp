<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


	<div>
	
		
		<div class="container">
			<span class="display"><spring:message code="dashboard.average.rendezvouses.per.user"/></span><fmt:formatNumber value="${rendezvousesPerUser[0]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			<span class="display"><spring:message code="dashboard.standard.rendezvouses.per.user"/></span><fmt:formatNumber value="${rendezvousesPerUser[1]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			
		</div>
		<br/>
		
		<div class="container">
			<span class="display"><spring:message code="dashboard.ratio.user.rendezvous.vs.no"/></span><fmt:formatNumber value="${ratioUserRendezvousVsNo}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			
		</div>
		<br/>
		
		<div class="container">
			<span class="display"><spring:message code="dashboard.average.users.per.rendezvous"/></span><fmt:formatNumber value="${usersPerRendezvous[0]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			<span class="display"><spring:message code="dashboard.standard.users.per.rendezvous"/></span><fmt:formatNumber value="${usersPerRendezvous[1]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			
		</div>
		<br/>
		
		<div class="container">
			<span class="display"><spring:message code="dashboard.average.rendezvouses.rsvpd.per.user"/></span><fmt:formatNumber value="${rendezvousesRsvpdPerUser[0]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			<span class="display"><spring:message code="dashboard.standard.rendezvouses.rsvpd.per.user"/></span><fmt:formatNumber value="${rendezvousesRsvpdPerUser[1]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			
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
			<span class="display"><spring:message code="dashboard.average.announcements.per.rendezvous"/></span><fmt:formatNumber value="${announcementsPerRendezvous[0]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			<span class="display"><spring:message code="dashboard.standard.announcements.per.rendezvous"/></span><fmt:formatNumber value="${announcementsPerRendezvous[1]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			
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
			<span class="display"><spring:message code="dashboard.average.questions.per.rendezvous"/></span><fmt:formatNumber value="${questionsPerRendezvous[0]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			<span class="display"><spring:message code="dashboard.standard.questions.per.rendezvous"/></span><fmt:formatNumber value="${questionsPerRendezvous[1]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			
		</div>
		<br/>
		
		<div class="container">
			<span class="display"><spring:message code="dashboard.average.answers.per.rendezvous"/></span><fmt:formatNumber value="${answersPerRendezvous[0]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			<span class="display"><spring:message code="dashboard.standard.answers.per.rendezvous"/></span><fmt:formatNumber value="${answersPerRendezvous[1]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			
		</div>
		<br/>
		
		<div class="container">
			<span class="display"><spring:message code="dashboard.average.replies.per.comment"/></span><fmt:formatNumber value="${repliesPerComment[0]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			<span class="display"><spring:message code="dashboard.standard.replies.per.comment"/></span><fmt:formatNumber value="${repliesPerComment[1]}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/>
			<br/>
			
		</div>
		<br/>
		
		

	
	</div>
	
	

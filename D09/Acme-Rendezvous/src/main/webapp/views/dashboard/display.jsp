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
		
		<!-- Nuevos -->
		
		<div class="container">
			<p class="display"><spring:message code="dashboard.bestSellingServices"/></p>
			<jstl:forEach items="${bestSellingServices}" var="row">
				<spring:url var="urlBestSellingServices" value="servicio/display.do">
					<spring:param name="servicioId" value="${row.getId()}"></spring:param>
				</spring:url>
				<p style="margin-left: 15px;"><a href="${urlBestSellingServices}" ><jstl:out value="${row.name}"></jstl:out></a></p>
			</jstl:forEach>
			
			<acme:paginate pageNumber="${pageNumber}" url="dashboard/administrator/display.do" objects="${bestSellingServices}" page="${page}"/>
			
		</div>
		<br/>
		
		<div class="container">
			<p class="display"><spring:message code="dashboard.managerMoreServicesAverage"/></p>
			<jstl:forEach items="${managerMoreServicesAverage}" var="row">
				<spring:url var="urlManagerMoreServicesAverage" value="manager/display.do">
					<spring:param name="managerId" value="${row.getId()}"></spring:param>
				</spring:url>
				<p style="margin-left: 15px;"><a href="${urlManagerMoreServicesAverage}" ><jstl:out value="${row.name}"></jstl:out></a></p>
			</jstl:forEach>
			
		</div>
		<br/>

		<div class="container">
			<p class="display"><spring:message code="dashboard.managerMoreServicesCancelled"/></p>
			<jstl:forEach items="${managerMoreServicesCancelled}" var="row">
				<spring:url var="urlManagerMoreServicesCancelled" value="manager/display.do">
					<spring:param name="managerId" value="${row.getId()}"></spring:param>
				</spring:url>
				<p style="margin-left: 15px;"><a href="${urlManagerMoreServicesCancelled}" ><jstl:out value="${row.name}"></jstl:out></a></p>
			</jstl:forEach>
			
		</div>
		<br/>
		
		<div class="container">
			<acme:display code="dashboard.avgNumberCategoriesPerRendezvous" value="${avgNumberCategoriesPerRendezvous}" formatNumber="true"/>
		</div>
		<br/>
		
		<div class="container">
			<acme:display code="dashboard.avgRatioServicesCategory" value="${avgRatioServicesCategory}" formatNumber="true"/>
		</div>
		<br/>
		
		<div class="container">
			<acme:display code="dashboard.avgServicesPerRendezvous" value="${avgMinMaxStandardDesviationServicesPerRendezvous[0]}" formatNumber="true"/>
			<acme:display code="dashboard.minServicesPerRendezvous" value="${avgMinMaxStandardDesviationServicesPerRendezvous[1]}" formatNumber="true"/>
			<acme:display code="dashboard.maxServicesPerRendezvous" value="${avgMinMaxStandardDesviationServicesPerRendezvous[2]}" formatNumber="true"/>
			<acme:display code="dashboard.standardDesviationServicesPerRendezvous" value="${avgMinMaxStandardDesviationServicesPerRendezvous[3]}" formatNumber="true"/>
		</div>
		<br/>
	
		<div class="container">
			<p class="display"><spring:message code="dashboard.topSellingServices"/></p>
			<div class="container"><div class="text-danger">
			<p class="display"><spring:message code="dashboard.mostrar"/>${size}</p>
			<div class="container"><ul class="nav nav-pills">
			<jstl:if test="${size == 5}">
				<li><a class="btn btn-default" href="dashboard/administrator/display.do?size=10"><spring:message code="dashboard.display10" /></a><br /></li>
				<li><a class="btn btn-default" href="dashboard/administrator/display.do?size=20"><spring:message code="dashboard.display20" /></a><br /></li>
			</jstl:if>
			<jstl:if test="${size == 10}">
				<li><a class="btn btn-default" href="dashboard/administrator/display.do?size=5"><spring:message code="dashboard.display5" /></a><br /></li>
				<li><a class="btn btn-default" href="dashboard/administrator/display.do?size=20"><spring:message code="dashboard.display20" /></a><br /><li>
			</jstl:if>
			<jstl:if test="${size == 20}">
				<li><a class="btn btn-default" href="dashboard/administrator/display.do?size=5"><spring:message code="dashboard.display5" /></a><br /></li>
				<li><a class="btn btn-default" href="dashboard/administrator/display.do?size=10"><spring:message code="dashboard.display10" /></a><br /></li>
			</jstl:if>
			</ul></div></div></div>
			<jstl:forEach items="${topSellingServices}" var="row">
				<spring:url var="urlTopSellingServices" value="servicio/display.do">
					<spring:param name="servicioId" value="${row.getId()}"></spring:param>
				</spring:url>
				<p style="margin-left: 15px;"><a href="${urlTopSellingServices}" ><jstl:out value="${row.name}"></jstl:out></a></p>
			</jstl:forEach>
			
		</div>
		<br/>
		
	</div>
	
	

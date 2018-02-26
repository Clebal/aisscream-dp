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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<jstl:if test="${rendezvous.getAdultOnly()==false || rendezvous.getAdultOnly()==true && canPermit}">

<jstl:if test="${rendezvous.getIsDeleted()==true}">
<span class="display"><spring:message code="rendezvous.isDeleted.message"/></span>
</jstl:if>
<br>
<jstl:if test="${rendezvous.getPicture()!=null && rendezvous.getPicture()!='' }">
	<img src="${rendezvous.getPicture()}" alt="Picture" width="400px" height="200px" style="margin-left:15px;" />
</jstl:if>
	<div>
		<div class="container">
			
			<acme:display code="rendezvous.name" value="${rendezvous.getName()}"/>
			
			<acme:display code="rendezvous.description" value="${rendezvous.getDescription()}"/>
			
			<acme:display code="rendezvous.moment" value="${rendezvous.getMoment()}" codeMoment="rendezvous.format.moment"/>
		
			<jstl:if test="${rendezvous.getDraft()==true}">
				<p><span class="display"><spring:message code="rendezvous.draft.trueMessage"/></span></p>
			</jstl:if>
			
			<jstl:if test="${rendezvous.getDraft()==false}">
				<p><span class="display"><spring:message code="rendezvous.draft.falseMessage"/></span></p>
			</jstl:if>
			
			<jstl:if test="${rendezvous.getAdultOnly()==true}">
				<p><span class="display"><spring:message code="rendezvous.adultOnly.trueMessage"/></span></p>
			</jstl:if>
			
			<jstl:if test="${rendezvous.getAdultOnly()==false}">
				<p><span class="display"><spring:message code="rendezvous.adultOnly.falseMessage"/></span></p>
			</jstl:if>
			
			<acme:display code="rendezvous.latitude" value="${rendezvous.getLatitude()}"/>
			
			<acme:display code="rendezvous.longitude" value="${rendezvous.getLongitude()}"/>
						
		</div>
			
		<div class="container">
			
				<span style="font-size:20px"><spring:message code="rendezvous.other.actions"/></span>
				<br>
				<br>
				
				<acme:displayLink parametre="actorId" code="rendezvous.creator.display" action="actor/display.do" parametreValue="${rendezvous.getCreator().getId()}"/>
				
				<acme:displayLink parametre="rendezvousId" code="rendezvous.listAttendants.display" action="actor/listAttendants.do" parametreValue="${rendezvous.getId()}"/>
				
				<acme:displayLink parametre="rendezvousId" code="rendezvous.linkerRendezvouses.display" action="rendezvous/listLinkerRendezvouses.do" parametreValue="${rendezvous.getId()}"/>
				
				<acme:displayLink parametre="rendezvousId" code="rendezvous.linkedRendezvouses.display" action="rendezvous/listLinkedRendezvouses.do" parametreValue="${rendezvous.getId()}"/>
				
				
				<jstl:if test="${!canStreamAnnouncements }">
					<acme:displayLink parametre="rendezvousId" code="rendezvous.announcements.display" action="announcement/list.do" parametreValue="${rendezvous.getId()}"/>
				</jstl:if>
				<acme:displayLink parametre="rendezvousId" code="rendezvous.rsvps.display" action="rsvp/list.do" parametreValue="${rendezvous.getId()}"/>
								
		</div>
		
		
	
		<!-- Si es un auditor le permitimos crear una note y un audit-->
		<security:authorize access="hasRole('ADMIN')">
			<jstl:if test="${rendezvous.getIsDeleted()==false}">
				<div class="container">
			
				<span style="font-size:20px"><spring:message code="rendezvous.administrator.actions"/></span>
				<br>
				<br>
			<!-- Enlace para borrar el Rendezvous -->
			<acme:displayLink parametre="rendezvousId" code="rendezvous.deleteForAdmin" action="rendezvous/administrator/delete.do" parametreValue="${rendezvous.getId()}"/>
				
				</div>
			</jstl:if>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<div class="container">
			
			<span style="font-size:20px"><spring:message code="rendezvous.user.actions"/></span>
			<br>
			<br>
			
			<security:authentication var="principal" property="principal.username"/>
			<jstl:if test="${rendezvous.getCreator().getUserAccount().getUsername().equals(principal) && rendezvous.getIsDeleted()==false}">
					
					<!-- Lo enlazamos con otro rendezvous-->
					<acme:displayLink parametre="rendezvousId" code="rendezvous.rendezvousesForLink" action="rendezvous/user/listRendezvousesForLink.do" parametreValue="${rendezvous.getId()}"/>
					 
					<!-- Creamos una question-->
					<acme:displayLink parametre="rendezvousId" code="rendezvous.question.create" action="question/user/create.do" parametreValue="${rendezvous.getId()}"/>
					
					<!-- Creamos un announcement-->
					<acme:displayLink parametre="rendezvousId" code="rendezvous.announcement.create" action="announcement/user/create.do" parametreValue="${rendezvous.getId()}"/>
					
			</jstl:if>
					
			<jstl:if test="${rendezvous.getIsDeleted()==false }">	
					<!-- Creamos un RSVP-->
				<jstl:if test="${canCreateRSVP }">	
					<acme:displayLink parametre="rendezvousId" code="rendezvous.rsvp.create" action="rsvp/user/create.do" parametreValue="${rendezvous.getId()}"/>		
				</jstl:if>
			
			<!-- Si es el creador o un attendant de ese trip le permitimos crear un comentario-->
				<jstl:if test="${canCreateComment }">
					<acme:displayLink parametre="rendezvousId" code="rendezvous.comment.create" action="comment/user/create.do" parametreValue="${rendezvous.getId()}"/>					
				</jstl:if>
			</jstl:if>	
			</div>
			
			</security:authorize>
			
			<jstl:if test="${canStreamAnnouncements }">
			
			<jstl:if test="${!announcements.isEmpty()}">
		
			<div>
				<span style="font-size:20px"><spring:message code="rendezvous.announcements"></spring:message></span>
				<br>
				<br>
				<jstl:forEach var="row3" items="${announcements}">
				
					
					<div class="container-square2" style="border:2px solid black; margin-left:25px; margin-bottom:20px; padding:10px;">
						<span class="display"><spring:message code="rendezvous.announcement.moment"/></span><fmt:formatDate value="${row3.getMoment()}" pattern="${momentFormat }"/>
						<br>
						
						<span class="display"><spring:message code="rendezvous.announcement.title"/></span><jstl:out value="  ${row3.getTitle()}" />
						<br>
						
						<span class="display"><spring:message code="rendezvous.announcement.description"/></span><jstl:out value="  ${row3.getDescription()}" />
						<br>		
					
						<br>
						<br>	

					</div>
					<br>
				</jstl:forEach>
			<jstl:forEach var="i" begin="1" end="${pageNumber2}">
	
			<spring:url var="urlMorePageAnnouncement" value="rendezvous/display.do">
				<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
				<spring:param name="page" value="${page}" />
				<spring:param name="page2" value="${i}" />
			</spring:url>
			
			<jstl:if test="${page2==i}">
				<span  style='margin-right:10px;'><a href="${urlMorePageAnnouncement}" class='btn btn-danger'><jstl:out value="${i}"></jstl:out></a></span>
			</jstl:if>
			<jstl:if test="${page2!=i}">
				<span  style='margin-right:10px;'><a href="${urlMorePageAnnouncement}" class='btn btn-primary'><jstl:out value="${i}"></jstl:out></a></span>
			</jstl:if>
			
	</jstl:forEach>
			</div>
		</jstl:if>
		</jstl:if>
			
		<br>	
																
			<jstl:if test="${!comments.isEmpty()}">
		
			<div>
				<span style="font-size:20px"><spring:message code="rendezvous.comments"></spring:message></span>
				<br>
				<br>
				<jstl:forEach var="row2" items="${comments}">
				
					<spring:url var="urlMoreComments" value="comment/display.do">
						<spring:param name="commentId" value="${row2.getId()}" />
						<spring:param name="page" value="1" />
					</spring:url>
					
					<div class="container-square2" style="border:2px solid black; margin-left:25px; margin-bottom:20px; padding:10px;">
						<span class="display"><spring:message code="rendezvous.comment.text"/></span><jstl:out value="${row2.getText()}" />
						<br>
						
						<span class="display"><spring:message code="rendezvous.comment.moment"/></span><fmt:formatDate value="${row2.getMoment()}" pattern="${momentFormat }"/>
						<br>
						<jstl:if test="${row2.getPicture()!=null && row2.getPicture()!=''}">
							<span> 
								<img src="${row2.getPicture()}" alt="Picture" width="400px" height="200px" style="margin-left:15px;" />
							</span>
						</jstl:if>
					
						<br>
						<br>	
					 	<p><a href="${urlMoreComments}" class='btn btn-primary' style='margin-right:10px;'><spring:message code="comment.more"/></a></p>

					</div>
					<br>
				</jstl:forEach>
			<jstl:forEach var="i" begin="1" end="${pageNumber}">
	
			<spring:url var="urlMorePage" value="rendezvous/display.do">
				<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
				<spring:param name="page" value="${i}" />
				<spring:param name="page2" value="${page2}" />
				
			</spring:url>
			
			<jstl:if test="${page==i}">
				<span  style='margin-right:10px;'><a href="${urlMorePage}" class='btn btn-danger'><jstl:out value="${i}"></jstl:out></a></span>
			</jstl:if>
			<jstl:if test="${page!=i}">
				<span  style='margin-right:10px;'><a href="${urlMorePage}" class='btn btn-primary'><jstl:out value="${i}"></jstl:out></a></span>
			</jstl:if>
			
	</jstl:forEach>
			</div>
		</jstl:if>
			

		
	</div>
	</jstl:if>
	
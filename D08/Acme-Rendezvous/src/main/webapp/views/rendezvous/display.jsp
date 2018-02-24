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
			<spring:message code="rendezvous.format.moment" var="momentFormat"/>
			
			<p><span class="display"><spring:message code="rendezvous.name"/></span><jstl:out value="  ${rendezvous.getName()}" /></p>
		
			<p><span class="display"><spring:message code="rendezvous.description"/></span><jstl:out value="  ${rendezvous.getDescription()}" /></p>
			
			<p><span class="display"><spring:message code="rendezvous.moment"/></span><fmt:formatDate value="${rendezvous.getMoment()}" pattern="${momentFormat }"/></p>
			
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
			
			<p><span class="display"><spring:message code="rendezvous.latitude"/></span><jstl:out value="${rendezvous.getLatitude()}"/></p>
			
			<p><span class="display"><spring:message code="rendezvous.longitude"/></span><jstl:out value="${rendezvous.getLongitude()}"/></p>
			
		</div>
		
		
		<spring:url var="urlCreator" value="actor/display.do">
			<spring:param name="actorId" value="${rendezvous.getCreator().getId()}" />
		</spring:url>
		
		<spring:url var="urlLinkerRendezvouses" value="rendezvous/listLinkerRendezvouses.do">
			<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
		</spring:url>
		
		<spring:url var="urlLinkedRendezvouses" value="rendezvous/listLinkedRendezvouses.do">
			<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
		</spring:url>
		
		<spring:url var="urlAnnouncements" value="announcement/list.do">
			<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
		</spring:url>
		
		<spring:url var="urlRsvps" value="rsvp/list.do">
			<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
		</spring:url>
		
		
		<div class="container">
			
				<span style="font-size:20px"><spring:message code="rendezvous.other.actions"/></span>
				<br>
				<br>
				<a href="${urlCreator}" ><spring:message code="rendezvous.creator.display"/></a>
				<br>
					
				<a href="${urlLinkerRendezvouses}" ><spring:message code="rendezvous.linkerRendezvouses.display"/></a>
				<br>
				<a href="${urlLinkedRendezvouses}" ><spring:message code="rendezvous.linkedRendezvouses.display"/></a>
				<br>
				<a href="${urlAnnouncements}" ><spring:message code="rendezvous.announcements.display"/></a>
				<br>
				<a href="${urlRsvps}" ><spring:message code="rendezvous.rsvps.display"/></a>
				<br>	
				
		</div>
		
		
	
		<!-- Si es un auditor le permitimos crear una note y un audit-->
		<security:authorize access="hasRole('ADMIN')">
			<jstl:if test="${rendezvous.getIsDeleted()==false}">
			<div class="container">
			
				<span style="font-size:20px"><spring:message code="rendezvous.administrator.actions"/></span>
				<br>
				<br>
				<!-- Enlace para borrar el Rendezvous -->
				<spring:url var="urlDeleteRendezvous" value="rendezvous/administrator/delete.do">
					<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
				</spring:url>
				<a href="${urlDeleteRendezvous}" ><spring:message code="rendezvous.deleteForAdmin"/></a>
				<br>
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
					<spring:url var="urlRendezvousesForLink" value="rendezvous/user/listRendezvousesForLink.do">
						<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
					</spring:url>
					
					<a href="${urlRendezvousesForLink}" ><spring:message code="rendezvous.rendezvousesForLink"/></a>
					<br>
					 
					<!-- Lo desenlazamos con otro rendezvous-->
					<!-- 
					<spring:url var="urlRendezvousesForUnLink" value="rendezvous/user/listRendezvousesForUnLink.do">
						<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
					</spring:url>
					
					<a href="${urlRendezvousesForUnLink}" ><spring:message code="rendezvous.rendezvousesForUnLink"/></a>
					<br/>
					-->
					<!-- Creamos una question-->
					<spring:url var="urlCreateQuestion" value="question/user/create.do">
						<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
					</spring:url>
					
					<a href="${urlCreateQuestion}" ><spring:message code="rendezvous.question.create"/></a>
					<br>
					
					<!-- Creamos un announcement-->
					<spring:url var="urlCreateAnnouncement" value="announcement/user/create.do">
						<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
					</spring:url>
					
					<a href="${urlCreateAnnouncement}" ><spring:message code="rendezvous.announcement.create"/></a>
					<br>
			</jstl:if>
					
			<jstl:if test="${rendezvous.getIsDeleted()==false }">	
					<!-- Creamos un RSVPt-->
			<jstl:if test="${canCreateRSVP }">
					
					<spring:url var="urlCreateRSVP" value="rsvp/user/create.do">
						<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
					</spring:url>
					
					<a href="${urlCreateRSVP}" ><spring:message code="rendezvous.rsvp.create"/></a>
					<br>
			</jstl:if>
			
			<!-- Si es el creador o un attendant de ese trip le permitimos crear un comentario-->
			<jstl:if test="${canCreateComment }">
				
					
					<spring:url var="urlCreateComment" value="comment/user/create.do">
								<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
					</spring:url>
							
					<a href="${urlCreateComment}" ><spring:message code="rendezvous.comment.create"/></a>
					<br>
					
			</jstl:if>
			</jstl:if>	
			</div>
			
			
			</security:authorize>
											
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
	
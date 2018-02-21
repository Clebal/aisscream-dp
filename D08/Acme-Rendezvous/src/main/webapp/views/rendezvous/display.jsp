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
<jstl:if test="${rendezvous.getPicture()!=null}">
	<img src="${rendezvous.getPicture()}" alt="Picture" width="400px" height="200px" style="margin-left:15px;" />
</jstl:if>
	<div>
		<div class="container">
			<spring:message code="rendezvous.format.moment" var="momentFormat"/>
			
			<span class="display"><spring:message code="rendezvous.name"/></span><jstl:out value="  ${rendezvous.getName()}" />
			<br/>
		
			<span class="display"><spring:message code="rendezvous.description"/></span><jstl:out value="  ${rendezvous.getDescription()}" />
			<br/>
			
			<span class="display"><spring:message code="rendezvous.moment"/></span><fmt:formatDate value="${rendezvous.getMoment()}" pattern="${momentFormat }"/>
			<br/>
			
			<jstl:if test="${rendezvous.getDraft()==true}">
				<span class="display"><spring:message code="rendezvous.draft.trueMessage"/></span>
			<br/>
			</jstl:if>
			
			<jstl:if test="${rendezvous.getDraft()==false}">
				<span class="display"><spring:message code="rendezvous.draft.falseMessage"/></span>
			<br/>
			</jstl:if>
			
			<jstl:if test="${rendezvous.getAdultOnly()==true}">
				<span class="display"><spring:message code="rendezvous.adultOnly.trueMessage"/></span>
			<br/>
			</jstl:if>
			
			<jstl:if test="${rendezvous.getAdultOnly()==false}">
				<span class="display"><spring:message code="rendezvous.adultOnly.falseMessage"/></span>
			<br/>
			</jstl:if>
			
			<span class="display"><spring:message code="rendezvous.latitude"/></span><jstl:out value="${rendezvous.getLatitude()}"/>
			<br/>
			
			<span class="display"><spring:message code="rendezvous.longitude"/></span><jstl:out value="${rendezvous.getLongitude()}"/>
			<br/>
			
		</div>
		
		
		<!-- Mostramos las preguntas si las hay-->
		
		<jstl:if test="${!questions.isEmpty()}">
		
			<div class="container">
				<span style="font-size:20px"><spring:message code="rendezvous.questions"></spring:message></span>
				<br/>
				<br/>
				<jstl:forEach var="row" items="${questions}">
					<div style="border:2px solid black; margin-left:25px; margin-bottom:20px; padding:10px;">
						<span class="display"><spring:message code="rendezvous.question.text"/></span><jstl:out value="${row.getText()}" />
						<br/>
					</div>
					<br/>
				</jstl:forEach>
			</div>
		</jstl:if>
		
		<spring:url var="urlCreator" value="actor/user/display.do">
			<spring:param name="userId" value="${rendezvous.getCreator().getId()}" />
		</spring:url>
			
		<spring:url var="urlComments" value="comment/list.do">
			<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
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
				<br/>
				<br/>
				<a href="${urlCreator}" ><spring:message code="rendezvous.creator.display"/></a>
				<br/>
				<!-- 
				<a href="${urlComments}" ><spring:message code="rendezvous.comments.display"/></a>
				<br/>
				 -->	
				<a href="${urlLinkerRendezvouses}" ><spring:message code="rendezvous.linkerRendezvouses.display"/></a>
				<br/>
				<a href="${urlLinkedRendezvouses}" ><spring:message code="rendezvous.linkedRendezvouses.display"/></a>
				<br/>
				<a href="${urlAnnouncements}" ><spring:message code="rendezvous.announcements.display"/></a>
				<br/>
				<a href="${urlRsvps}" ><spring:message code="rendezvous.rsvps.display"/></a>
				<br/>	
				
		</div>
		
		
	
		<!-- Si es un auditor le permitimos crear una note y un audit-->
		<security:authorize access="hasRole('ADMIN')">
			<jstl:if test="${rendezvous.getIsDeleted()==false}">
			<div class="container">
			
				<span style="font-size:20px"><spring:message code="rendezvous.administrator.actions"/></span>
				<br/>
				<br/>
				<!-- Enlace para borrar el Rendezvous -->
				<spring:url var="urlDeleteRendezvous" value="rendezvous/administrator/delete.do">
					<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
				</spring:url>
				<a href="${urlDeleteRendezvous}" ><spring:message code="rendezvous.deleteForAdmin"/></a>
				<br/>
			</div>
				</jstl:if>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<div class="container">
			
			<span style="font-size:20px"><spring:message code="rendezvous.user.actions"/></span>
					<br/>
					<br/>
			
			<security:authentication var="principal" property="principal.username"/>
			<jstl:if test="${rendezvous.getCreator().getUserAccount().getUsername().equals(principal) && rendezvous.getIsDeleted()==false}">
					
					<!-- Lo enlazamos con otro rendezvous-->
					<spring:url var="urlRendezvousesForLink" value="rendezvous/user/listRendezvousesForLink.do">
						<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
					</spring:url>
					
					<a href="${urlRendezvousesForLink}" ><spring:message code="rendezvous.rendezvousesForLink"/></a>
					<br/>
					 
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
					<br/>
					
					<!-- Creamos un announcement-->
					<spring:url var="urlCreateAnnouncement" value="announcement/user/create.do">
						<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
					</spring:url>
					
					<a href="${urlCreateAnnouncement}" ><spring:message code="rendezvous.announcement.create"/></a>
					<br/>
			</jstl:if>
					
			<jstl:if test="${rendezvous.getIsDeleted()==false }">	
					<!-- Creamos un RSVPt-->
			<jstl:if test="${canCreateRSVP }">
					
					<spring:url var="urlCreateRSVP" value="rsvp/user/create.do">
						<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
					</spring:url>
					
					<a href="${urlCreateRSVP}" ><spring:message code="rendezvous.rsvp.create"/></a>
					<br/>
			</jstl:if>
			
			<!-- Si es el creador o un attendant de ese trip le permitimos crear un comentario-->
			<jstl:if test="${canCreateComment }">
				
					
					<spring:url var="urlCreateComment" value="comment/user/create.do">
								<spring:param name="rendezvousId" value="${rendezvous.getId()}" />
					</spring:url>
							
					<a href="${urlCreateComment}" ><spring:message code="rendezvous.comment.create"/></a>
					<br/>
					
			</jstl:if>
			</jstl:if>	
			</div>
			
			
			</security:authorize>
			
			
			<jstl:if test="${!comments.isEmpty()}">
		
			<div class="container">
				<span style="font-size:20px"><spring:message code="rendezvous.comments"></spring:message></span>
				<br/>
				<br/>
				<jstl:forEach var="row2" items="${comments}">
					<div style="border:2px solid black; margin-left:25px; margin-bottom:20px; padding:10px;">
						<span class="display"><spring:message code="rendezvous.comment.text"/></span><jstl:out value="${row2.getText()}" />
						<br/>
						
						<span class="display"><spring:message code="rendezvous.comment.moment"/></span><fmt:formatDate value="${row2.getMoment()}" pattern="${momentFormat }"/>
						<br/>
						<jstl:if test="${row2.getPicture()!=null}">
							<span> 
								<img src="${row2.getPicture()}" alt="Picture" width="400px" height="200px" style="margin-left:15px;" />
							</span>
						</jstl:if>
					<jstl:if test="${canCreateComment }">
				
					
					<spring:url var="urlCreateReply" value="comment/user/createReply.do">
								<spring:param name="commentId" value="${row2.getId()}" />
					</spring:url>
							
					<a href="${urlCreateReply}" ><spring:message code="rendezvous.comment.reply"/></a>
					<br/>
					
					</jstl:if>
					</div>
					<br/>
				</jstl:forEach>
			</div>
		</jstl:if>
			

		
	</div>
	</jstl:if>
	
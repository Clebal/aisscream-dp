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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<display:table class="table table-striped table-bordered table-hover" name="rendezvouses" id="row">
	
	<jsp:useBean id="currentMomentVar" class="java.util.Date"/>
	<security:authorize access="hasRole('USER')">
		<security:authentication var="principal" property="principal.username"/>

		<display:column>
		<jstl:if test="${row.getCreator().getUserAccount().getUsername().equals(principal)}">
				<jstl:if test="${row.getDraft()==true && row.getIsDeleted()==false}">
			<a href="rendezvous/user/edit.do?rendezvousId=${row.getId()}"> <spring:message
					code="rendezvous.edit" />
			</a>
				</jstl:if>	
		
				</jstl:if>
		</display:column>
				
		
		
		<!-- Para que aparezcan los enlaces de addlink cuando accedamos desde el diplay para enlazar -->
		<jstl:if test="${requestURI.equals('rendezvous/user/listRendezvousesForLink.do') }">
		<display:column>
		<jstl:if test="${canLink && !myRendezvousIsDeleted && row.getIsDeleted()==false}">
		<spring:url var="urlLinkRendezvous" value="rendezvous/user/linkRendezvous.do">
						<spring:param name="myRendezvousId" value="${rendezvousId}" />
						<spring:param name="linkedRendezvousId" value="${row.getId()}" />
					</spring:url>
		<a href="${urlLinkRendezvous }"> <spring:message
					code="rendezvous.link" />
			</a>
					</jstl:if>
			
		</display:column>
		</jstl:if>
		
		<!-- Para que aparezcan los enlaces de removelink cuando accedamos desde el diplay para desenlazar -->
		<jstl:if test="${requestURI.equals('rendezvous/listLinkedRendezvouses.do') }">
		<display:column>
		<jstl:if test="${canUnLink && !myRendezvousIsDeleted && row.getIsDeleted()==false}">
		<spring:url var="urlUnLinkRendezvous" value="rendezvous/user/unLinkRendezvous.do">
						<spring:param name="myRendezvousId" value="${rendezvousId}" />
						<spring:param name="linkedRendezvousId" value="${row.getId()}" />
					</spring:url>
		<a href="${urlUnLinkRendezvous }"> <spring:message
					code="rendezvous.unLink" />
			</a>
			</jstl:if>
		</display:column>
		</jstl:if>
		
	</security:authorize>
	
	<acme:column property="name" domain="rendezvous" />
	
	<acme:column property="description" domain="rendezvous" />
	
	<acme:column property="moment" domain="rendezvous" formatDate="true" />
	
	<acme:columnBoolean property="draft" domain="rendezvous" row="${row}"/>
	
	<acme:columnBoolean property="adultOnly" domain="rendezvous" row="${row}"/>
	
	<acme:column property="longitude" domain="rendezvous"/>
		
	<acme:column property="latitude" domain="rendezvous"/>
		
	<acme:columnBoolean property="isDeleted" domain="rendezvous" row="${row}"/>
		
	<spring:message code="rendezvous.moment.isPast" var="momentIsPast"/>
	<display:column title="${momentIsPast}">
		<jstl:if test="${row.getMoment().compareTo(currentMomentVar)<=0}">
		 	<jstl:out value="X"/>
		</jstl:if>
	</display:column>
		
	<spring:url var="urlDisplay" value="rendezvous/display.do">
		<spring:param name="rendezvousId" value="${row.getId()}" />
		
	</spring:url>
	
	<display:column>
		<a href="${urlDisplay }"> <spring:message code="rendezvous.display" /></a>
	</display:column>
	
</display:table>

	<jstl:if test="${requestURI.equals('rendezvous/listByUser.do') }">
		<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${rendezvouses}" page="${page }" parameter="userId" parameterValue="${userId}"/>
	</jstl:if>
	
	<jstl:if test="${requestURI.equals('rendezvous/listByAttendant.do') }">
		<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${rendezvouses}" page="${page }" parameter="attendantId" parameterValue="${attendantId}"/>
	</jstl:if>
	
<jstl:if test="${!requestURI.equals('rendezvous/listByUser.do') && !requestURI.equals('rendezvous/listByAttendant.do')  }">
	
	<jstl:if test="${haveRendezvousId==true }">
	<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${rendezvouses}" page="${page }" parameter="rendezvousId" parameterValue="${rendezvousId}"/>
	</jstl:if>
	
	<jstl:if test="${haveRendezvousId==false}">
	<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${rendezvouses}" page="${page }"/>
	</jstl:if>
</jstl:if>	

<br>
<br>
<security:authorize access="hasRole('USER')">
	<div>
		<a href="rendezvous/user/create.do">
			<spring:message code="rendezvous.create" />
		</a>
	</div>
</security:authorize>

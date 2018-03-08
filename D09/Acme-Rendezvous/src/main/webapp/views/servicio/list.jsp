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



<display:table class="table table-striped table-bordered table-hover" name="servicios" id="row">
	
<jstl:if test="${requestURI.equals('servicio/manager/list.do')}">
	
	<security:authorize access="hasRole('MANAGER')">
		<security:authentication var="principal" property="principal.username"/>

		<display:column>
		<jstl:if test="${row.getManager().getUserAccount().getUsername().equals(principal)}">
			<a href="servicio/manager/edit.do?servicioId=${row.getId()}"> <spring:message
					code="servicio.edit" />
			</a>
				</jstl:if>	
		
		</display:column>
				
	</security:authorize>	
</jstl:if>	
		<!-- Para que aparezcan los enlaces de addlink cuando accedamos desde el diplay para enlazar -->
		<security:authorize access="hasRole('ADMIN')">
		
			<display:column>
		
			<jstl:if test="${row.getStatus().equals('ACCEPTED')}">
					<spring:url var="urlAcceptServicio" value="servicio/administrator/cancel.do">
						<spring:param name="servicioId" value="${row.getId()}" />
					</spring:url>
					<a href="${urlAcceptServicio }"> <spring:message
					code="servicio.cancel" />
					</a>
			</jstl:if>
			
			<jstl:if test="${row.getStatus().equals('CANCELLED')}">
					<spring:url var="urlCancelledServicio" value="servicio/administrator/accept.do">
						<spring:param name="servicioId" value="${row.getId()}" />
					</spring:url>
					<a href="${urlCancelledServicio }"> <spring:message
					code="servicio.accept" />
					</a>
			</jstl:if>
			
			</display:column>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<jstl:if test="${requestURI.equals('servicio/user/listForRequestByRendezvous.do')}">
					<spring:url var="urlCreateRequest" value="request/user/request.do">
						<spring:param name="rendezvousId" value="${rendezvousId}" />
						<spring:param name="servicioId" value="${row.getId()}" />
						
					</spring:url>
					<display:column>
					
					<a href="${urlCreateRequest }"> <spring:message
					code="servicio.create.request" />
					</a>
					</display:column>
			</jstl:if>
		</security:authorize>
		
		
	<acme:column property="name" domain="servicio" />
	
	<acme:column property="description" domain="servicio" />
		
	<spring:message code="servicio.status.isAccepted" var="statusIsAccepted"/>
	<display:column title="${statusIsAccepted}">
		<jstl:if test="${row.getStatus().equals('ACCEPTED')}">
		 	<jstl:out value="X"/>
		</jstl:if>
	</display:column>
		
	<spring:url var="urlDisplay" value="servicio/display.do">
		<spring:param name="servicioId" value="${row.getId()}" />
		
	</spring:url>
	
	<display:column>
		<a href="${urlDisplay }"> <spring:message code="servicio.display" /></a>
	</display:column>
	
</display:table>

<jstl:if test="${requestURI.equals('servicio/manager/list.do') || requestURI.equals('servicio/list.do')}">
		<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${servicios}" page="${page}"/>
</jstl:if>

<jstl:if test="${requestURI.equals('servicio/user/listForRequestByRendezvous.do') || requestURI.equals('servicio/listByRendezvousId.do')}">
		<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${servicios}" page="${page}" parameter="rendezvousId" parameterValue="${rendezvousId}"/>
</jstl:if>

<jstl:if test="${requestURI.equals('servicio/listByCategoryId.do')}">
		<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${servicios}" page="${page}" parameter="categoryId" parameterValue="${categoryId}"/>
</jstl:if>



<security:authorize access="hasRole('MANAGER')">
	<div>
	<br>
		<a href="servicio/manager/create.do">
			<spring:message code="servicio.create" />
		</a>
	</div>
</security:authorize>

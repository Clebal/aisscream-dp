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



<display:table class="table table-striped table-bordered table-hover" name="services" id="row">
	
<jstl:if test="${requestURI.equals('service/manager/list.do')}">
	
	<security:authorize access="hasRole('MANAGER')">
		<security:authentication var="principal" property="principal.username"/>

		<display:column>
		<jstl:if test="${row.getManager().getUserAccount().getUsername().equals(principal)}">
			<a href="service/manager/edit.do?serviceId=${row.getId()}"> <spring:message
					code="service.edit" />
			</a>
				</jstl:if>	
		
		</display:column>
				
	</security:authorize>	
</jstl:if>	
		<!-- Para que aparezcan los enlaces de addlink cuando accedamos desde el diplay para enlazar -->
		<security:authorize access="hasRole('ADMIN')">
		
			<display:column>
		
			<jstl:if test="${row.getStatus().equals('ACCEPTED')}">
					<spring:url var="urlAcceptService" value="service/administrator/cancel.do">
						<spring:param name="serviceId" value="${row.getId()}" />
					</spring:url>
					<a href="${urlAcceptService }"> <spring:message
					code="service.cancel" />
					</a>
			</jstl:if>
			
			<jstl:if test="${row.getStatus().equals('CANCELLED')}">
					<spring:url var="urlCancelledService" value="service/administrator/accept.do">
						<spring:param name="serviceId" value="${row.getId()}" />
					</spring:url>
					<a href="${urlCancelledService }"> <spring:message
					code="service.accept" />
					</a>
			</jstl:if>
			
			</display:column>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<jstl:if test="${requestURI.equals('service/user/listForRequestByRendezvous.do')}">
					<spring:url var="urlCreateRequest" value="request/user/request.do">
						<spring:param name="rendezvousId" value="${rendezvousId}" />
						<spring:param name="serviceId" value="${row.getId()}" />
						
					</spring:url>
					<display:column>
					
					<a href="${urlCreateRequest }"> <spring:message
					code="service.create.request" />
					</a>
					</display:column>
			</jstl:if>
		</security:authorize>
		
		
	<acme:column property="name" domain="service" />
	
	<acme:column property="description" domain="service" />
		
	<spring:message code="service.status.isAccepted" var="statusIsAccepted"/>
	<display:column title="${statusIsAccepted}">
		<jstl:if test="${row.getStatus().equals('ACCEPTED')}">
		 	<jstl:out value="X"/>
		</jstl:if>
	</display:column>
		
	<spring:url var="urlDisplay" value="service/display.do">
		<spring:param name="serviceId" value="${row.getId()}" />
		
	</spring:url>
	
	<display:column>
		<a href="${urlDisplay }"> <spring:message code="service.display" /></a>
	</display:column>
	
</display:table>

<jstl:if test="${requestURI.equals('service/manager/list.do') || requestURI.equals('service/list.do') || requestURI.equals('service/administrator/bestSellingServices.do')}">
		<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${services}" page="${page}"/>
</jstl:if>

<jstl:if test="${requestURI.equals('service/user/listForRequestByRendezvous.do') || requestURI.equals('service/listByRendezvousId.do')}">
		<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${services}" page="${page}" parameter="rendezvousId" parameterValue="${rendezvousId}"/>
</jstl:if>

<jstl:if test="${requestURI.equals('service/listByCategoryId.do')}">
		<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${services}" page="${page}" parameter="categoryId" parameterValue="${categoryId}"/>
</jstl:if>



<security:authorize access="hasRole('MANAGER')">
	<div>
	<br>
		<a href="category/manager/createService.do">
			<spring:message code="service.create" />
		</a>
	</div>
</security:authorize>

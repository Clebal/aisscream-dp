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



<jstl:if test="${servicio.getPicture()!=null && servicio.getPicture()!='' }">
	<img src="${servicio.getPicture()}" alt="Picture" width="400px" height="200px" style="margin-left:15px;" />
</jstl:if>
<br>
	<div>
		<div class="container">
			
			<acme:display code="servicio.name" value="${servicio.getName()}"/>
			
			<acme:display code="servicio.description" value="${servicio.getDescription()}"/>
					
			<acme:display code="servicio.status" value="${servicio.getStatus()}"/>
						
		</div>
			
		<!-- Si es el manager del servicio podemos añadir o borrar categories-->
		<security:authorize access="hasRole('MANAGER')">
				<security:authentication var="principal" property="principal.username"/>
		
			<jstl:if test="${servicio.getManager().getUserAccount().getUsername().equals(principal)}">
				<div class="container">
			
				
			<acme:displayLink parametre="servicioId" code="servicio.addCategory" action="category/manager/addCategory.do" parametreValue="${servicio.getId()}"/>
			
			<acme:displayLink parametre="servicioId" code="servicio.removeCategory" action="category/manager/removeCategory.do" parametreValue="${servicio.getId()}"/>
			
				
				</div>
			</jstl:if>
		</security:authorize>
																
			<jstl:if test="${!categories.isEmpty()}">
		
			<div>
				<span style="font-size:20px"><spring:message code="servicio.categories"></spring:message></span>
				<br>
				<br>
				<jstl:forEach var="row" items="${categories}">
				
					<spring:url var="urlMoreCategories" value="servicio/display.do">
						<spring:param name="servicioId" value="${row.getId()}" />
					</spring:url>
					
					<div class="container-square2" style="border:2px solid black; margin-left:25px; margin-bottom:20px; padding:10px;">
						<span class="display"><spring:message code="servicio.category.name"/></span><jstl:out value="${row.getName()}" />
						<br>
						<span class="display"><spring:message code="servicio.category.description"/></span><jstl:out value="${row.getDescription()}" />
						
						<br>
						<br>	
					 	<p><a href="${urlMoreCategories}" class='btn btn-primary' style='margin-right:10px;'><spring:message code="servicio.category.more"/></a></p>

					</div>
					<br>
				</jstl:forEach>
				
			<acme:paginate pageNumber="${pageNumber }" url="servicio/display.do" objects="${categories}" page="${page }" parameter="servicioId" parameterValue="${servicio.getId()}"/>
			
			</div>
		</jstl:if>
			

		
	</div>
	
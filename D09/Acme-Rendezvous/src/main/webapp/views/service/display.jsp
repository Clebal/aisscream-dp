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



<jstl:if test="${service.getPicture()!=null && service.getPicture()!='' }">
	<img src="${service.getPicture()}" alt="Picture" width="400px" height="200px" style="margin-left:15px;" />
</jstl:if>
<br>
	<div>
		<div class="container">
			
			<acme:display code="service.name" value="${service.getName()}"/>
			
			<acme:display code="service.description" value="${service.getDescription()}"/>
					
			<acme:display code="service.status" value="${service.getStatus()}"/>
						
		</div>
			
		<!-- Si es el manager del service podemos añadir o borrar categories-->
		<security:authorize access="hasRole('MANAGER')">
				<security:authentication var="principal" property="principal.username"/>
		
			<jstl:if test="${service.getManager().getUserAccount().getUsername().equals(principal)}">
				<div class="container">
			
				
			<acme:displayLink parametre="serviceId" code="service.addCategory" action="category/manager/addCategory.do" parametreValue="${service.getId()}"/>
			
			<acme:displayLink parametre="serviceId" code="service.removeCategory" action="category/manager/removeCategory.do" parametreValue="${service.getId()}"/>
			
				
				</div>
			</jstl:if>
		</security:authorize>
																
			<jstl:if test="${!categories.isEmpty()}">
		
			<div>
				<span style="font-size:20px"><spring:message code="service.categories"></spring:message></span>
				<br>
				<br>
				<jstl:forEach var="row" items="${categories}">
				
					<spring:url var="urlMoreCategories" value="category/display.do">
						<spring:param name="categoryId" value="${row.getId()}" />
					</spring:url>
					
					<div class="container-square2" style="border:2px solid black; margin-left:25px; margin-bottom:20px; padding:10px;">
						<span class="display"><spring:message code="service.category.name"/></span><jstl:out value="${row.getName()}" />
						<br>
						<span class="display"><spring:message code="service.category.description"/></span><jstl:out value="${row.getDescription()}" />
						
						<br>
						<br>	
					 	<p><a href="${urlMoreCategories}" class='btn btn-primary' style='margin-right:10px;'><spring:message code="service.category.more"/></a></p>

					</div>
					<br>
				</jstl:forEach>
				
			<acme:paginate pageNumber="${pageNumber }" url="service/display.do" objects="${categories}" page="${page }" parameter="serviceId" parameterValue="${service.getId()}"/>
			
			</div>
		</jstl:if>
			

		
	</div>
	
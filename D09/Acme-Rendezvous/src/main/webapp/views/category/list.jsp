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



<display:table class="table table-striped table-bordered table-hover" name="categories" id="row">
	

		
		
			<display:column>
		
			<jstl:if test="${action.equals('add')}">
					<spring:url var="urlAddCategory" value="servicio/manager/addCategory.do">
						<spring:param name="servicioId" value="${servicioId}" />
						<spring:param name="categoryId" value="${row.getId()}" />
					</spring:url>
					<a href="${urlAddCategory }"> <spring:message code="category.add" /></a>
			</jstl:if>
			
			<jstl:if test="${action.equals('remove')}">
				<spring:url var="urlRemoveCategory" value="servicio/manager/removeCategory.do">
					<spring:param name="servicioId" value="${servicioId}" />
					<spring:param name="categoryId" value="${row.getId()}" />
				</spring:url>
				<a href="${urlRemoveCategory }"> <spring:message code="category.remove" /></a>
			</jstl:if>
			
			
			
			</display:column>
			
			<spring:message code="category.name" var="nameHeader" />
			<display:column property="name" title="${nameHeader}" ></display:column>
			
			<spring:message code="category.description" var="descriptionHeader" />
			<display:column property="description" title="${descriptionHeader}" ></display:column>
		
		
	
	
</display:table>

<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${categories}" page="${page}" parameter="servicioId" parameterValue="${servicioId}"/>





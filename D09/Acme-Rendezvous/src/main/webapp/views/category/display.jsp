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

<div class="container">

	<jstl:if test="${category!=null}">
		<acme:display code="category.name" value="${category.getName()}"/>
	
		<acme:display code="category.description" value="${category.getDescription()}"/>
		
		<jstl:if test="${category.getFatherCategory()!=null}">
			<acme:displayLink parametre="categoryId" code="category.father.category" action="category/display.do" parametreValue="${category.getFatherCategory().getId()}" parametre2="categoryToMoveId" parametreValue2="${categoryToMoveId}" css="btn btn-primary"></acme:displayLink>		
		</jstl:if>
		
		<jstl:if test="${category.getFatherCategory()==null}">
			<acme:displayLink code="category.father.category" action="category/display.do"  parametre="categoryToMoveId" parametreValue="${categoryToMoveId}" css="btn btn-primary"></acme:displayLink>		
		</jstl:if>
		
		<security:authorize access="hasRole('ADMIN')">
			<acme:displayLink parametre="categoryId" code="category.edit" action="category/administrator/edit.do" parametreValue="${category.getId()}" css="btn btn-primary"></acme:displayLink>
			<acme:displayLink parametre="fatherCategoryId" code="category.create" action="category/administrator/create.do" parametreValue="${category.getId()}" css="btn btn-primary"></acme:displayLink>
			
			<!-- Ponemos enlaces para controlar la reorganización -->
			<jstl:if test="${categoryToMoveId==null}">
				<acme:displayLink parametre="categoryId" code="category.move.this" action="category/display.do" parametreValue="${category.getId()}" parametre2="page" parametreValue2="${page}" parametre3="categoryToMoveId" parametreValue3="${category.getId()}" css="btn btn-primary"></acme:displayLink>
			</jstl:if>
			
			<jstl:if test="${categoryToMoveId!=null}">
				
				<jstl:if test="${categoryToMoveId!= category.getId()}">
					<acme:displayLink parametre="categoryNewFatherId" code="category.move.here" action="category/administrator/reorganising.do" parametreValue="${category.getId()}" parametre2="categoryToMoveId" parametreValue2="${categoryToMoveId}" css="btn btn-warning"></acme:displayLink>
				</jstl:if>
				
				<jstl:if test="${categoryToMoveId== category.getId()}">
					<spring:message code="category.navigation" var="navigation"></spring:message>
					<p><jstl:out value="${navigation}"></jstl:out></p>
				</jstl:if>
				<acme:displayLink parametre="categoryId" code="category.move.cancel" action="category/display.do" parametreValue="${category.getId()}" parametre2="page" parametreValue2="${page}" parametre3="categoryToMoveId" parametreValue3="" css="btn btn-danger"></acme:displayLink>
			
			</jstl:if>
		</security:authorize>
		
		<acme:displayLink code="category.rendezvous" action="rendezvous/bycategory.do"  parametre="categoryId" parametreValue="${category.getId()}" css="btn btn-primary"></acme:displayLink>	
		
	</jstl:if>
	
	<jstl:if test="${category==null}">
		<security:authorize access="hasRole('ADMIN')">		
			<acme:displayLink code="category.create" action="category/administrator/create.do" css="btn btn-primary"></acme:displayLink>
		</security:authorize>
		
		<!-- Mover a la raíz -->
		<jstl:if test="${categoryToMoveId!=null}">
			<acme:displayLink parametre="categoryNewFatherId" code="category.move.here" action="category/administrator/reorganising.do" parametreValue="${0}" parametre2="categoryToMoveId" parametreValue2="${categoryToMoveId}" css="btn btn-warning"></acme:displayLink>
		</jstl:if>
	</jstl:if>
	
	
	
</div>

<jstl:if test="${childrenCategories.size()>0}">
	
	<jstl:forEach var="row" items="${childrenCategories}">
		
		<div class="container-square2">
	
			<acme:display code="category.name" value="${row.getName()}"/>
	
			<acme:display code="category.description" value="${row.getDescription()}"/>
			
			<acme:displayLink parametre="categoryId" code="category.children" action="category/display.do" parametreValue="${row.getId()}" parametre2="categoryToMoveId" parametreValue2="${categoryToMoveId}" css="btn btn-primary"></acme:displayLink>		
			
			
		</div>
		
	</jstl:forEach>
	
	
	<acme:paginate url="category/display.do" objects="${childrenCategories}" parameter="categoryId" parameterValue="${category.getId()}" parameter2="categoryToMoveId" parameterValue2="${categoryToMoveId}" page="${page}" pageNumber="${pageNumber}"/>
	
</jstl:if> 
	
	

	
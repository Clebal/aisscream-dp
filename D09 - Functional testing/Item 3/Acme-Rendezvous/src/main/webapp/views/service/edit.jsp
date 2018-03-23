<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="service/manager/edit.do" modelAttribute="service">

	<form:hidden path="id"/>
 	<jstl:if test="${service.getId()==0}">
 		<form:hidden path="manager"/>
 		<form:hidden path="status"/>
 		<form:hidden path="categories"/>
 	 </jstl:if>
 	
 	
 	
	<acme:textbox path="name" code="service.name" />
	
	<acme:textarea code="service.description" path="description"/>
		
	<acme:textbox code="service.picture" path="picture"/>
	
	<acme:submit name="save" code="service.save" />
	
	<jstl:if test="${service.getId()!= 0 && canDelete}">
	<acme:submit name="delete" code="service.delete" codeDelete="service.confirm.delete"/>
	</jstl:if>
	
	<acme:cancel code="service.cancel" url="service/manager/list.do"/>
	
</form:form>
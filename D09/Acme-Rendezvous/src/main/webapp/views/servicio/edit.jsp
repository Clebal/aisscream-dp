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


<form:form action="servicio/manager/edit.do" modelAttribute="servicio">

	<form:hidden path="id"/>
 	<jstl:if test="${servicio.getId()==0}">
 		<form:hidden path="manager"/>
 		<form:hidden path="status"/>
 		<form:hidden path="categories"/>
 	 </jstl:if>
 	
 	
 	
	<acme:textbox path="name" code="servicio.name" />
	
	<acme:textarea code="servicio.description" path="description"/>
		
	<acme:textbox code="servicio.picture" path="picture"/>
	
	<acme:submit name="save" code="servicio.save" />
	
	<jstl:if test="${servicio.getId()!= 0 && canDelete}">
	<acme:submit name="delete" code="servicio.delete" codeDelete="servicio.confirm.delete"/>
	</jstl:if>
	
	<acme:cancel code="servicio.cancel" url="servicio/manager/list.do"/>
	
</form:form>
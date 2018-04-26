<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="newspaper/user/edit.do" modelAttribute="newspaper">

	<form:hidden path="id" />
	<form:hidden path="publisher" />
	<form:hidden path="isPublished" />
	<form:hidden path="articles" />
	
	
		
	<acme:textbox code="newspaper.title" path="title"/>

	<acme:textbox code="newspaper.description" path="description"/>
	
	<acme:textbox path="publicationDate" code="newspaper.publicationDate" placeholder="dd/MM/yyyy" />
	
	<acme:textbox code="newspaper.picture" path="picture"/>
	
	<acme:checkbox code="newspaper.isPrivate" path="isPrivate"/>
	
	 
	<acme:submit name="save" code="newspaper.save"/>
	
	<acme:cancel url="newspaper/user/list.do" code="newspaper.cancel"/>
			
</form:form>
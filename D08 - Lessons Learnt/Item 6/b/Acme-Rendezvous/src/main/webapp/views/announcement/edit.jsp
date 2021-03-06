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

<form:form action="announcement/user/edit.do" modelAttribute="announcement">

	<form:hidden path="id"/>
	<jstl:if test="${announcement.getId() == 0}">
	 	<form:hidden path="rendezvous"/>
 	</jstl:if>

	<acme:textbox path="moment" code="announcement.moment" readonly="true" />
 	
	<acme:textbox path="title" code="announcement.title"/>
	
	<acme:textbox path="description" code="announcement.description"/>

	<acme:submit name="save" code="announcement.save" />
	
	<jstl:if test="${announcement.getId()!= 0}">
		<acme:submit name="delete" code="announcement.delete" />
	</jstl:if>
	
	<acme:cancel code="announcement.cancel" url="announcement/user/list.do" />
	
</form:form>
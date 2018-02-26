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

<form:form action="question/user/edit.do" modelAttribute="question">

	<form:hidden path="id"/>
	<jstl:if test="${question.getId() == 0}">
 		<form:hidden path="rendezvous"/>
 	</jstl:if>
 	
 	<jstl:if test="${question.getId()!= 0}">
 		<acme:textbox path="number" code="question.number" readonly="true" />
 	</jstl:if>
 	
	<acme:textbox path="text" code="question.text" />

	<acme:submit name="save" code="question.save" />

	<jstl:if test="${question.getId()!= 0}">
		<acme:submit name="delete" code="question.delete" />
	</jstl:if>
	
	<acme:cancel code="question.cancel" url="question/user/list.do" />

</form:form>
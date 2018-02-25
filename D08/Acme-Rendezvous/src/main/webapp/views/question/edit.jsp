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
	<form:hidden path="version"/>
 	<form:hidden path="rendezvous"/>
 	
	<acme:textbox path="text" code="question.text" />

	<acme:submit name="save" code="question.save" cssClass="btn btn-primary" />

	<jstl:if test="${question.getId()!= 0}">
		<acme:submit name="delete" code="question.delete" cssClass="btn btn-warning" />
	</jstl:if>
	
	<acme:cancel code="question.cancel" url="question/user/list.do" cssClass="btn btn-danger" />

</form:form>
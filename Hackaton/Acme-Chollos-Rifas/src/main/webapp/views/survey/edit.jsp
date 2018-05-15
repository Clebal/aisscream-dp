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

<form:form action="survey/${model}/edit.do" modelAttribute="surveyForm">

	<form:hidden path="id"/>

	<acme:textbox code="survey.title" path="title" />
	
	<jstl:if test="${surveyForm.getId() == 0}">
	
		<acme:surveyInput codeQuestion="survey.question" codeAnswer="survey.answer" />
	
	</jstl:if>
	
	<acme:submit name="save" code="survey.save" />

	<acme:cancel url="survey/${model}/list.do" code="survey.cancel"/>

</form:form>
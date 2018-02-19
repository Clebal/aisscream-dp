<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="question/user/edit.do" modelAttribute="question">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
 	<form:hidden path="rendezvous"/>
 	
	<div class="form-group"> 
		<form:label path="number">
			<spring:message code="question.number"/>
		</form:label>
		<form:input class="form-control" path="number" />
		<form:errors class="text-danger" path="number"/>
	</div>
 	
	<div class="form-group"> 
		<form:label path="text">
			<spring:message code="question.text"/>
		</form:label>
		<form:input class="form-control" path="text"/>
		<form:errors class="text-danger" path="text"/>
	</div>
	
	<input type="submit" class="btn btn-primary" name="save" value="<spring:message code="question.save" />">
	
	<jstl:if test="${question.getId()!= 0}">
		<input type="submit" class="btn btn-warning" name="delete" value="<spring:message code="question.delete" />">
	</jstl:if>
	
	<input type="button" class="btn btn-danger" name="cancel" value="<spring:message code="question.cancel" />" onclick="javascript: relativeRedir('question/user/list.do');" >

</form:form>
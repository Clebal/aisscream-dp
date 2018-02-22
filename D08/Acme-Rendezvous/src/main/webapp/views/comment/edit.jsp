<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="comment/user/edit.do" modelAttribute="comment">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
 	<form:hidden path="user"/>
 	<form:hidden path="rendezvous"/>
 	
	

	<div class="form-group"> 
		<form:label path="picture">
			<spring:message code="comment.picture"/>
		</form:label>
		<form:input class="form-control" path="picture"/>
		<form:errors class="text-danger" path="picture"/>
	</div>
	
	<div class="form-group"> 
		<form:label path="text">
			<spring:message code="comment.text" />:
		</form:label>
		<form:textarea path="text"/>
		<form:errors class="text-danger" path="text"/>
	</div>
	
	<div class="form-group"> 
		<form:label path="moment">
			<spring:message code="comment.moment"/>
		</form:label>
		<form:input class="form-control" path="moment" placeholder="dd/MM/yyyy HH:mm" readonly="true"/>
		<form:errors class="text-danger" path="moment"/>
	</div>
	
	<security:authorize access="hasRole('USER')">
		<security:authentication var="principal" property="principal.username"/>
		<jstl:if test="${comment.getUser().getUserAccount().getUsername().equals(principal) && comment.getId()==0}">
			<input type="submit" class="btn btn-primary" name="save" value="<spring:message code="comment.save" />">
		</jstl:if>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<jstl:if test="${comment.getId()!= 0}">
			<input type="submit" class="btn btn-warning" name="delete" value="<spring:message code="comment.delete" />" onclick="return confirm('<spring:message code="comment.confirm.delete" />')">
		</jstl:if>
	</security:authorize>
	
	<input type="button" class="btn btn-danger" name="cancel" value="<spring:message code="comment.cancel" />" onclick="javascript: relativeRedir('comment/display.do?commentId=${comment.getId()}');" >

</form:form>
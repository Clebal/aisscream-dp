<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="announcement/user/edit.do" modelAttribute="announcement">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
 	<form:hidden path="rendezvous"/>
 	
	<div class="form-group"> 
		<form:label path="moment">
			<spring:message code="announcement.moment"/>
		</form:label>
		<form:input class="form-control" path="moment" placeholder="dd/MM/yyyy HH:mm" disabled/>
		<form:errors class="text-danger" path="moment"/>
	</div>
 	
	<div class="form-group"> 
		<form:label path="title">
			<spring:message code="announcement.title"/>
		</form:label>
		<form:input class="form-control" path="title"/>
		<form:errors class="text-danger" path="title"/>
	</div>
	
	<div class="form-group"> 
		<form:label path="description">
			<spring:message code="announcement.description" />:
		</form:label>
		<form:textarea path="description"/>
		<form:errors class="text-danger" path="description"/>
	</div>
	
	<input type="submit" class="btn btn-primary" name="save" value="<spring:message code="announcement.save" />">
	
	<jstl:if test="${announcement.getId()!= 0}">
		<input type="submit" class="btn btn-warning" name="delete" value="<spring:message code="announcement.delete" />">
	</jstl:if>
	
	<input type="button" class="btn btn-danger" name="cancel" value="<spring:message code="announcement.cancel" />" onclick="javascript: relativeRedir('announcement/user/list.do');" >

</form:form>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="rendezvous/user/edit.do" modelAttribute="rendezvous">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
 	<form:hidden path="creator"/>
 	<form:hidden path="isDeleted"/>
 	<form:hidden path="linkerRendezvouses"/>
 	
	

	<div class="form-group"> 
		<form:label path="name">
			<spring:message code="rendezvous.name"/>
		</form:label>
		<form:input class="form-control" path="name"/>
		<form:errors class="text-danger" path="name"/>
	</div>
	
	<div class="form-group"> 
		<form:label path="description">
			<spring:message code="rendezvous.description" />:
		</form:label>
		<form:textarea path="description"/>
		<form:errors class="text-danger" path="description"/>
	</div>
	
		<div class="form-group"> 
			<form:label path="moment">
				<spring:message code="rendezvous.moment"/>
			</form:label>
			<form:input class="form-control" path="moment" placeholder="dd/MM/yyyy HH:mm"/>
			<form:errors class="text-danger" path="moment"/>
		</div>
	
	
	<div class="form-group"> 
		<form:label path="picture">
			<spring:message code="rendezvous.picture" />:
		</form:label>
		<form:textarea class="form-control" path="picture"/>
		<form:errors class="text-danger" path="picture"/>
	</div>
	
	<div class="form-group"> 
		<form:label path="draft">
			<spring:message code="rendezvous.draft" />:
		</form:label>
		<form:checkbox path="draft" />
	</div>
	
	<div class="form-group"> 
		<form:label path="adultOnly">
			<spring:message code="rendezvous.adultOnly" />:
		</form:label>
		<form:checkbox path="adultOnly" />
	</div>
	
	<div class="form-group"> 
		<form:label path="latitude">
			<spring:message code="rendezvous.latitude" />:
		</form:label>
		<form:input class="form-control" path="latitude"/>
		<form:errors class="text-danger" path="latitude"/>
	</div>
	
	<div class="form-group"> 
		<form:label path="longitude">
			<spring:message code="rendezvous.longitude" />:
		</form:label>
		<form:input class="form-control" path="longitude"/>
		<form:errors class="text-danger" path="longitude"/>
	</div>
	
	
		<input type="submit" class="btn btn-primary" name="save" value="<spring:message code="rendezvous.save" />">
	<jstl:if test="${rendezvous.getId()!= 0}">
		<input type="submit" class="btn btn-warning" name="delete" value="<spring:message code="rendezvous.delete" />" onclick="return confirm('<spring:message code="rendezvous.confirm.delete" />')">
	</jstl:if>
	
	<input type="button" class="btn btn-danger" name="cancel" value="<spring:message code="rendezvous.cancel" />" onclick="javascript: relativeRedir('rendezvous/user/list.do');" >

</form:form>
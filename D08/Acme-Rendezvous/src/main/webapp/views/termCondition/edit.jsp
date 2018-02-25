<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="termCondition/administrator/edit.do" modelAttribute="internationalization">

	<form:hidden path="id" />
	
	<div class="form-group"> 
		<form:label path="value">
				<spring:message code="term.condition.edit" />
			
		</form:label>
		<form:textarea class="form-control" rows="20" cols="20" path="value"/>
		<form:errors css="text-danger" path="value"/>
	</div>
	
	<input type="submit" class="btn btn-primary" name="save" value="<spring:message code="term.condition.save" />" >

	<input type="button" class="btn btn-danger" name="cancel" value="<spring:message code="term.condition.cancel" />" onclick="javascript: relativeRedir('termCondition/administrator/display.do');" >
		
</form:form>
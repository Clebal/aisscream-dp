<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="rsvp/user/create.do" modelAttribute="rsvpForm">

	<form:hidden path="rendezvousId"/>
	
	<jstl:forEach items="${rsvpForm.getQuestionsAndAnswer()}" var="mapEntry">
		
		<div class="form-group"> 
			<form:label path="questionsAndAnswer[${mapEntry.key}]" >
            	<jstl:out value="mapEntry.key.text"></jstl:out>
            </form:label>
			<form:input class="form-control" path="questionsAndAnswer[${mapEntry.key}]" />
			
			<form:errors class="text-danger" path="questionsAndAnswer[${mapEntry.key}]"/>
		</div>
		<br/>
	</jstl:forEach>
	
	<security:authorize access="hasRole('USER')">
		<input type="submit" class="btn btn-primary" name="save" value="<spring:message code="rsvp.save" />">
	</security:authorize>
	
	<input type="button" class="btn btn-danger" name="cancel" value="<spring:message code="rsvp.cancel" />" onclick="javascript: relativeRedir('rendezvous/list.do');" >

</form:form>
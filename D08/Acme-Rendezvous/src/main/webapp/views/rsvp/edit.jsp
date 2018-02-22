<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="rsvp/user/edit.do" modelAttribute="rsvpForm">

	<form:hidden path="rendezvousId"/>
	
	<jstl:forEach items="${rsvpForm.getQuestions()}" var="mapEntry">
		
		<form:hidden path="questions[${mapEntry.key}]"/>
		
		<div class="form-group"> 
			<form:label path="answers[${mapEntry.key}]" >
            	<jstl:out value="${mapEntry.value}"></jstl:out>
            </form:label>
			<form:input class="form-control" path="answers[${mapEntry.key}]" />
			
			<jstl:if test="${answers.size()!=0}">
				<jstl:forEach var="i" begin="0" end="${answers.size()-1}">
					<jstl:if test="${mapEntry.key==answers.get(i).getQuestion().getId()}">
						<form:errors class="text-danger" path="answers[${i}]"/>
					</jstl:if>
				</jstl:forEach>
			</jstl:if>
		</div>
		<br/>
	</jstl:forEach>
	
	<security:authorize access="hasRole('USER')">
		<input type="submit" class="btn btn-primary" name="save" value="<spring:message code="rsvp.save" />">
	</security:authorize>
	
	<input type="button" class="btn btn-danger" name="cancel" value="<spring:message code="rsvp.cancel" />" onclick="javascript: relativeRedir('rendezvous/list.do');" >

</form:form>
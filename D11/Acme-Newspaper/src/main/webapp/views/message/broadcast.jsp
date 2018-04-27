<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="message/administrator/edit.do" modelAttribute="notification">

	<form:hidden path="id" />
	
	<acme:textbox code="message.moment" path="moment" readonly="true"/>
	
	<acme:select code="message.priority" path="priority" option="HIGH" option2="NEUTRAL" option3="LOW" />
	
	<acme:textbox code="message.subject" path="subject" />

	<acme:textarea code="message.body" path="body" />

	<acme:submit name="save" code="message.save"/>
	
	<input type="button" class="btn btn-danger" name="cancel" value="<spring:message code="notification.cancel" />" onclick="javascript: relativeRedir('message/list.do');" >
		
	<acme:cancel url="/index.do" code="message.cancel" />	
	
</form:form>
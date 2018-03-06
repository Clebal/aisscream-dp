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

<form:form action="request/request.do" modelAttribute="request">

	<acme:select path="service" code="request.services" items="services" itemLabel="name" />
	
	<acme:select path="creditCard" code="request.creditCards" items="creditCards" itemLabel="number" />
	
	<acme:textbox code="request.comments" path="comments"/>
	
	<acme:submit name="save" code="request.save" />
	
	<acme:cancel url="rendezvous/list.do" code="request.cancel"/>

</form:form>
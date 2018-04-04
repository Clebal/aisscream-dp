<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme"	tagdir="/WEB-INF/tags"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container">

	<acme:display code="actor.name" value="${actor.getName()}"/>
	
	<acme:display code="actor.surname" value="${actor.getSurname()}"/>
	
	<acme:display code="actor.email" value="${actor.getEmailAddress()}"/>
	
	<acme:display code="actor.phone" value="${actor.getPhoneNumber()}"/>
	
	<acme:display code="actor.address" value="${actor.getPostalAddress()}"/>
		
</div>

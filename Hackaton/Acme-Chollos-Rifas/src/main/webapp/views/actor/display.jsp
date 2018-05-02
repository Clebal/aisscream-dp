<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme"	tagdir="/WEB-INF/tags"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container">

	<jstl:if test="${model.equals('user')}">
		<img style="margin-top: -108px; margin-right: 50px;" src="${actor.getAvatar()}" alt="Avatar" width="300" height="300"/>
	</jstl:if>
	
	<div style="display: inline-block">
	
		<acme:display code="actor.name" value="${actor.getName()}"/>
		
		<acme:display code="actor.surname" value="${actor.getSurname()}"/>
		
		<acme:display code="actor.email" value="${actor.getEmail()}"/>
		
		<acme:display code="actor.phone" value="${actor.getPhone()}"/>
		
		<acme:display code="actor.address" value="${actor.getAddress()}"/>
		
		<acme:display code="actor.identifier" value="${actor.getIdentifier()}"/>
		
		<jstl:if test="${model.equals('user')}">
			<acme:display code="actor.points" value="${actor.getPoints()}" />
		</jstl:if>
		
		<jstl:if test="${model.equals('company')}">
			<acme:display code="actor.companyname" value="${actor.getCompanyName()}" />
			<acme:display code="actor.type" value="${actor.getType()}" />
		</jstl:if>
		
	</div>
		
</div>

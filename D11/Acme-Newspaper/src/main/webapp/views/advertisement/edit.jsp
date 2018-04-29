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

<form:form action="advertisement/agent/edit.do" modelAttribute="advertisement">

	<form:hidden path="id"/>
	

	<acme:textbox code="advertisement.title" path="title"/>
	
	<acme:textbox code="advertisement.url.banner" path="urlBanner"/>
	
	<acme:textbox code="advertisement.url.target" path="urlTarget"/>
	
	
	<!-- CreditCard -->
	<span class="display"><spring:message code="advertisement.credit.card" /></span> 
	
	<acme:textbox code="advertisement.credit.card.holder.name" path="holderName"/>
	
	<acme:textbox code="advertisement.credit.card.brand.name" path="brandName"/>

	<acme:textbox code="advertisement.credit.card.number" path="creditCard.number"/>
	
	<acme:textbox code="advertisement.credit.card.expiration.month" path="expirationMonth"/>
	
	<acme:textbox code="advertisement.credit.card.expiration.year" path="expirationYear"/>
	
	<acme:textbox code="advertisement.credit.card.cvv.code" path="cvvCode"/>
	
	
	
	<security:authorize access="hasRole('AGENT')">
		<security:authentication var="principal" property="principal.username"/>
		<jstl:if test="${principal.equals(advertisement.getAgent().getUserAccount().getUsername())}">
			<acme:submit name="save" code="advertisement.save" />
		</jstl:if>
	
		
	</security:authorize>
	<security:authorize access="hasRole('AGENT')">
		<security:authentication var="principal" property="principal.username"/>
		<jstl:if test="${principal.equals(advertisement.getAgent().getUserAccount().getUsername()) && advertisement.getId() != 0}">
			<acme:submit name="delete" code="advertisement.delete" codeDelete="advertisement.confirm.delete"/>
		</jstl:if>
	</security:authorize>
	
	<acme:cancel url="advertisement/agent/list.do" code="advertisement.cancel"/>
	
	
	

</form:form>
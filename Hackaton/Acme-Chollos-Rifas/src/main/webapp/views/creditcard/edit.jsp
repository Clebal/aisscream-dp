<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="creditcard/user/edit.do" modelAttribute="creditCard">

	<form:hidden path="id" />
	
	<jstl:if test="${creditCard.getId() == 0}">
		<form:hidden path="user" />
	</jstl:if>
	
	<acme:textbox code="creditcard.holderName" path="holderName"/>
	
	<acme:textbox code="creditcard.brandName" path="brandName"/>

	<acme:textbox code="creditcard.number" path="number"/>
	 
	<acme:textbox code="creditcard.expirationMonth" path="expirationMonth"/>
	 
	<acme:textbox code="creditcard.expirationYear" path="expirationYear"/>
	 
	<acme:textbox code="creditcard.cvvcode" path="cvvcode"/>
	
	<acme:submit name="save" code="creditcard.save"/>

	<jstl:if test="${creditCard.getId() != 0 && isAdded}">
		<acme:submit name="delete" code="creditcard.delete"/>
 	</jstl:if>
	
	<acme:cancel url="creditcard/user/list.do" code="creditcard.cancel"/>
			
</form:form>
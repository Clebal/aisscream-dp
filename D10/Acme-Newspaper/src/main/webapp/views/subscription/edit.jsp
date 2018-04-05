<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="subscription/customer/edit.do" modelAttribute="subscription">

	<form:hidden path="id" />
	<form:hidden path="newspaper"/>
	
	<jstl:if test="${subscription.getId() == 0}">
		<form:hidden path="user" />
	</jstl:if>
	
	<acme:textbox code="subscription.holderName" path="holderName"/>
	
	<acme:textbox code="subscription.brandName" path="brandName"/>

	<acme:textbox code="subscription.number" path="number"/>
	 
	<acme:textbox code="subscription.expirationMonth" path="expirationMonth"/>
	 
	<acme:textbox code="subscription.expirationYear" path="expirationYear"/>
	 
	<acme:textbox code="subscription.cvvcode" path="cvvcode"/>	
	
	<acme:submit name="save" code="subscription.save"/>

	<jstl:if test="${subscription.getId() != 0 && !isAdded}">
		<acme:submit name="delete" code="subscription.delete"/>
 	</jstl:if>
	
	<acme:cancel url="subscription/customer/list.do" code="subscription.cancel"/>
			
</form:form>
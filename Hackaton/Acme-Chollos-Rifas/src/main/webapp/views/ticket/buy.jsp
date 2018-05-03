<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="ticket/user/buy.do" modelAttribute="ticketForm">

 	<form:hidden path="raffle" />
	
	<acme:textbox code="raffle.amount" path="amount" />
	
	<acme:select code="raffle.creditcard" path="creditCard" items="${creditCards}" itemLabel="number" selected="${primaryCreditCard}" />

	<acme:submit name="save" code="raffle.save"/>
	
	<acme:cancel url="raffle/display.do?raffleId=${raffleId}" code="raffle.cancel"/>
			
</form:form>
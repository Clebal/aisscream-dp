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

<form:form action="request/user/request.do" modelAttribute="request">

	<form:hidden path="id" />
	<form:hidden path="rendezvous" />
	<form:hidden path="service" />
	
	<div class="form-group"> 
		<form:label path="creditCard">
			<spring:message code="request.creditCards" />:
		</form:label>
		<form:select id="selectCreditCardId" path="creditCard" class="form-control">
      		<option value="0">-- Select --</option>
      		<jstl:forEach items="${creditcards}" var="cc">
				<option value="${cc.id}" ${cc.id == lastCreditCard ? 'selected' : ''} >${cc.number}</option>
        	</jstl:forEach>
		</form:select>
		<form:errors class="text-danger" path="creditCard" />
	</div>
	
<%-- 	<acme:select path="creditCard" code="request.creditCards" items="creditcards" itemLabel="number" id="id" />
 --%>
	<acme:textbox code="request.comments" path="comments"/>
	
	<acme:submit name="save" code="request.save" />
	
	<acme:cancel url="rendezvous/list.do" code="request.cancel"/>

</form:form>
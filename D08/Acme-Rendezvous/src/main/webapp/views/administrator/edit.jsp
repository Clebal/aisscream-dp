<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme"	tagdir="/WEB-INF/tags"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<form:form action="${requestURI}" modelAttribute="administrator">

	<form:hidden path="id" />

	<!-- Username -->
	<jstl:if test="${administrator.getId()!=0 }">
	<acme:textbox code="actor.username" readonly="readonly" path="userAccount.username"/>
	</jstl:if>
	
	<jstl:if test="${administrator.getId()==0 }">
	<acme:textbox code="actor.username" path="userAccount.username"/>
		
	<!--  Password -->
	<acme:textbox code="actor.password" path="userAccount.password"/>
	</jstl:if>
	
	<!-- Name -->
	<acme:textbox code="actor.name" path="name"/>
	
	<!-- Surname -->
	<acme:textbox code="actor.surname" path="surname"/>
	
	<!-- Address -->
	<acme:textbox code="actor.address" path="address"/>

	<!-- Birthdate -->	
	<acme:textbox code="actor.birthdate" path="birthdate" placeholder="dd/MM/yyyy"/>

	<!-- Email -->
	<acme:textbox code="actor.email" path="email"/>

	<!-- Phone -->
	<acme:textbox code="actor.phone" path="phone"/>
		
	<jstl:if test="${canEdit==true }">	
		<acme:submit name="save" code="actor.save"/>
	</jstl:if>
	
	<acme:cancel code="actor.cancel" url="welcome/index.do"/>
	
</form:form>

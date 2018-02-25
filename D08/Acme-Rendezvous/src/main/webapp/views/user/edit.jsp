<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme"	tagdir="/WEB-INF/tags"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="${requestURI }" modelAttribute="userForm">

<%-- 	<form:hidden path="id" />
	<form:hidden path="version" />
	<jstl:if test="${user.getUserId()!=0 }">
	<form:hidden path="userAccount.password"/>
	</jstl:if>
	<form:hidden path="userAccount.authorities" />
	<form:hidden path="userAccount.version" />
	<form:hidden path="userAccount.enabled" />
	<form:hidden path="userAccount.id" /> --%>
	
	<form:hidden path="id" />

	<!-- Username -->
	<jstl:if test="${userForm.getId()!=0 }">
	<acme:textbox code="actor.username" readonly="readOnly" path="username"/>
	</jstl:if>
	
	<jstl:if test="${userForm.getId()==0 }">
	<acme:textbox code="actor.username" path="username"/>

	<!--  Password -->
	<acme:password code="actor.password" path="password"/>

	<!-- Check password -->
	<acme:password code="actor.checkPassword" path="checkPassword"/>

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
			
	<jstl:if test="${userForm.getId()==0 }">
		<div class="form-check">
			<form:checkbox class="form-check-input" path="check" />
			<form:label path="check"><spring:message code="actor.terminos1" /></form:label> <a href="termCondition/display.do"><spring:message code="actor.terminos2" /></a>
			<form:errors class="text-danger" path="check"/>
		</div>
	</jstl:if>
	
	<br>
		

	<acme:submit name="save" code="actor.save" />

	<acme:cancel code="actor.cancel" url="welcome/index.do"/>
	
</form:form>

<script type="text/javascript">
 
	/*function activar(form) {
    	if (form.check.checked) {
   			 form.save.disabled=false;
 		 } else {     
   			 form.save.disabled=true;
  		 }
 	}*/
	
 </script>
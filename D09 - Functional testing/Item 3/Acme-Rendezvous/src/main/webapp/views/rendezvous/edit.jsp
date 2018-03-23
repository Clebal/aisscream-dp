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


<form:form action="rendezvous/user/edit.do" modelAttribute="rendezvous">

	<form:hidden path="id"/>
	<!--<form:hidden path="version"/>-->
 	<jstl:if test="${rendezvous.getId()==0}">
 	<form:hidden path="creator"/>
 	<form:hidden path="isDeleted"/>
 	<form:hidden path="linkerRendezvouses"/>
 	<jstl:if test="${canPermit==false}">
 	 <form:hidden path="adultOnly"/>
 	</jstl:if>
 	 </jstl:if>
 	
 	
 	
	<acme:textbox path="name" code="rendezvous.name" />
	
	<acme:textarea code="rendezvous.description" path="description"/>
	
	<acme:textbox path="moment" code="rendezvous.moment" placeholder="dd/MM/yyyy HH:mm" />
	
	<acme:textbox code="rendezvous.picture" path="picture"/>
	
	<acme:checkbox code="rendezvous.draft" path="draft"/>
	
	 <jstl:if test="${canPermit==true}">
	 	<acme:checkbox code="rendezvous.adultOnly" path="adultOnly"/>
	</jstl:if>
	
	<acme:textbox path="latitude" code="rendezvous.latitude" />
	
	<acme:textbox path="longitude" code="rendezvous.longitude" />
	
	<acme:submit name="save" code="rendezvous.save" />
	
	<jstl:if test="${rendezvous.getId()!= 0}">
	<acme:submit name="delete" code="rendezvous.delete" codeDelete="rendezvous.confirm.delete"/>
	</jstl:if>
	
	<acme:cancel code="rendezvous.cancel" url="rendezvous/user/list.do"/>
	
</form:form>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table class="table table-striped table-bordered table-hover" name="questions" id="row" requestURI="${requestURI}">
	
	<acme:columnLink action="edit" domain="question" id="${row.getId()}" />
	
	<acme:column property="number" domain="question" />

	<acme:column property="text" domain="question" />

</display:table>

<form:form action="answer/user/create.do" modelAttribute="answer">

	<form:hidden path="id"/>
	<jstl:if test="${answer.getId() == 0}">
 		<form:hidden path="question"/>
 	</jstl:if>
 	
	<acme:textbox path="text" code="answer.text" />

	<acme:submit name="save" code="answer.save" />
	
</form:form>

<acme:paginate url="${requestURI}" objects="${questions}" pageNumber="${pageNumber}" page="${page}" />
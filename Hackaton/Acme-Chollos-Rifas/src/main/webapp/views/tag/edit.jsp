<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="tag/administrator/edit.do" modelAttribute="tag">

	<form:hidden path="id" />
	
	<acme:textbox code="tag.name" path="name"/>
	
	<acme:submit name="save" code="tag.save" />
		
	<jstl:if test="${tag.getId() != 0}">
	<acme:submit name="delete" code="tag.delete" codeDelete="tag.confirm.delete"/> 	</jstl:if>
	
	<acme:cancel url="tag/list.do" code="tag.cancel"/>
	
</form:form>
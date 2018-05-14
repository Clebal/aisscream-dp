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

<form:form action="level/administrator/edit.do" modelAttribute="level">

	<form:hidden path="id"/>
	
	<acme:textbox code="level.name" path="name"/>
	<acme:textbox code="level.image" path="image"/>
	<acme:textbox code="level.minPoints" path="minPoints"/>
	<acme:textbox code="level.maxPoints" path="maxPoints"/>
	
	
	<acme:submit name="save" code="level.save" />
		<jstl:if test="${level.getId() != 0 && canDeleteLevel }">
			<acme:submit name="delete" code="level.delete" />
		</jstl:if>	
	<acme:cancel url="level/list.do" code="level.cancel"/>

</form:form>
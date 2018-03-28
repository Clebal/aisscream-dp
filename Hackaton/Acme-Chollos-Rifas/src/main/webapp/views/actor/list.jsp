<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme"	tagdir="/WEB-INF/tags"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table class="table table-striped table-bordered table-hover" name="users" id="row" requestURI="${requestURI}">
	
	<acme:column property="name" domain="actor"/>
	
	<acme:column property="surname" domain="actor"/>
	
	<acme:column property="birthdate" domain="actor" formatDate="true"/>
	
	<acme:column property="email" domain="actor"/>
	
	<acme:column property="phone" domain="actor"/>
	
	<acme:column property="address" domain="actor"/>
		
	<acme:columnLink domain="actor" action="display" id="${row.getId()}"/>
	
</display:table>

<jstl:if test="${requestURI!='actor/listAttendants.do'}">
<acme:paginate url="${requestURI}" objects="${users}" pageNumber="${pageNumber}" page="${page}" />
</jstl:if>
<jstl:if test="${requestURI=='actor/listAttendants.do'}">
<acme:paginate url="${requestURI}" objects="${users}" pageNumber="${pageNumber}" page="${page}" parameter="rendezvousId" parameterValue="${rendezvousId}"/>
</jstl:if>

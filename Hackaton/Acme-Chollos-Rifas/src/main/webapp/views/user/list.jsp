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
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table class="table table-striped table-bordered table-hover">
	<thead>
		<tr>
			<th><spring:message code="actor.user" /></th>
			<th><spring:message code="actor.name" /></th>
			<th><spring:message code="actor.surname" /></th>
			<th><spring:message code="actor.email" /></th>
			<th><spring:message code="actor.phone" /></th>
			<th><spring:message code="actor.address" /></th>
			<th></th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${users}" var="entry">
		<tr>
			<td style="background-image: url(<jstl:out value="${entry.key.getAvatar()}" />); height: 100px; width: 100px; background-size: cover">
				<img width="50px" height="50px" style="margin: -29px;" src="<jstl:out value="${entry.value.getImage()}" />">
			</td>
			<td><jstl:out value="${entry.key.getName()}"/></td>
			<td><jstl:out value="${entry.key.getSurname()}"/></td>
			<td><jstl:out value="${entry.key.getEmail()}"/></td>
			<td><jstl:out value="${entry.key.getPhone()}"/></td>
			<td><jstl:out value="${entry.key.getAddress()}"/></td>
			<td><a href="actor/user/display.do?userId=${entry.key.getId()}"><spring:message code="actor.display" /></a></td>
		</tr>
	</c:forEach>
	</tbody>
</table>

<acme:paginate pageNumber="${pageNumber}" url="actor/user/list.do" objects="${users}" page="${page}" />
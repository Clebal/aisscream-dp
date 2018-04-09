<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme"	tagdir="/WEB-INF/tags"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container">

	<jstl:if test="${!isFollowing}">
		<a class="btn btn-primary" href="actor/user/follow.do?userId=${user.getId()}"><spring:message code="user.follow" /></a>
		<br><br>
	</jstl:if>

	<acme:display code="actor.name" value="${user.getName()}"/>
	
	<acme:display code="actor.surname" value="${user.getSurname()}"/>
	
	<acme:display code="actor.emailAddress" value="${user.getEmailAddress()}"/>
	
	<acme:display code="actor.phoneNumber" value="${user.getPhoneNumber()}"/>
	
	<acme:display code="actor.postalAddress" value="${user.getPostalAddress()}"/>
	
	<a class="btn btn-primary" href="actor/user/followers.do?userId=${user.getId()}"><spring:message code="user.followers" /></a>
	<a class="btn btn-primary" href="actor/user/followeds.do?userId=${user.getId()}"><spring:message code="user.followeds" /></a>
	
	<h2><spring:message code="user.articles" /></h2>
	<jstl:if test="${articles != null && articles.size() != 0}">
		<jstl:forEach var="a" items="${articles}">
			<a href="article/display.do?articleId=${a.getId()}"><jstl:out value="${a.getTitle()}" /></a>
		</jstl:forEach>
		<br><br>
		<a class="btn btn-primary" href="article/list.do?userId=${actor.getId()}&page=2"><spring:message code="user.readMoreArticles" /></a>
	</jstl:if>
	<jstl:if test="${articles == null || articles.size() == 0}">
		<p><spring:message code="user.noArticles" /></p>
	</jstl:if>
		
</div>

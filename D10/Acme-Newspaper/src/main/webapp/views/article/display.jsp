<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="acme"	tagdir="/WEB-INF/tags"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container">

	<acme:display code="article.moment" value="${article.getMoment()}"/>

	<acme:display code="article.title" value="${article.getTitle()}"/>
	
	<acme:display code="article.summary" value="${article.getSummary()}"/>

	<acme:display code="article.body" value="${article.getBody()}"/>
	
	<spring:message code="article.isTaboo" var="isTaboo"/>
		<jstl:if test="${row.getIsTaboo()}">
		 	<jstl:out value="X"/>
		</jstl:if>
		
	<acme:column property="isFinalMode" domain="article"/>
	<spring:message code="article.isFinalMode" var="isFinalMode"/>
		<jstl:if test="${row.getIsFinalMode()}">
		 	<jstl:out value="X"/>
		</jstl:if>
	
	<spring:url var="urlNewspaper" value="newspaper/display.do">
	<spring:param name="newspaperId" value="${article.getNewspaper().getId()}" />
	</spring:url>
	<p><a href="${urlNewspaper}"> <spring:message code="article.newspaper" /></a></p>
	
	<spring:url var="urlUser" value="user/profile.do">
	<spring:param name="userId" value="${article.getWriter().getId()}" />
	</spring:url>
	<p><a href="${urlUser}"> <spring:message code="article.writer" /></a></p>
	
	<spring:message code="article.pictures" />
	<jstl:forEach items="${pictures}" var="picture">
    	<img src="${picture}" alt="Picture" width="400px" height="200px" style="margin-left:15px;" />
	</jstl:forEach>

</div>

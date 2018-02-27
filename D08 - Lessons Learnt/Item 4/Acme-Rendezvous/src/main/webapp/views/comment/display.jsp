<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div class="container">

	<spring:message code="comment.alt" var="commentAlt"/>
	
	<jstl:if test="${comment.getPicture()!=null && comment.getPicture()!=''}">
		<acme:image value="${comment.getPicture()}" alt="${commentAlt}"/>
	</jstl:if>
	
	<acme:display code="comment.moment" value="${comment.getMoment()}" codeMoment="comment.format.moment"/>
	
	<acme:display code="comment.text" value="${comment.getText()}"/>

	
	<security:authorize access="hasRole('USER')"> 
		<jstl:if test="${canComment}">
			<spring:url var="urlCreateComment" value="comment/user/create.do">
				<spring:param name="repliedCommentId" value="${comment.getId()}" />
				<spring:param name="rendezvousId" value="${comment.getRendezvous().getId()}" />
			</spring:url>
			
			<p><span  style='margin-right:10px;'><a href="${urlCreateComment}" class='btn btn-primary'><spring:message code="comment.create"/></a></span></p>
		</jstl:if>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<acme:displayLink parametre="commentId" code="comment.delete" action="comment/administrator/edit.do" parametreValue="${comment.getId()}" css="btn btn-warning"></acme:displayLink>
	</security:authorize>
	
	<acme:displayLink parametre="rendezvousId" code="comment.rendezvous" action="rendezvous/display.do" parametreValue="${comment.getRendezvous().getId()}" css="btn btn-primary"></acme:displayLink>		
	<acme:displayLink parametre="actorId" code="comment.user" action="actor/display.do" parametreValue="${comment.getUser().getId()}" css="btn btn-primary"></acme:displayLink>		
	
</div>

<jstl:if test="${comments.size()>0}">
	
	<jstl:forEach var="row" items="${comments}">
		
		<div class="container-square2">
		
			<jstl:if test="${row.getPicture()!=null && row.getPicture()!=''}">
				<acme:image value="${row.getPicture()}" alt="${commentAlt}"/>
			</jstl:if>
	
			<acme:display code="comment.moment" value="${row.getMoment()}" codeMoment="comment.format.moment"/>
	
			<acme:display code="comment.text" value="${row.getText()}"/>
			
			<acme:displayLink parametre="commentId" code="comment.more" action="comment/display.do" parametreValue="${row.getId()}" css="btn btn-primary"></acme:displayLink>		
			
			
		</div>
		
	</jstl:forEach>
	
	<acme:paginate url="comment/display.do" objects="${comments}" parameter="commentId" parameterValue="${comment.getId()}" page="${page}" pageNumber="${pageNumber}"/>
	
</jstl:if> 
	
	

	
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


<div class="container">

	<spring:message code="comment.format.moment" var="momentFormat"/>
	<spring:message code="comment.alt" var="commentAlt"/>
	
	<jstl:if test="${row.getPicture()!=null && row.getPicture()!=''}">
		<img src="${comment.getPicture()}" alt="${commentAlt}" width="400px" height="200px" style="margin-left:15px;" />
	</jstl:if>
	
	<p><span class="display"><spring:message code="comment.moment"/></span><fmt:formatDate value="${comment.getMoment()}" pattern="${momentFormat }"/></p>
	
	<p><span class="display"><spring:message code="comment.text"/></span><jstl:out value="  ${comment.getText()}" /></p>
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:url var="urlDeleteComment" value="comment/administrator/edit.do">
			<spring:param name="commentId" value="${comment.getId()}" />
		</spring:url>
		
		<p><span  style='margin-right:10px;'><a href="${urlDeleteComment}" class='btn btn-primary'><spring:message code="comment.delete"/></a></span></p>
	</security:authorize>
	
	<security:authorize access="hasRole('USER')">
		<jstl:if test="${canComment}">
			<spring:url var="urlCreateComment" value="comment/user/create.do">
				<spring:param name="repliedCommentId" value="${comment.getId()}" />
				<spring:param name="rendezvousId" value="${comment.getRendezvous().getId()}" />
			</spring:url>
			
			<p><span  style='margin-right:10px;'><a href="${urlCreateComment}" class='btn btn-primary'><spring:message code="comment.create"/></a></span></p>
		</jstl:if>
	</security:authorize>
	
	<spring:url var="urlRendezvous" value="rendezvous/display.do">
		<spring:param name="rendezvousId" value="${comment.getRendezvous().getId()}" />
	</spring:url>
			
	<span  style='margin-right:10px;'><a href="${urlRendezvous}" class='btn btn-primary'><spring:message code="comment.rendezvous"/></a></span>		

	<spring:url var="urlUser" value="actor/display.do">
		<spring:param name="actorId" value="${comment.getUser().getId()}" />
	</spring:url>
			
	<span  style='margin-right:10px;'><a href="${urlUser}" class='btn btn-primary'><spring:message code="comment.user"/></a></span>	
	
</div>

<jstl:if test="${comments.size()>0}">
	
	<jstl:forEach var="row" items="${comments}">
	
		<spring:url var="urlMoreComments" value="comment/display.do">
			<spring:param name="commentId" value="${row.getId()}" />
			<spring:param name="page" value="1" />
		</spring:url>
		
		<div class="container-square2">
			<jstl:if test="${row.getPicture()!=null && row.getPicture()!=''}">
				<img src="${row.getPicture()}" alt="${commentAlt}" width="400px" height="200px" style="margin-left:15px;" />
			</jstl:if>
	
			<p><span class="display"><spring:message code="comment.moment"/></span><fmt:formatDate value="${row.getMoment()}" pattern="${momentFormat }"/></p>
	
			<p><span class="display"><spring:message code="comment.text"/></span><jstl:out value="  ${row.getText()}" /></p>	
			
			<p><span  style='margin-right:10px;'><a href="${urlMoreComments}" class='btn btn-primary'><spring:message code="comment.more"/></a></span></p>
			
			<security:authorize access="hasRole('ADMIN')">
				<spring:url var="urlDeleteComment" value="comment/administrator/edit.do">
					<spring:param name="commentId" value="${row.getId()}" />
				</spring:url>
				
				<p><span  style='margin-right:10px;'><a href="${urlDeleteComment}" class='btn btn-primary'><spring:message code="comment.delete"/></a></span></p>
			
			</security:authorize>
			
		</div>
		
	</jstl:forEach>
	
	<jstl:forEach var="i" begin="1" end="${pageNumber}">
	
			<spring:url var="urlMorePage" value="comment/display.do">
				<spring:param name="commentId" value="${comment.getId()}" />
				<spring:param name="page" value="${i}" />
			</spring:url>
			
			<jstl:if test="${page==i}">
				<span  style='margin-right:10px;'><a href="${urlMorePage}" class='btn btn-danger'><jstl:out value="${i}"></jstl:out></a></span>
			</jstl:if>
			<jstl:if test="${page!=i}">
				<span  style='margin-right:10px;'><a href="${urlMorePage}" class='btn btn-primary'><jstl:out value="${i}"></jstl:out></a></span>
			</jstl:if>
			
	</jstl:forEach>
	
</jstl:if>
	
	

	
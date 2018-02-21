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
	
	<jstl:if test="${comment.getPicture()!=null}">
		<img src="${comment.getPicture()}" alt="${commentAlt}" width="400px" height="200px" style="margin-left:15px;" />
	</jstl:if>
	
	<p><span class="display"><spring:message code="comment.moment"/></span><fmt:formatDate value="${comment.getMoment()}" pattern="${momentFormat }"/></p>
	
	<p><span class="display"><spring:message code="comment.text"/></span><jstl:out value="  ${comment.getText()}" /></p>	

</div>

<jstl:if test="${comments.size()>0}">
	
	<jstl:forEach var="row" items="${comments}">
	
		<spring:url var="urlMoreComments" value="comment/display.do">
			<spring:param name="commentId" value="${row.getId()}" />
		</spring:url>
		
		<div class="container-square2">
			<jstl:if test="${row.getPicture()!=null}">
				<img src="${row.getPicture()}" alt="${commentAlt}" width="400px" height="200px" style="margin-left:15px;" />
			</jstl:if>
	
			<p><span class="display"><spring:message code="comment.moment"/></span><fmt:formatDate value="${row.getMoment()}" pattern="${momentFormat }"/></p>
	
			<p><span class="display"><spring:message code="comment.text"/></span><jstl:out value="  ${row.getText()}" /></p>	
			
			<p><span class='btn btn-primary' style='margin-right:10px;'><a href="${urlMoreComments}" ><spring:message code="comment.more"/></a></span></p>
		</div>
		
	</jstl:forEach>
	
	<jstl:forEach var="i" begin="0" end="${pageNumber}">
	
			<spring:url var="urlMorePage" value="comment/display.do">
				<spring:param name="commentId" value="${comment.getId()}" />
				<spring:param name="page" value="${i}" />
			</spring:url>
			
			<jstl:if test="${page==i}">
				<span class='btn btn-danger' style='margin-right:10px;'><a href="${urlMorePage}" ><jstl:out value="${i}"></jstl:out></a></span>
			</jstl:if>
			<jstl:if test="${page!=i}">
				<span class='btn btn-primary' style='margin-right:10px;'><a href="${urlMorePage}" ><jstl:out value="${i}"></jstl:out></a></span>
			</jstl:if>
			
	</jstl:forEach>
	
</jstl:if>
	
	

	
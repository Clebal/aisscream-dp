<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<display:table class="table table-striped table-bordered table-hover" name="newspapers" id="row">
	<jsp:useBean id="currentMomentVar" class="java.util.Date"/>
	
<jstl:if test="${requestURI.equals('newspaper/user/list.do')}">
	
	<security:authorize access="hasRole('USER')">
		<display:column>
		<jstl:if test="${row.getIsPublished()==true && row.getPublicationDate()>currentMomentVar}">
			<a href="newspaper/user/publish.do?newspaperId=${row.getId()}"> <spring:message
					code="newspaper.publish" />
			</a>
				</jstl:if>	
		
		</display:column>
		
		<display:column>
		<jstl:if test="${row.getIsPrivate==true}">
			<a href="newspaper/user/putPublic.do?newspaperId=${row.getId()}"> <spring:message
					code="newspaper.public" />
			</a>
		</jstl:if>
		
		<jstl:if test="${row.getIsPrivate==false}">
			<a href="newspaper/user/putPrivate.do?newspaperId=${row.getId()}"> <spring:message
					code="newspaper.private" />
			</a>
		</jstl:if>	
		
		</display:column>
				
	</security:authorize>	
</jstl:if>

	<security:authorize access="hasRole('USER')">
	
	<display:column>
		<jstl:if test="${ row.getIsPublished()==false || row.getPublicationDate()> currentMomentVar}">
			<a href="article/user/create.do?newspaperId=${row.getId()}"> <spring:message
					code="newspaper.create.article" />
			</a>
				</jstl:if>	
		
		</display:column>

	</security:authorize>	
			
	<acme:column property="title" domain="newspaper" />
	
	<acme:column property="description" domain="newspaper" />
	
	<acme:column property="publicationDate" domain="newspaper" formatDate="true" />
	
	<spring:message code="newspaper.isPublished" var="newspaperIsPublished"/>
	<display:column title="${newspaperIsPublished}">
		<jstl:if test="${ row.getIsPublished()==true && row.getPublicationDate()<=currentMomentVar}">
			<jstl:out value="X"/>
		</jstl:if>	
	</display:column>
	
			
	<jstl:set var="canPermit" value="false"/>
	
	
	<security:authorize access="hasRole('USER')">
	<security:authentication var="principal" property="principal.username"/>
	<jstl:if test="${row.getPublisher().getUserAccount().getUsername().equals(principal) || (row.getPublicationDate()<= currentMomentVar && row.getIsPublished()==true)}">
		<jstl:set var="canPermit" value="true"/>
	</jstl:if>
	</security:authorize>
	
	<security:authorize access="hasRole('CUSTOMER')">
	<security:authentication var="principal" property="principal.username"/>
	<jstl:if test="${row.getPublicationDate()<= currentMomentVar && row.getIsPublished()==true}">
		<jstl:set var="canPermit" value="true"/>
	</jstl:if>
	<jstl:if test="${requestURI.equals('newspaper/customer/list.do')}">
		<jstl:set var="canPermit" value="true"/>
	</jstl:if>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<jstl:set var="canPermit" value="true"/>
	</security:authorize>
	
	<security:authorize access="isAnonymous()">
		<jstl:if test="${row.getPublicationDate()<=currentMoment && row.getIsPublished() == true && row.getIsPrivate() == false}">
			<jstl:set var="canPermit" value="true"/>
		</jstl:if>
	</security:authorize>
	
	
			
	<spring:url var="urlDisplay" value="newspaper/display.do">
		<spring:param name="newspaperId" value="${row.getId()}" />
		
	</spring:url>
	
	<display:column>
		<jstl:if test="${canPermit==true}">	
			<a href="${urlDisplay }"> <spring:message code="newspaper.display" /></a>
		</jstl:if>
	</display:column>
		
</display:table>

<jstl:if test="${requestURI.equals('newspaper/user/list.do') || requestURI.equals('newspaper/customer/list.do') || requestURI.equals('newspaper/list.do') || requestURI.equals('newspaper/customer/listForSubscribe.do') || requestURI.equals('newspaper/administrator/findTaboos.do') || requestURI.equals('newspaper/administrator/find10PercentageMoreAvg.do') }">
		<acme:paginate pageNumber="${pageNumber }" url="${requestURI }" objects="${newspapers}" page="${page}"/>
</jstl:if>


<security:authorize access="hasRole('USER')">
<jstl:if test="${requestURI.equals('newspaper/user/list.do')}">
	<div>
	<br>
		<a href="newspaper/user/create.do">
			<spring:message code="newspaper.create" />
		</a>
	</div>
</jstl:if>	
</security:authorize>
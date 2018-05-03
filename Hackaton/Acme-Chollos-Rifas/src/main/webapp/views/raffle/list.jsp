<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table class="table table-striped table-bordered table-hover" name="raffles" id="row" requestURI="${requestURI}">

	<jstl:if test="${requestURI.equals('raffle/moderator/list.do')}">
		<acme:columnLink domain="raffle" action="delete" id="${row.getId()}" actor="moderator" />
	</jstl:if>
	<jstl:if test="${!requestURI.equals('raffle/moderator/list.do')}">
		<acme:columnLink domain="raffle" action="edit" id="${row.getId()}" actor="company" />
	</jstl:if>

	<acme:column domain="raffle" property="title"/>
	
	<acme:column domain="raffle" property="description"/>
	
	<jstl:if test="${row.getProductUrl() != null && !row.getProductUrl().equals('')}">
		<acme:columnLink domain="raffle" code="productName" content="${row.getProductName()}" url="${row.getProductUrl()}"/>
	</jstl:if>
	
	<jstl:if test="${row.getProductUrl() == null || row.getProductUrl().equals('')}">
		<acme:column domain="raffle" property="productName" />
	</jstl:if>

	<acme:column domain="raffle" property="maxDate" formatDate="true" sortable="true"/>
	
	<acme:column domain="raffle" property="price" sortable="true" />
	
	<jstl:if test="${!requestURI.equals('raffle/company/list.do')}">
		<acme:columnLink domain="raffle" code="company" content="${row.getCompany().getName()} ${row.getCompany().getSurname()}" url="actor/company/display.do?actorId=${row.getCompany().getId()}" />
	</jstl:if>
	
	<jstl:if test="${requestURI.equals('raffle/company/list.do')}">
		<acme:columnLink domain="raffle" action="display" id="${row.getId()}" actor="company" />
	</jstl:if>
	
	<jstl:if test="${requestURI.equals('raffle/moderator/list.do')}">
		<acme:columnLink domain="raffle" action="display" id="${row.getId()}" />
	</jstl:if>
	
</display:table>

<acme:paginate pageNumber="${pageNumber}" url="${requestURI}" objects="${raffles}" page="${page}"/>

<br><br>

<a href="raffle/company/create.do" class="btn btn-primary"><spring:message code="raffle.create" /></a>
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

<jstl:if test="${userId != null}">
	<form class="navbar-form navbar-right" action="article/listSearch.do" method="GET">
        <div class="form-group">
          	<input name="userId" value="${userId}" hidden="true"/>
          	<input type="text" name="keyword" class="form-control" placeholder="<spring:message code="article.search" />">
        </div>
        <button type="submit" class="btn btn-default"><spring:message code="master.page.submit"/></button>
     </form>
</jstl:if>
<jstl:if test="${userId == null}">
	<form class="navbar-form navbar-right" action="article/listSearch.do" method="GET">
        <div class="form-group">
          	<input type="text" name="keyword" class="form-control" placeholder="<spring:message code="article.search" />">
        </div>
        <button type="submit" class="btn btn-default"><spring:message code="master.page.submit"/></button>
     </form>
</jstl:if>

<display:table class="table table-striped table-bordered table-hover" name="articles" id="row" requestURI="${requestURI}">

	<jstl:if test="${editar}">
	<acme:columnLink action="edit" domain="article" actor="user" id="${row.getId()}" />
	</jstl:if>
	
	<jstl:if test="${borrar}">
	<acme:columnLink action="delete" domain="article" id="${row.getId()}" actor="administrator"/>
	</jstl:if>
	
	<acme:column property="moment" domain="article"  formatDate="true"/>
	
	<acme:column property="title" domain="article"/>
	
	<acme:column property="summary" domain="article"/>
		
	<acme:column property="body" domain="article"/>
	
	<spring:message code="article.isFinalMode" var="isFinalMode"/>
	<display:column title="${isFinalMode}">
		<jstl:if test="${row.getIsFinalMode()}">
		 	<jstl:out value="X"/>
		</jstl:if>
	</display:column>
		
	<spring:message code="article.hasTaboo" var="hasTaboo"/>
	<display:column title="${hasTaboo}">
		<jstl:if test="${row.getHasTaboo()}">
		 	<jstl:out value="X"/>
		</jstl:if>
	</display:column>
		
	<acme:columnLink domain="article" action="display" id="${row.getId()}"/>
	
</display:table>

<acme:paginate url="${requestURI}" objects="${articles}" pageNumber="${pageNumber}" page="${page}"/>


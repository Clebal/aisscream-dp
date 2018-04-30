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
<jstl:if test="${userId == null && taboo == null}">
	<form class="navbar-form navbar-right" action="article/listSearch.do" method="GET">
        <div class="form-group">
          	<input type="text" name="keyword" class="form-control" placeholder="<spring:message code="article.search" />">
        </div>
        <button type="submit" class="btn btn-default"><spring:message code="master.page.submit"/></button>
     </form>
</jstl:if>
<jstl:if test="${userId == null && taboo == false}">
	<form class="navbar-form navbar-right" action="article/administrator/listSearch.do" method="GET">
        <div class="form-group">
          	<input type="text" name="keyword" class="form-control" placeholder="<spring:message code="article.search" />">
        </div>
        <button type="submit" class="btn btn-default"><spring:message code="master.page.submit"/></button>
     </form>
</jstl:if>
<jstl:if test="${userId == null && taboo == true}">
	<form class="navbar-form navbar-right" action="article/administrator/listSearchTaboo.do" method="GET">
        <div class="form-group">
          	<input type="text" name="keyword" class="form-control" placeholder="<spring:message code="article.search" />">
        </div>
        <button type="submit" class="btn btn-default"><spring:message code="master.page.submit"/></button>
     </form>
</jstl:if>

<display:table class="table table-striped table-bordered table-hover" name="articles" id="row" requestURI="${requestURI}">


	<display:column title="">
	<jstl:if test="${editar && !row.getIsFinalMode()}">
		<spring:url var="urlEdit" value="article/user/edit.do">
		<spring:param name="articleId" value="${row.getId()}" />
		</spring:url>
		<p><a href="${urlEdit}"> <spring:message code="article.edit" /></a></p>		
	</jstl:if>
	</display:column>
	
	<jstl:if test="${borrar && taboo == false}">
	<acme:columnLink action="deleteLis" domain="article" id="${row.getId()}" actor="administrator"/>
	</jstl:if>
	<jstl:if test="${borrar && taboo == true}">
	<acme:columnLink action="deleteTab" domain="article" id="${row.getId()}" actor="administrator"/>
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


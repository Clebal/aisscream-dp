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


<display:table class="table table-striped table-bordered table-hover" name="rendezvouses" id="row"
	requestURI="${requestURI}" pagesize="5"
	>
	
	<security:authorize access="hasRole('USER')">
		<security:authentication var="principal" property="principal.username"/>

		<display:column>
		<jstl:if test="${row.getCreator().getUserAccount().getUsername().equals(principal)}">
				<jstl:if test="${row.getDraft()==true && row.getIsDeleted()==false}">
			<a href="rendezvous/user/edit.do?rendezvousId=${row.getId()}"> <spring:message
					code="rendezvous.edit" />
			</a>
				</jstl:if>	
		
				</jstl:if>
		</display:column>
				
		
		
		<!-- Para que aparezcan los enlaces de addlink cuando accedamos desde el diplay para enlazar -->
		<jstl:if test="${canLink && !myRendezvousIsDeleted && row.getIsDeleted()==false}">
		<display:column>
		<spring:url var="urlLinkRendezvous" value="rendezvous/user/linkRendezvous.do">
						<spring:param name="myRendezvousId" value="${rendezvousId}" />
						<spring:param name="linkedRendezvousId" value="${row.getId()}" />
					</spring:url>
		<a href="${urlLinkRendezvous }"> <spring:message
					code="rendezvous.link" />
			</a>
		</display:column>
		</jstl:if>
		
		<!-- Para que aparezcan los enlaces de removelink cuando accedamos desde el diplay para desenlazar -->
		<jstl:if test="${canUnLink && !myRendezvousIsDeleted && row.getIsDeleted()==false}">
		<display:column>
		<spring:url var="urlUnLinkRendezvous" value="rendezvous/user/unLinkRendezvous.do">
						<spring:param name="myRendezvousId" value="${rendezvousId}" />
						<spring:param name="linkedRendezvousId" value="${row.getId()}" />
					</spring:url>
		<a href="${urlUnLinkRendezvous }"> <spring:message
					code="rendezvous.unLink" />
			</a>
		</display:column>
		</jstl:if>
		
	</security:authorize>
	
	<spring:message code="rendezvous.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}"></display:column>

	<spring:message code="rendezvous.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"></display:column>

	<spring:message code="rendezvous.format.moment" var="momentFormat"/>
	<spring:message code="rendezvous.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}" format="{0,date,${momentFormat}}"></display:column>

	<spring:message code="rendezvous.draft" var="draftHeader" />
	<display:column property="draft" title="${draftHeader}"></display:column>
		
	<spring:message code="rendezvous.adultOnly" var="adultOnlyHeader" />
	<display:column property="adultOnly" title="${adultOnlyHeader}"/>
	
		
	<spring:message code="rendezvous.longitude" var="longitudeHeader" />
	<display:column property="longitude" title="${longitudeHeader}"/>
	
	
	<spring:message code="rendezvous.latitude" var="latitudeHeader" />
	<display:column property="latitude" title="${latitudeHeader}"/>

	
	<spring:message code="rendezvous.isDeleted" var="isDeletedHeader" />
	<display:column title="${isDeletedHeader}">
	<jstl:if test="${row.getIsDeleted()==true }">
	<jstl:out value="X"/>
	</jstl:if>
	</display:column>

	<spring:url var="urlDisplay" value="rendezvous/display.do">
		<spring:param name="rendezvousId" value="${row.getId()}" />
	</spring:url>
	
	<display:column>
		<a href="${urlDisplay }"> <spring:message code="rendezvous.display" /></a>
	</display:column>

</display:table>

<br>
<security:authorize access="hasRole('USER')">
	<div>
		<a href="rendezvous/user/create.do">
			<spring:message code="rendezvous.create" />
		</a>
	</div>
</security:authorize>

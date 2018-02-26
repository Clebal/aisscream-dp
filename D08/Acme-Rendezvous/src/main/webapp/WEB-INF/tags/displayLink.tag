<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Atributes -->
<%@ attribute name="action" required="true" %>
<%@ attribute name="parametre" required="true" %> 
<%@ attribute name="parametreValue" required="true" type="java.lang.Object" %>   
<%@ attribute name="code" required="true" %>
<%@ attribute name="css" required="false" %>  

<%-- Definition --%>


		<spring:url var="urlDisplayLink" value="${action}">
			<spring:param name="${parametre }" value="${parametreValue}" />
		</spring:url>

		<p>
			<a href="${urlDisplayLink}" class="${css}"><spring:message code="${code }"/></a>
		</p>
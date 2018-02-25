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
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div>
	
	<br/>
	<img src="http://acme-world.com/wp-content/themes/immersivegarden/images/logo-acme-grey.png" alt="<spring:message code="about.us.alt" />" width="400px" height="200px" style="text-align: center; margin:auto;" />
	
	<br/>
	<br/>
	<p><span class="display"><spring:message code="about.us.name"/></span>: <spring:message code="about.us.name.value"/></p>
	<p><span class="display"><spring:message code="about.us.vat.number"/></span>: <spring:message code="about.us.vat.number.value"/></p>
	
	

</div>
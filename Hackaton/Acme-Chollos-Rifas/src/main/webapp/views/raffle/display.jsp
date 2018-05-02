<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div class="container">
	
	<h3><jstl:out value="${raffle.getTitle()}" /></h3>
	
	<p><jstl:out value="${raffle.getDescription()}" /></p>
	
	<p style="font-size: 25px; text-align:center"><spring:message code="welcome.currency.english" /><fmt:formatNumber value="${raffle.getPrice()}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/><spring:message code="welcome.currency.spanish" /></p>
	
	<div style="display:flex">
	<c:forEach items="${raffle.getProductImages()}" var="image">
		<div style="display: inline-block; background-position: center; width:calc(100%/${raffle.getProductImages().size()}); height: 300px; background-image: url(${image}); background-size: cover;"></div>
	</c:forEach>
	</div>
	
	<security:authorize access="hasRole('USER')">
		<br>
		<div style="text-align: center">
			<a class="btn btn-primary" href="raffle/user/buy.do?raffleId=${raffle.getId()}"><spring:message code="raffle.buy.creditcard" /></a>
			<jstl:if test="${raffle.getPrice() != 0.0}">
				<a class="btn btn-primary" href="#" data-toggle="modal" data-target="#myModal"><spring:message code="raffle.buy.paypal" /></a>
			</jstl:if>
			<jstl:if test="${raffle.getPrice() == 0.0}">
				<a class="btn btn-primary" href="raffle/user/buy.do?raffleId=${raffle.getId()}&method=PAYPAL"><spring:message code="raffle.buy.creditcard" /></a>
			</jstl:if>
		</div>
		
		<jstl:if test="${raffle.getPrice() != 0.0}">
			<!-- Modal -->
			<div class="modal fade" id="myModal" tabindex="-1">
			  <div class="modal-dialog">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
			        <h4 class="modal-title" id="myModalLabel"><spring:message code="welcome.amountTicketToBuy" /></h4>
			      </div>
		          <form action="raffle/user/buy.do" method="GET">
				      <div class="modal-body">
				        	<input type="hidden" name="raffleId" value="${raffle.getId()}" />
				        	<input type="hidden" name="method" value="PAYPAL" />
				        	<div class="from-group">
				        		<input class="form-control" type="number" name="amount" />
				        	</div>
				      </div>
				      <div class="modal-footer">
				        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="welcome.cancel" /></button>
				        <button type="submit" class="btn btn-primary"><spring:message code="welcome.buy" /></button>
				      </div>
		          </form>
			    </div>
			  </div>
			</div>
		</jstl:if>
	</security:authorize>
	
</div>
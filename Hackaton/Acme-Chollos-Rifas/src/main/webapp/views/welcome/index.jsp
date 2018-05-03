<%--
 * index.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="raffle">
	<h3><spring:message code="welcome.raffle" /></h3>
	<div class="row">
		
			<jstl:forEach var="item" items="${raffles}">
				<div class="thumbnail col-sm-6 col-md-4" style="padding: 0px;">
					<img src="${item.getProductImages().toArray()[0]}" alt="..." style="padding: 0px; margin: 0px; width: 100%; height: 300px!important;">
					<div class="caption">
					
						<h4>${item.getTitle()}</h4>
						<%-- <p>${item.getDescription()}</p> --%>	
						
						<jstl:if test="${item.getPrice() != 0.0}">				
	 						<p style="text-align: center; font-size: 20px; font-weight: bold;"><spring:message code="welcome.currency.english" /><fmt:formatNumber value="${item.getPrice()}" currencySymbol="" type="number" minFractionDigits="2" maxFractionDigits="2"/><spring:message code="welcome.currency.spanish" /></p>
						</jstl:if>
						
						<jstl:if test="${item.getPrice() == 0.0}">				
	 						<p style="text-align: center; font-size: 20px; font-weight: bold;"><spring:message code="raffle.free" /></p>
						</jstl:if>
						
						<small><spring:message code="welcome.offeredBy" />: <a href="actor/company/display.do?actorId=${item.getCompany().getId()}">${item.getCompany().getCompanyName()}</a></small><br><br>
						
						<security:authorize access="hasRole('USER')">
							<a href="#" class="dropdown-toggle btn btn-primary" data-toggle="dropdown"><spring:message code="welcome.buy" /> <span class="caret"></span></a>
			        		<ul class="dropdown-menu" style="top: inherit; left: inherit;">
								<li><a href="ticket/user/buy.do?raffleId=${item.getId()}&method=CREDITCARD"><spring:message code="welcome.creditcard" /></a></li>
								<jstl:if test="${item.getPrice() != 0.0}">
									<li><a href="#" data-toggle="modal" data-target="#myModal">PayPal</a></li>
								</jstl:if>
								<jstl:if test="${item.getPrice() == 0.0}">
									<li><a href="ticket/user/buy.do?raffleId=${item.getId()}&method=PAYPAL">PayPal</a></li>
								</jstl:if>		
							</ul>
							
							<jstl:if test="${item.getPrice() != 0.0}">
								<!-- Modal -->
								<div class="modal fade" id="myModal" tabindex="-1">
								  <div class="modal-dialog">
								    <div class="modal-content">
								      <div class="modal-header">
								        <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
								        <h4 class="modal-title" id="myModalLabel"><spring:message code="welcome.amountTicketToBuy" /></h4>
								      </div>
							          <form action="ticket/user/buy.do" method="GET">
									      <div class="modal-body">
									        	<input type="hidden" name="raffleId" value="${item.getId()}" />
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
						<a href="raffle/display.do?raffleId=${item.getId()}" class="btn btn-default"><spring:message code="welcome.details" /></a>
						
					</div>
				</div>
			</jstl:forEach>
	</div>
	<a href="raffle/list.do" class="btn btn-primary" style="width: 100%; padding: 10px;"><spring:message code="welcome.moreRaffles" /></a>
</div>

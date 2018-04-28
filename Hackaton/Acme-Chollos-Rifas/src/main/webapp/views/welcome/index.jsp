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

<h2><spring:message code="welcome.raffle" /></h2>
<div class="row">
	<div class="col-sm-6 col-md-4">
	
		<jstl:forEach var="item" items="${raffles}">
			<div class="thumbnail" style="padding: 0px;">
				<img width="300" height="300" src="${item.getProductImages().toArray()[0]}" alt="..." style="padding: 0px; margin: 0px; width: 100%;">
				<div class="caption">
					<h3>${item.getTitle()}</h3>
					<p>${item.getDescription()}</p>
					<security:authorize access="hasRole('USER')">
						<a href="#" class="dropdown-toggle btn btn-primary" data-toggle="dropdown"><spring:message code="welcome.buy" /> <span class="caret"></span></a>
		        		<ul class="dropdown-menu" style="top: inherit; left: inherit;">
							<li><a href="raffle/user/buy.do?raffleId=${item.getId()}&method=CREDITCARD"><spring:message code="welcome.creditcard" /></a></li>
							<li><a href="#" data-toggle="modal" data-target="#myModal">PayPal</a></li>		
						</ul>
						
						<!-- Modal -->
						<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
						  <div class="modal-dialog" role="document">
						    <div class="modal-content">
						      <div class="modal-header">
						        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						        <h4 class="modal-title" id="myModalLabel">Número de tickets a comprar</h4>
						      </div>
					          <form action="raffle/user/buy.do" method="GET">
							      <div class="modal-body">
							        	<input type="hidden" name="raffleId" value="${item.getId()}" />
							        	<input type="hidden" name="method" value="PAYPAL" />
							        	<div class="from-group">
							        		<input class="form-control" type="number" name="amount" />
							        	</div>
							      </div>
							      <div class="modal-footer">
							        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
							        <button type="submit" class="btn btn-primary">¡Comprar!</button>
							      </div>
					          </form>
						    </div>
						  </div>
						</div>
						
						<script>
/* 							$('#myModal').modal();
 */						</script>
					</security:authorize>
					<a href="raffle/display.do?rifaId=${item.getId()}" class="btn btn-default"><spring:message code="welcome.details" /></a>
				</div>
			</div>
		</jstl:forEach>

	</div>
</div>
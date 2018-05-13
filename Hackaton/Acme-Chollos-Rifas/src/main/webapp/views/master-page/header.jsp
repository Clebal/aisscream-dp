<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<nav class="navbar navbar-default">
  <div class="container-fluid">

    <div class="navbar-header">
 		<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
	  <a style="background-image: url(${banner}); -ms-behavior: url(styles/backgroundsize.min.htc);" class="navbar-brand" href="#"><span>${nameHeader}</span></a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">    
      <ul class="nav navbar-nav">
      	
      	<li><a href="welcome/index.do"><spring:message code="master.page.home" /></a></li>
      	
      	<security:authorize access="isAnonymous()">
      		<li><a href="plan/display.do"><spring:message code="master.page.all.plans" /></a></li>
  
      	</security:authorize>
               
        <li class="dropdown">
        
        	<security:authorize access="hasRole('ADMIN')">
        		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.administrator" /> <span class="caret"></span></a>
        		<ul class="dropdown-menu">
					<li><a href="dashboard/administrator/display.do"><spring:message code="master.page.admin.dashboard" /></a></li>
					<li><a href="termCondition/administrator/display.do"><spring:message code="master.page.admin.termCondition" /></a></li>		
					<li><a href="configuration/administrator/display.do"><spring:message code="master.page.admin.configuration" /></a></li>
					<li><a href="tag/administrator/list.do"><spring:message code="master.page.admin.tag" /></a></li>
					<li><a href="plan/display.do"><spring:message code="master.page.all.plans" /></a></li>							
				</ul>
			</security:authorize>
			
			<security:authorize access="hasRole('USER')">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="master.page.user" /> <span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a href="actor/list.do"><spring:message code="master.page.list.user" /></a></li>
					<li><a href="raffle/user/list.do"><spring:message code="master.page.user.raffle" /></a></li>
					<li><a href="creditcard/user/list.do"><spring:message code="master.page.user.creditCard" /></a></li>
					<li><a href="evaluation/user/list.do"><spring:message code="master.page.user.evaluation" /></a></li>
					<li><a href="comment/user/list.do"><spring:message code="master.page.user.comment" /></a></li>
					<li><a href="plan/display.do"><spring:message code="master.page.all.plans" /></a></li>
					<li><a href="subscription/user/display.do"><spring:message code="master.page.user.mySubscription" /></a></li>																													
				</ul>
			</security:authorize>
			
			<security:authorize access="hasRole('MANAGER')">
        		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.manager" /> <span class="caret"></span></a>
        		<ul class="dropdown-menu">
        		<li><a href="plan/display.do"><spring:message code="master.page.all.plans" /></a></li>												
				</ul>
			</security:authorize>
			
			<security:authorize access="hasRole('COMPANY')">
        		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.company" /> <span class="caret"></span></a>
        		<ul class="dropdown-menu">
        		<li><a href="tag/company/list.do"><spring:message code="master.page.company.tag" /></a></li>
        		<li><a href="plan/display.do"><spring:message code="master.page.all.plans" /></a></li>												       	
				</ul>
			</security:authorize>
			
        </li>
        
        <li class="dropdown">
	        <security:authorize access="isAuthenticated()">		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="master.page.profile" /> <span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a href="raffle/list.do"><spring:message code="master.page.list.raffle" /></a></li>
					<security:authorize access="hasRole('USER')">
						<li><a href="actor/user/profile.do"><spring:message code="master.page.profile" /></a></li>
						<li><a href="actor/user/edit.do"><spring:message code="master.page.profile.edit" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('SPONSOR')">
						<li><a href="actor/sponsor/profile.do"><spring:message code="master.page.profile" /></a></li>
						<li><a href="actor/sponsor/edit.do"><spring:message code="master.page.profile.edit" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('MODERATOR')">
						<li><a href="actor/moderator/profile.do"><spring:message code="master.page.profile" /></a></li>
						<li><a href="actor/moderator/edit.do"><spring:message code="master.page.profile.edit" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="actor/administrator/profile.do"><spring:message code="master.page.profile" /></a></li>
						<li><a href="actor/administrator/edit.do"><spring:message code="master.page.profile.edit" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('COMPANY')">
						<li><a href="actor/company/profile.do"><spring:message code="master.page.profile" /></a></li>
						<li><a href="actor/company/edit.do"><spring:message code="master.page.profile.edit" /></a></li>
					</security:authorize>
				</ul>
			</security:authorize>
        </li>
        
      </ul>
      
      
      <div class="navbar-right navbar-btn btn-group">
      
	      <security:authorize access="isAuthenticated()">
	      	<a href="j_spring_security_logout" class="btn btn-primary"><spring:message code="master.page.logout"/></a>
	      </security:authorize>
	      
	      <security:authorize access="isAnonymous()">
	      	<a href="security/login.do" class="btn btn-primary"><spring:message code="master.page.login"/></a>
		   	<div class="btn-group">
		   	<a href="#" class="dropdown-toggle btn btn-primary" data-toggle="dropdown"><spring:message	code="master.page.register" /> <span class="caret"></span></a>
       		<ul class="dropdown-menu">
				<li><a href="actor/user/create.do"><spring:message code="master.page.create.user" /></a></li>
		   		<li><a href="actor/sponsor/create.do"><spring:message code="master.page.create.sponsor" /></a></li>
		   		<li><a href="actor/company/create.do"><spring:message code="master.page.create.company" /></a></li>
			</ul>
			</div>
		   	<a href="actor/list.do" class="btn btn-primary"><spring:message code="master.page.list.user" /></a>
	      </security:authorize>
      
		  	<div class="btn-group">
		 <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
		   <spring:message code="master.page.language" />
		   <span class="caret"></span>
		 </button>
		 <ul class="dropdown-menu">
		   <li><a href="?language=en"><spring:message code="master.page.language.english" /></a></li>
		   <li><a href="?language=es"><spring:message code="master.page.language.spanish" /></a></li>
		 </ul>
		</div>
      
		  
	  </div>
		
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

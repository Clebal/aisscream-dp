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
	  <a style="background-image: url(http://www.uwyo.edu/reslife-dining/_files/re-design-images/dining-logos/rendezvouslogo_2016.png); -ms-behavior: url(styles/backgroundsize.min.htc);" class="navbar-brand" href="#"><span>Sample Project</span></a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">    
      <ul class="nav navbar-nav">
      	
      	<li><a href="welcome/index.do"><spring:message code="master.page.home" /></a></li>
      	
      	<security:authorize access="isAnonymous()">
			
      	</security:authorize>
               
        <li class="dropdown">
        
        	<security:authorize access="hasRole('ADMIN')">
        		<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.administrator" /> <span class="caret"></span></a>
        		<ul class="dropdown-menu">
					
					<li><a href="termCondition/administrator/display.do"><spring:message code="master.page.admin.termCondition" /></a></li>		
					
				</ul>
			</security:authorize>
	
			
			
			
        </li>
        
        <li class="dropdown">
	        <security:authorize access="isAuthenticated()">		
				<a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="master.page.profile" /> <span class="caret"></span></a>
				<ul class="dropdown-menu">
					
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

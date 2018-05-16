<%--
 * textbox.tag
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 
 
<%@ attribute name="codeQuestion" required="true" %>
<%@ attribute name="codeAnswer" required="true" %>

<%-- Definition --%>

<style>
label.answer {
    margin-left: 50px;
    margin-top: 15px;
    margin-bottom: 25px;
}
</style>

<spring:message code="${codeQuestion}" var="questionText"/>
<spring:message code="${codeAnswer}" var="answerText"/>
<spring:message code="survey.addAnswer" var="addAnswer" />
<spring:message code="survey.removeAnswer" var="removeAnswer" />
<spring:message code="survey.removeQuestion" var="removeQuestion" />

<div id="questionsContainer" data-number="0" data-number2="1">

</div>

<button class="btn btn-primary" id="addQuestion"><spring:message code="survey.addQuestion" /></button><br><br>

<script>

var questionText = "${questionText}";
var answerText = "${answerText}";
var addAnswer = "${addAnswer}";
var removeAnswer ="${removeAnswer}";
var removeQuestion ="${removeQuestion}";

$(document).ready(function() {
	var questionsContainer = $("#questionsContainer");
	var number = questionsContainer.attr("data-number");
	var number2 = questionsContainer.attr("data-number2");
	$(questionsContainer).append('<br><div><label>'+questionText+ ' ' + number2 +'&nbsp;&nbsp;</label><input type="text" name="questions['+number+'].text">&nbsp;&nbsp;<a class="addAnswer" data-number="1" data-number2="2" data-container="1">'+addAnswer+'</a> - <a class="removeAnswer">'+removeAnswer+'</a><br><div class="answersContainer'+number2+'" style="display: inline; margin-right: 5px"><div  style="display: inline; margin-right: 5px"><label class="answer">'+answerText + ' 1&nbsp;&nbsp;</label><input type="text" name="questions['+number+'].answers[0].text" /></div></div></div>');
});

$("#addQuestion").click(function(e) {
	e.preventDefault();
	var questionsContainer = $("#questionsContainer");
	var number = questionsContainer.attr("data-number");
	var number2 = questionsContainer.attr("data-number2");
	$("#questionsContainer").attr("data-number", ++number);
	$("#questionsContainer").attr("data-number2", ++number2);
	$(questionsContainer).append('<br><div><label>'+questionText+ ' ' + number2 +'&nbsp;&nbsp;</label><input type="text" name="questions['+number+'].text">&nbsp;&nbsp;<a class="addAnswer" data-number="1" data-number2="2" data-container="'+number2+'">'+addAnswer+'</a> - <a class="removeQuestion">'+removeQuestion+'</a> - <a class="removeAnswer">'+removeAnswer+'</a><br><div class="answersContainer'+number2+'" style="display: inline; margin-right: 5px"><div  style="display: inline; margin-right: 5px"><label class="answer">'+answerText + ' 1&nbsp;&nbsp;</label><input type="text" name="questions['+number+'].answers[0].text" /></div></div></div>');
});

$("body").on("click", ".addAnswer", function(e){
	e.preventDefault();
	var numberAnswer = $(e.currentTarget).attr("data-number");
	var numberAnswer2 = $(e.currentTarget).attr("data-number2");
	var numberQuestion = $("#questionsContainer").attr("data-number2");
	var numberContainer = $(e.currentTarget).attr("data-container");
	$(".answersContainer"+numberContainer).append('<div  style="display: inline; margin-right: 5px"><br><label class="answer">'+answerText + ' ' + numberAnswer2 +'&nbsp;&nbsp;</label><input type="text" name="questions['+--numberQuestion+'].answers['+ numberAnswer +'].text" /></div>');
	$(e.currentTarget).attr("data-number", ++numberAnswer);
	$(e.currentTarget).attr("data-number2", ++numberAnswer2);
});

$("body").on("click", ".removeAnswer", function(e){
	e.preventDefault();
	if($(e.currentTarget.parentNode).children("div").children().length > 1) {
		var number = $($(e.currentTarget.parentNode).children()[2]).attr("data-number");
		var number2 = $($(e.currentTarget.parentNode).children()[2]).attr("data-number2");
		$($(e.currentTarget.parentNode).children()[2]).attr("data-number", --number);
		$($(e.currentTarget.parentNode).children()[2]).attr("data-number2", --number2);
		$(e.currentTarget.parentNode).children("div").children(":last-child").remove();
	}
});

$("body").on("click", ".removeQuestion", function(e){
	e.preventDefault();
	var number = $(e.currentTarget.parentNode.parentNode).attr("data-number");
	var number2 = $(e.currentTarget.parentNode.parentNode).attr("data-number2");
	$(e.currentTarget.parentNode.parentNode).attr("data-number", --number);
	$(e.currentTarget.parentNode.parentNode).attr("data-number2", --number2);
	$(e.currentTarget.parentNode).remove();
});

</script>
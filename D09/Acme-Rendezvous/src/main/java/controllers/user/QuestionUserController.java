package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.QuestionService;
import services.RendezvousService;

import controllers.AbstractController;
import domain.Question;
import domain.Rendezvous;

@Controller
@RequestMapping("/question/user")
public class QuestionUserController extends AbstractController {

	// Services
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private RendezvousService rendezvousService;
	
	// Constructor
	public QuestionUserController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Integer page) {
		ModelAndView result;
		Collection<Question> questions;
		Integer size;

		size = 5;
		if (page == null) page = 1;
				
		questions = this.questionService.findByCreatorUserAccountId(LoginService.getPrincipal().getId(), page, size);
		Assert.notNull(questions);
		
		result = super.paginateModelAndView("question/list", this.questionService.countByCreatorUserAccountId(LoginService.getPrincipal().getId()), page, size);
		result.addObject("requestURI", "question/user/list.do");
		result.addObject("questions", questions);
		
		return result;
	}
	
	// Create
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int rendezvousId) {
		ModelAndView result;
		Question question;
		Rendezvous rendezvous;
		
		rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);
		
		question = this.questionService.create(rendezvous);
		Assert.notNull(question);

		result = this.createEditModelAndView(question);
		
		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int questionId) {
		ModelAndView result;
		Question question;
				
		question = this.questionService.findOneToEdit(questionId);
		Assert.notNull(question);
		
		result = this.createEditModelAndView(question);
		
		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Question question, BindingResult binding) {
		ModelAndView result;
		
		question = this.questionService.reconstruct(question, binding);
		
		if(binding.hasErrors()){
			result = this.createEditModelAndView(question);
		}else{
			try {
				this.questionService.save(question);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(question, "question.commit.error");
			}
		}
		
		return result;
	}
	
	// Delete
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Question question, BindingResult binding) {
		ModelAndView result;
		
		question = this.questionService.reconstruct(question, binding);
		
		try {
			this.questionService.delete(question);
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(question, "question.commit.error");
		}
		
		return result;
	}
	
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Question question) {
		ModelAndView result;

		result = this.createEditModelAndView(question, null);

		return result;
	}
	
	protected ModelAndView createEditModelAndView(final Question question, final String messageCode) {
		ModelAndView result;
		
		result = new ModelAndView("question/edit");
		
		result.addObject("question", question);
		result.addObject("message", messageCode);

		return result;
	}
	
}

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
import services.CompanyService;
import services.EvaluationService;
import services.UserService;

import controllers.AbstractController;
import domain.Company;
import domain.Evaluation;
import domain.User;

@Controller
@RequestMapping("/evaluation/user")
public class EvaluationUserController extends AbstractController {

	// Services
	
	@Autowired
	private EvaluationService evaluationService;
	
	@Autowired
	private CompanyService	companyService;
	
	@Autowired
	private UserService	userService;
	
	// Constructor
	public EvaluationUserController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Integer page) {
		ModelAndView result;
		Collection<Evaluation> evaluations;
		Integer size;

		size = 5;
		if (page == null) page = 1;
				
		evaluations = this.evaluationService.findByCreatorUserAccountId(LoginService.getPrincipal().getId(), page, size);
		Assert.notNull(evaluations);
		
		result = super.paginateModelAndView("evaluation/list", this.evaluationService.countByCreatorUserAccountId(LoginService.getPrincipal().getId()), page, size);
		result.addObject("requestURI", "evaluation/user/list.do");
		result.addObject("evaluations", evaluations);
		
		return result;
	}
	
	// Create
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int companyId) {
		ModelAndView result;
		Evaluation evaluation;
		Company company;
		User user;
		
		company = this.companyService.findOne(companyId);
		Assert.notNull(company);
		
		user = this.userService.findByUserAccountId(LoginService.getPrincipal().getId());
		
		evaluation = this.evaluationService.create(company, user);
		Assert.notNull(evaluation);

		result = this.createEditModelAndView(evaluation);
		
		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int evaluationId) {
		ModelAndView result;
		Evaluation evaluation;
				
		evaluation = this.evaluationService.findOne(evaluationId);
		Assert.notNull(evaluation);
		
		result = this.createEditModelAndView(evaluation);
		
		return result;
	}
	
	// Edit
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Evaluation evaluation, BindingResult binding) {
		ModelAndView result;
		
		evaluation = this.evaluationService.reconstruct(evaluation, binding);
		
		if(binding.hasErrors()){
			result = this.createEditModelAndView(evaluation);
		}else{
			try {
				this.evaluationService.save(evaluation);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(evaluation, "evaluation.commit.error");
			}
		}
		
		return result;
	}
	
	// Delete
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Evaluation evaluation, BindingResult binding) {
		ModelAndView result;
		
		try {
			this.evaluationService.delete(evaluation);
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(evaluation, "evaluation.commit.error");
		}
		
		return result;
	}
	
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Evaluation evaluation) {
		ModelAndView result;

		result = this.createEditModelAndView(evaluation, null);

		return result;
	}
	
	protected ModelAndView createEditModelAndView(final Evaluation evaluation, final String messageCode) {
		ModelAndView result;
		
		if (evaluation.getId() == 0)
			result = new ModelAndView("evaluation/create");
		else
			result = new ModelAndView("evaluation/edit");
		
		result.addObject("evaluation", evaluation);
		result.addObject("message", messageCode);

		return result;
	}
	
}

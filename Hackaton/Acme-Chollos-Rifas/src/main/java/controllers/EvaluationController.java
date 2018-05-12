package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.EvaluationService;

import controllers.AbstractController;
import domain.Evaluation;

@Controller
@RequestMapping("/evaluation")
public class EvaluationController extends AbstractController {

	// Services
	@Autowired
	private EvaluationService evaluationService;
	
	// Constructor
	public EvaluationController() {
		super();
	}
	
	// List
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Integer page, @RequestParam int companyId) {
		ModelAndView result;
		Collection<Evaluation> evaluations;
		Integer size;

		size = 5;
		if (page == null) page = 1;
				
		evaluations = this.evaluationService.findByCompanyId(companyId, page, size);
		Assert.notNull(evaluations);
		
		result = super.paginateModelAndView("evaluation/list", this.evaluationService.countByCompanyId(companyId), page, size);
		result.addObject("requestURI", "evaluation/list.do?companyId=" + companyId);
		result.addObject("evaluations", evaluations);
		
		return result;
	}
	
	// Display
		@RequestMapping(value="/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam int evaluationId) {
			ModelAndView result;
			Evaluation evaluation;
					
			evaluation = this.evaluationService.findOne(evaluationId);
			Assert.notNull(evaluation);
			
			result = new ModelAndView("evaluation/display");
			result.addObject("requestURI", "evaluation/list.do");
			result.addObject("evaluation", evaluation);
			
			return result;
		}
	
}

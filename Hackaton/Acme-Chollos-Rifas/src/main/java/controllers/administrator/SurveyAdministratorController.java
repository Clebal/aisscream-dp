
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.SurveyService;
import domain.Survey;

@Controller
@RequestMapping("/survey/administrator")
public class SurveyAdministratorController extends AbstractController {

	// Services
	@Autowired
	private SurveyService			surveyService;
	
	// Constructor
	public SurveyAdministratorController() {
		super();
	}
	
	// List
	@RequestMapping(value="/morePopular", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue="1") Integer page) {
		ModelAndView result;
		Page<Survey> surveyPage;
		    
		surveyPage = this.surveyService.surveyMorePopular(page, 5);
		Assert.notNull(surveyPage);
		
		result = new ModelAndView("survey/list");
		result.addObject("surveys", surveyPage.getContent());
		result.addObject("page", page);
		result.addObject("pageNumber", surveyPage.getTotalPages());
		result.addObject("requestURI", "survey/administrator/morePopular.do");
		    
		return result;
	}

}
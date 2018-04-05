
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import controllers.AbstractController;
import domain.Article;

@Controller
@RequestMapping("/article/administrator")
public class ArticleAdministratorController extends AbstractController {

	// Services
	@Autowired
	private ArticleService			articleService;
	
	// Constructor
	public ArticleAdministratorController() {
		super();
	}

	// List taboo
		@RequestMapping(value="/listTaboo", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam(required=false) Integer page) {
			ModelAndView result;
			Page<Article> articles;
			Integer pageAux;
			
			if (page == null)
				pageAux = 1;
			else
				pageAux = page;
			
			articles = this.articleService.findAllTabooPaginated(pageAux, 5);
			Assert.notNull(articles);
			
			result = new ModelAndView("article/list");

			result.addObject("articles", articles);
			result.addObject("pageNumber", articles.getTotalPages());
			result.addObject("page", pageAux);
			result.addObject("requestURI", "article/user/list.do");
			
			return result;
		}
	
}

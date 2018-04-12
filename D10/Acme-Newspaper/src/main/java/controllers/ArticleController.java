
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ArticleService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractController {

	// Services
	@Autowired
	private ArticleService			articleService;
	
	@Autowired
	private UserService					userService;
	
	// Constructor
	public ArticleController() {
		super();
	}
	
	// List
		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam(required=false) Integer userId, @RequestParam(required=false) Integer page) {
			ModelAndView result;
			Page<Article> articles;
			Integer pageAux, userAux;
			boolean editar, borrar;
			
			borrar = false;
			
			if (page == null)
				pageAux = 1;
			else
				pageAux = page;
			
			if (userId == null) {
				editar = true;
				userAux = this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId();
				articles = this.articleService.findByWritterId(userAux, pageAux, 5);
			} else {
				editar = false;
				userAux = userId;
				articles = this.articleService.findAllUserPaginated(userId, pageAux, 5);
			}
			
			Assert.notNull(articles);
			
			result = new ModelAndView("article/list");

			result.addObject("articles", articles.getContent());
			result.addObject("pageNumber", articles.getTotalPages());
			result.addObject("page", pageAux);
			if (userId == null)
				result.addObject("requestURI", "article/list.do");
			else
				result.addObject("requestURI", "article/list.do?userId="+userId);
			result.addObject("editar", editar);
			result.addObject("borrar", borrar);
			result.addObject("userId", userId);
			result.addObject("taboo", null);
			
			return result;
		}
		
		@RequestMapping(value = "/listSearch", method = RequestMethod.GET)
		public ModelAndView listSearch(@RequestParam(required=false) Integer userId, @RequestParam(required = false, defaultValue = "1") final Integer page, @RequestParam(required = false, defaultValue = "") final String keyword) {
			ModelAndView result;
			Page<Article> articles;
			Integer userAux;
			boolean editar, borrar;
			
			borrar = false;

			if (userId == null) {
				editar = true;
				userAux = this.userService.findByUserAccountId(LoginService.getPrincipal().getId()).getId();
				articles = this.articleService.findPublishedSearch(userAux, keyword, page, 5);
			} else {
				editar = false;
				userAux = userId;
				articles = this.articleService.findPublicsPublishedSearch(userAux, keyword, page, 5);
			}
			Assert.notNull(articles);

			result = new ModelAndView("article/list");
			result.addObject("pageNumber", articles.getTotalPages());
			result.addObject("page", page);
			result.addObject("articles", articles.getContent());
			if (userId == null)
				result.addObject("requestURI", "article/listSearch.do");
			else
				result.addObject("requestURI", "article/listSearch.do?userId="+userId);
			result.addObject("keyword", keyword);
			result.addObject("editar", editar);
			result.addObject("borrar", borrar);
			result.addObject("userId", userId);
			System.out.println("USUARIO: " + userId);

			return result;
		}
	
		// Display
		@RequestMapping(value = "/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam final int articleId) {
			ModelAndView result;
			Article article;

			article = this.articleService.findOneToDisplay(articleId);
			Assert.notNull(article);

			result = new ModelAndView("article/display");
			result.addObject("article", article);

			return result;
		}
	
}

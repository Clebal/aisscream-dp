
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
		public ModelAndView listTaboo(@RequestParam(required=false) Integer page) {
			ModelAndView result;
			Page<Article> articles;
			Integer pageAux;
			boolean editar, borrar, taboo;
			
			editar = false;
			borrar = true;
			taboo = true;
			
			if (page == null)
				pageAux = 1;
			else
				pageAux = page;
			
			articles = this.articleService.findAllTabooPaginated(pageAux, 5);
			Assert.notNull(articles);
			
			result = new ModelAndView("article/list");

			result.addObject("articles", articles.getContent());
			result.addObject("pageNumber", articles.getTotalPages());
			result.addObject("page", pageAux);
			result.addObject("requestURI", "article/administrator/listTaboo.do");
			result.addObject("editar", editar);
			result.addObject("borrar", borrar);
			result.addObject("taboo", taboo);

			
			return result;
		}
		
		// List
		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam(required=false) Integer page) {
			ModelAndView result;
			Page<Article> articles;
			Integer pageAux;
			boolean editar, borrar, taboo;
			
			editar = false;
			borrar = true;
			taboo = false;
			
			if (page == null)
				pageAux = 1;
			else
				pageAux = 10;
			
			articles = this.articleService.findAllPaginated(pageAux, 5);
			
			Assert.notNull(articles);
			
			result = new ModelAndView("article/list");

			result.addObject("articles", articles.getContent());
			result.addObject("pageNumber", articles.getTotalPages());
			result.addObject("page", pageAux);
			result.addObject("requestURI", "article/administrator/list.do");
			result.addObject("editar", editar);
			result.addObject("borrar", borrar);
			result.addObject("taboo", taboo);
			
			return result;
		}
		
		
		@RequestMapping(value = "/listSearch", method = RequestMethod.GET)
		public ModelAndView listSearch(@RequestParam(required = false, defaultValue = "1") final Integer page, @RequestParam(required = false, defaultValue = "") final String keyword) {
			ModelAndView result;
			Page<Article> articles;
			boolean editar, borrar, taboo;
			
			editar = false;
			borrar = true;
			taboo = false;

			articles = this.articleService.findPublishedSearchNoAuth(keyword, page, 5);
			Assert.notNull(articles);

			result = new ModelAndView("article/list");
			result.addObject("pageNumber", articles.getTotalPages());
			result.addObject("page", page);
			result.addObject("articles", articles.getContent());
			if (keyword == null)
				result.addObject("requestURI", "article/administrator/listSearch.do");
			else
				result.addObject("requestURI", "article/administrator/listSearch.do?keyword="+keyword);
			result.addObject("keyword", keyword);
			result.addObject("editar", editar);
			result.addObject("borrar", borrar);
			result.addObject("taboo", taboo);


			return result;
		}
		
		@RequestMapping(value = "/listSearchTaboo", method = RequestMethod.GET)
		public ModelAndView listSearchTaboo(@RequestParam(required = false, defaultValue = "1") final Integer page, @RequestParam(required = false, defaultValue = "") final String keyword) {
			ModelAndView result;
			Page<Article> articles;
			boolean editar, borrar, taboo;
			
			editar = false;
			borrar = true;
			taboo = true;

			articles = this.articleService.findPublishedSearchTaboo(keyword, page, 5);
			Assert.notNull(articles);

			result = new ModelAndView("article/list");
			result.addObject("pageNumber", articles.getTotalPages());
			result.addObject("page", page);
			result.addObject("articles", articles.getContent());
			if (keyword == null)
				result.addObject("requestURI", "article/administrator/listSearchTaboo.do");
			else
				result.addObject("requestURI", "article/administrator/listSearchTaboo.do?keyword="+keyword);
			result.addObject("keyword", keyword);
			result.addObject("editar", editar);
			result.addObject("borrar", borrar);
			result.addObject("taboo", taboo);

			return result;
		}
	
		
		// Delete
		@RequestMapping(value="/deleteLis", method=RequestMethod.GET)
		public ModelAndView createA(@RequestParam final int articleId) {
			ModelAndView result;
			Article article;
			
			article = this.articleService.findOne(articleId);
			Assert.notNull(article);
			
			this.articleService.deleteFromNewspaper(article);
			
			result = new ModelAndView("redirect:list.do");
			
			return result;
		}
		
		@RequestMapping(value="/deleteTab", method=RequestMethod.GET)
		public ModelAndView createB(@RequestParam final int articleId) {
			ModelAndView result;
			Article article;
			
			article = this.articleService.findOne(articleId);
			Assert.notNull(article);
			
			this.articleService.deleteFromNewspaper(article);
			
			result = new ModelAndView("redirect:listTaboo.do");
			
			return result;
		}
	
}

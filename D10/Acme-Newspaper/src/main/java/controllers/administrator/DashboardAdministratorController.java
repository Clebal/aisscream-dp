
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.UserService;
import controllers.AbstractController;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	//Services
	@Autowired
	private UserService	userService;


	// Constructor
	public DashboardAdministratorController() {
		super();
	}

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer page, @RequestParam(required = false) final Integer size) {
		final ModelAndView result;
		final Double[] newspaperPerUser;
		final Double[] articlesPerWriter;
		final Double[] articlesPerNewspaper;
		final Double ratioUserCreateNewspaper;
		final Double ratioUserWrittenArticle;

		final Double followUpPerArticle;
		final Double followUpPerArticleOneWeek;
		final Double followUpPerArticleTwoWeek;
		final Double[] chirpPerUser;
		final Double ratioUserPostNumberChirps;

		final Double ratioPublicVsPrivateNewspaper;
		final Double numberArticlesPerPrivateNewspaper;
		final Double numberArticlesPerPublicNewspaper;
		final Double ratioSuscribersPrivateVsTotalCustomer;
		final Double averageRatioPrivateVsPublicNewspaperPerPublisher;

		newspaperPerUser = null;
		articlesPerWriter = null;
		articlesPerNewspaper = null;
		ratioUserCreateNewspaper = null;
		ratioUserWrittenArticle = null;

		followUpPerArticle = null;
		followUpPerArticleOneWeek = null;
		followUpPerArticleTwoWeek = null;
		chirpPerUser = null;
		ratioUserPostNumberChirps = null;

		ratioPublicVsPrivateNewspaper = null;
		numberArticlesPerPrivateNewspaper = null;
		numberArticlesPerPublicNewspaper = null;
		ratioSuscribersPrivateVsTotalCustomer = null;
		averageRatioPrivateVsPublicNewspaperPerPublisher = null;
		//select avg(cast((select count(n) from Newspaper n where n.publisher.id = u.id and n.isPrivate = true) as float) / cast((select count(n2) from Newspaper n2 where n2.publisher.id = u.id and n2.isPrivate=false) as float )) from User u;
		//	select (cast((select count(n) from Newspaper n where n.isPrivate=true) as float))/count(n2) from Newspaper n2 where n2.isPrivate=false;
		result = new ModelAndView("dashboard/display");
		//NO select avg(cast((select count(a) from Article a where a.writer.id=u.id) as float)) from User u;

		//select  avg(cast((select count(a) from Article a where a.writer.id=u.id) as float)), sqrt(sum((select count(a) from Article a where a.writer.id=u.id)*(select count(a) from Article a where a.writer.id=u.id))/(select count(u2) from User u2)-avg(cast((select count(a) from Article a where a.writer.id=u.id) as float ))*avg(cast((select count(a) from Article a where a.writer.id=u.id) as float ))) from User u
		//select cast((count(distinct n.publisher)) as float)/(select count(u) from User u) from Newspaper n;

		//select avg(cast((select count(f) from FollowUp f where f.article=a and (DATEDIFF(f.publicationMoment , a.newspaper.publicationDate)<7 or (DATEDIFF(f.publicationMoment , a.newspaper.publicationDate)=7 AND (hour(f.publicationMoment)*60*60+minute(f.publicationMoment )*60+second(f.publicationMoment )<=hour(a.newspaper.publicationDate)*60*60+minute(a.newspaper.publicationDate)*60+second( a.newspaper.publicationDate)) )))as float)) from Article a where a.newspaper.isPublished=true and a.newspaper.publicationDate<CURRENT_DATE
		//select avg(cast((select count(f) from FollowUp f where f.article=a and (DATEDIFF(f.publicationMoment , a.newspaper.publicationDate)<14 or (DATEDIFF(f.publicationMoment , a.newspaper.publicationDate)=14 AND (hour(f.publicationMoment)*60*60+minute(f.publicationMoment )*60+second(f.publicationMoment )<=hour(a.newspaper.publicationDate)*60*60+minute(a.newspaper.publicationDate)*60+second( a.newspaper.publicationDate)) )))as float)) from Article a where a.newspaper.isPublished=true and a.newspaper.publicationDate<CURRENT_DATE
		//select avg(cast((n.articles.size) as float)) from Newspaper n where n.isPrivate;
		result.addObject("newspaperPerUser", newspaperPerUser);
		result.addObject("articlesPerWriter", articlesPerWriter);
		result.addObject("articlesPerNewspaper", articlesPerNewspaper);
		result.addObject("ratioUserCreateNewspaper", ratioUserCreateNewspaper);
		result.addObject("ratioUserWrittenArticle", ratioUserWrittenArticle);

		result.addObject("followUpPerArticle", followUpPerArticle);
		result.addObject("followUpPerArticleOneWeek", followUpPerArticleOneWeek);
		result.addObject("followUpPerArticleTwoWeek", followUpPerArticleTwoWeek);
		result.addObject("chirpPerUser", chirpPerUser);
		result.addObject("ratioUserPostNumberChirps", ratioUserPostNumberChirps);

		result.addObject("ratioPublicVsPrivateNewspaper", ratioPublicVsPrivateNewspaper);
		result.addObject("numberArticlesPerPrivateNewspaper", numberArticlesPerPrivateNewspaper);
		result.addObject("numberArticlesPerPublicNewspaper", numberArticlesPerPublicNewspaper);
		result.addObject("ratioSuscribersPrivateVsTotalCustomer", ratioSuscribersPrivateVsTotalCustomer);
		result.addObject("averageRatioPrivateVsPublicNewspaperPerPublisher", averageRatioPrivateVsPublicNewspaperPerPublisher);

		return result;

	}
}

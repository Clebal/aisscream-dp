
package controllers.administrator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import controllers.AbstractController;


@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	//Services
//	@autowired
//	private userservice			userservice;
//
//	@autowired
//	private announcementservice	announcementservice;
//
//	@autowired
//	private rendezvousservice	rendezvousservice;
//
//	@autowired
//	private questionservice		questionservice;
//
//	@autowired
//	private commentservice		commentservice;
//
//	@autowired
//	private answerservice		answerservice;
//	
//	@autowired
//	private serviceservice serviceservice;
//	
//	@autowired
//	private managerservice managerservice;
//
//	@autowired
//	private categoryservice categoryservice;
//	
//	// constructor
//	public dashboardadministratorcontroller() {
//		super();
//	}
//	
//	//display
//	@requestmapping(value = "/display", method = requestmethod.get)
//	public modelandview display(@requestparam(required=false) integer page, @requestparam(required=false) integer size) {
//		integer pageaux, sizeaux;
//		final modelandview result;
//		double[] rendezvousesperuser;
//		final double ratiouserrendezvousvsno;
//		final double[] usersperrendezvous;
//		final double[] rendezvousesrsvpdperuser;
//		collection<rendezvous> rendezvousestop;
//		final double[] announcementsperrendezvous;
//		final double[] questionsperrendezvous;
//		final double[] answersperrendezvous;
//		final double[] repliespercomment;
//		page<service> bestsellingservices;
//		collection<service> topsellingservices;
//		collection<manager> managermoreservicesaverage, managermoreservicescancelled;
//		final double avgnumbercategoriesperrendezvous;
//		final double avgratioservicescategory;
//		final double[] avgminmaxstandarddesviationservicesperrendezvous;
//
//		if (page == null)
//			pageaux = 1;
//		else
//			pageaux = page;
//		
//		if (size == null)
//			sizeaux = 5;
//		else
//			sizeaux = size;
//
//		rendezvousesperuser = this.rendezvousservice.avgstandarddrsvpdcreatedperuser();
//		ratiouserrendezvousvsno = this.rendezvousservice.ratiocreatorsvstotal();
//		usersperrendezvous = this.userservice.avgstandarddusersperrendezvous();
//		rendezvousesrsvpdperuser = this.rendezvousservice.avgstandarddrendezvousesrsvpdperuser();
//		rendezvousestop = this.rendezvousservice.top10rendezvouses();
//		announcementsperrendezvous = this.announcementservice.avgstandartderivationannouncementperrendezvous();
//		questionsperrendezvous = this.questionservice.avgstandartderivationquestionsperrendezvous();
//		answersperrendezvous = this.answerservice.avgstandardanswerperrendezvous();
//		repliespercomment = this.commentservice.avgstandardrepliespercomment();
//		bestsellingservices = this.serviceservice.bestsellingservices(pageaux, sizeaux);
//		managermoreservicesaverage = this.managerservice.managermoreservicesaverage();
//		managermoreservicescancelled = this.managerservice.managermoreservicescancelled();
//		avgnumbercategoriesperrendezvous = this.categoryservice.avgnumbercategoriesperrendezvous();
//		avgratioservicescategory = this.serviceservice.ratioserviceseachcategory();
//		avgminmaxstandarddesviationservicesperrendezvous = this.serviceservice.avgminmaxstandartderivationservicesperrendezvous();		
//		topsellingservices = this.serviceservice.topbestsellingservices(sizeaux);
//		
//		result = new modelandview("dashboard/display");
//
//		result.addobject("rendezvousesperuser", rendezvousesperuser);
//		result.addobject("ratiouserrendezvousvsno", ratiouserrendezvousvsno);
//		result.addobject("usersperrendezvous", usersperrendezvous);
//		result.addobject("rendezvousesrsvpdperuser", rendezvousesrsvpdperuser);
//		result.addobject("rendezvousestop", rendezvousestop);
//		result.addobject("announcementsperrendezvous", announcementsperrendezvous);
//		result.addobject("questionsperrendezvous", questionsperrendezvous);
//		result.addobject("answersperrendezvous", answersperrendezvous);
//		result.addobject("repliespercomment", repliespercomment);
//		result.addobject("bestsellingservices", bestsellingservices.getcontent());
//		result.addobject("managermoreservicesaverage", managermoreservicesaverage);
//		result.addobject("managermoreservicescancelled", managermoreservicescancelled);
//		result.addobject("avgnumbercategoriesperrendezvous", avgnumbercategoriesperrendezvous);
//		result.addobject("avgratioservicescategory", avgratioservicescategory);
//		result.addobject("avgminmaxstandarddesviationservicesperrendezvous", avgminmaxstandarddesviationservicesperrendezvous);
//		result.addobject("topsellingservices", topsellingservices);
//		result.addobject("page", pageaux);
//		result.addobject("size", sizeaux);
//		result.addobject("pagenumber", bestsellingservices.gettotalpages());
//		
//		return result;
//
//	}
}


package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.LevelService;
import domain.Level;

@Controller
@RequestMapping("/level")
public class LevelController extends AbstractController {

	// Services
	@Autowired
	private LevelService	levelService;


	// Constructor
	public LevelController() {
		super();
	}

	// List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "1") final Integer page) {
		ModelAndView result;
		Page<Level> levels;

		levels = this.levelService.findAllPaginated(page, 5);
		Assert.notNull(levels);

		result = new ModelAndView("level/list");
		result.addObject("pageNumber", levels.getTotalPages());
		result.addObject("page", page);
		result.addObject("levels", levels.getContent());
		result.addObject("requestURI", "level/list.do");

		return result;
	}

	// List
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView list(final int levelId) {
		ModelAndView result;
		Level level;

		level = this.levelService.findOne(levelId);
		Assert.notNull(level);

		result = new ModelAndView("level/display");
		result.addObject("level", level);
		result.addObject("linkBroken", this.checkLinkImage(level));

		return result;
	}

	private boolean checkLinkImage(final Level level) {
		boolean linkBroken;
		URL linkImage;
		HttpURLConnection openCode;
		String[] contentTypes;
		String contentType;

		linkBroken = false;

		if (level.getImage() != null)
			try {
				linkImage = new URL(level.getImage());
				if (linkImage.getProtocol().equals("http"))
					openCode = (HttpURLConnection) linkImage.openConnection();
				else {
					System.setProperty("https.protocols", "TLSv1");
					openCode = (HttpsURLConnection) linkImage.openConnection();
				}
				openCode.setRequestMethod("GET");
				openCode.connect();
				contentTypes = openCode.getContentType().split("/");
				contentType = contentTypes[0];
				if (openCode.getResponseCode() < 200 || openCode.getResponseCode() >= 400 || !contentType.equals("image"))
					linkBroken = true;
			} catch (final SSLException s) {
				linkBroken = false;
			} catch (final IOException e) {
				linkBroken = true;
			}
		return linkBroken;
	}
}

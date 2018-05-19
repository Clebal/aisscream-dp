/*
 * AbstractController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.NotificationService;

@Controller
public class AbstractController {

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private NotificationService		notificationService;

	@ModelAttribute
	public void headerConfiguration(final Model model) {
		String banner, nameHeader, slogan;
		Integer notificationNotVisited;

		banner = this.configurationService.findBanner();
		slogan = this.configurationService.findSlogan();
		nameHeader = this.configurationService.findName();
		notificationNotVisited = this.notificationService.countNotVisitedByActorId();

		model.addAttribute("banner", banner);
		model.addAttribute("slogan", slogan);
		model.addAttribute("nameHeader", nameHeader);
		model.addAttribute("notificationNotVisited", notificationNotVisited);
	}

	public String makeUrl(final HttpServletRequest request) {
		return request.getRequestURL() + "?" + request.getQueryString();
	}

	public boolean checkLinkImage(final String image) {
		boolean linkBroken;
		URL linkImage;
		HttpURLConnection openCode;
		String[] contentTypes;
		String contentType;

		linkBroken = false;

		if (image != null)
			try {
				linkImage = new URL(image);
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

	public Map<String, Boolean> checkLinkImages(final Collection<String> images) {
		Map<String, Boolean> mapLinkBoolean;
		boolean linkBroken;
		URL linkImage;
		HttpURLConnection openCode;
		String[] contentTypes;
		String contentType;

		mapLinkBoolean = new HashMap<String, Boolean>();
		linkBroken = false;

		if (images != null)
			for (final String link : images) {
				try {
					linkImage = new URL(link);
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
				mapLinkBoolean.put(link, linkBroken);
			}
		return mapLinkBoolean;
	}

	// Panic handler ----------------------------------------------------------

	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		result = new ModelAndView("misc/panic");
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("exception", oops.getMessage());
		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));

		return result;
	}

}

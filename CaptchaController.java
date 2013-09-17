package com.freecharge.admin.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.cage.Cage;
import com.github.cage.GCage;

@Controller
@RequestMapping("/admin/captcha/*")
public class CaptchaController {

	private static final long serialVersionUID = 1490947492185481844L;

	private static final Cage cage = new GCage();

	/**
	 * Generates a captcha token and stores it in the session.
	 * 
	 * @param session where to store the captcha.
	 */
	public static void generateToken(HttpSession session) {
		String token = cage.getTokenGenerator().next();

		session.setAttribute("captchaToken", token);
		markTokenUsed(session, false);
	}

	/**
	 * Used to retrieve previously stored captcha token from session.
	 * 
	 * @param session where the token is possibly stored.
	 * @return token or null if there was none
	 */
	public static String getToken(HttpSession session) {
		Object val = session.getAttribute("captchaToken");

		return val != null ? val.toString() : null;
	}

	/**
	 * Marks token as used/unused for image generation.
	 * 
	 * @param session where the token usage flag is possibly stored.
	 * @param used false if the token is not yet used for image generation
	 */
	protected static void markTokenUsed(HttpSession session, boolean used) {
		session.setAttribute("captchaTokenUsed", used);
	}

	/**
	 * Checks if the token was used/unused for image generation.
	 * 
	 * @param session where the token usage flag is possibly stored.
	 * @return true if the token was marked as unused in the session
	 */
	protected static boolean isTokenUsed(HttpSession session) {
		return !Boolean.FALSE.equals(session.getAttribute("captchaTokenUsed"));
	}

	@RequestMapping(value = "draw", method = RequestMethod.GET)
	public void draw(@RequestParam Map<String, String> mapping, Model model,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession(false);
		CaptchaController.generateToken(session);
	    String token = session != null ? getToken(session) : null;
	    if (token == null || isTokenUsed(session)) {
	      resp.sendError(HttpServletResponse.SC_NOT_FOUND,
	          "Captcha not found.");

	      return;
	    }

	    setResponseHeaders(resp);
	    markTokenUsed(session, true);
	    cage.draw(token, resp.getOutputStream());
	  }
	  
	@RequestMapping(value = "view")
	public String captcha(@RequestParam Map<String, String> mapping, Model model,
			HttpServletRequest req, HttpServletResponse resp) {
		return "captcha";
	}
	
	@RequestMapping(value = "regenerate")
	public String regenerate(@RequestParam Map<String, String> mapping, Model model,
			HttpServletRequest req, HttpServletResponse resp) {
		return "captcha-draw";
	}
	/**
	   * Helper method, disables HTTP caching.
	   * 
	   * @param resp
	   *            response object to be modified
	   */
	  protected void setResponseHeaders(HttpServletResponse resp) {
	    resp.setContentType("image/" + cage.getFormat());
	    resp.setHeader("Cache-Control", "no-cache, no-store");
	    resp.setHeader("Pragma", "no-cache");
	    long time = System.currentTimeMillis();
	    resp.setDateHeader("Last-Modified", time);
	    resp.setDateHeader("Date", time);
	    resp.setDateHeader("Expires", time);
	  }
}

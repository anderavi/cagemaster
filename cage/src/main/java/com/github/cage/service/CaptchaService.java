package com.github.cage.service;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.github.cage.ACage;
import com.github.cage.Cage;

public class CaptchaService {
    private static final long serialVersionUID = 1490947492185481844L;

    private static final Cage cage = new ACage();
    
    private static final CaptchaService captchaService = new CaptchaService();
    
    public static CaptchaService getInstance() {
        return captchaService;
    }
    /**
     * Generates a captcha token and stores it in the session.
     * 
     * @param session where to store the captcha.
     */
    public void generateToken(HttpSession session) {
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
    public String getToken(HttpSession session) {
        Object val = session.getAttribute("captchaToken");

        return val != null ? val.toString() : null;
    }

    /**
     * Marks token as used/unused for image generation.
     * 
     * @param session where the token usage flag is possibly stored.
     * @param used false if the token is not yet used for image generation
     */
    public void markTokenUsed(HttpSession session, boolean used) {
        session.setAttribute("captchaTokenUsed", used);
    }

    /**
     * Checks if the token was used/unused for image generation.
     * 
     * @param session where the token usage flag is possibly stored.
     * @return true if the token was marked as unused in the session
     */
    public boolean isTokenUsed(HttpSession session) {
        return !Boolean.FALSE.equals(session.getAttribute("captchaTokenUsed"));
    }

    public void draw(String token, ServletOutputStream outputStream) throws IOException {
        // TODO Auto-generated method stub
        cage.draw(token, outputStream);
    }

    /**
     * Helper method, disables HTTP caching.
     * 
     * @param resp response object to be modified
     */
    public void setResponseHeaders(HttpServletResponse resp) {
        resp.setContentType("image/" + cage.getFormat());
        resp.setHeader("Cache-Control", "no-cache, no-store");
        resp.setHeader("Pragma", "no-cache");
        long time = System.currentTimeMillis();
        resp.setDateHeader("Last-Modified", time);
        resp.setDateHeader("Date", time);
        resp.setDateHeader("Expires", time);
    }

    public boolean isCaptchValid(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String sessionToken = getToken(session);
        String requestToken = request.getParameter("captcha");
        return sessionToken != null && sessionToken.equals(requestToken);
    }
}

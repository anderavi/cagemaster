package com.web.usage;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.github.cage.service.CaptchaService;

public class CaptchaServlet extends HttpServlet  {

    CaptchaService captchaService = null;
    
    public void init() throws ServletException {
        captchaService = CaptchaService.getInstance();
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpSession session = request.getSession(false);

        captchaService.generateToken(session);
        String token = session != null ? captchaService.getToken(session) : null;
        if (token == null || captchaService.isTokenUsed(session)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Captcha not found.");

            return;
        }

        captchaService.setResponseHeaders(response);
        // captchaService.markTokenUsed(session, true);
        captchaService.draw(token, response.getOutputStream());
        return;
    }
        
}

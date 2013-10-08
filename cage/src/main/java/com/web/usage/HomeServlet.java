package com.web.usage;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.cage.service.CaptchaService;

public class HomeServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    CaptchaService captchaService = null;
    
    public void init() throws ServletException {
        captchaService = CaptchaService.getInstance();
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);

    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String captcha = request.getParameter("captcha");
        boolean isValid = captchaService.isCaptchValid(request);
        String message = "";
        if(isValid) {
            message = "You have entered CORRECT captcha";
        } else {
            message = "You have entered a INCORRECT captcha";
        }
        response.sendRedirect("/?message="+message);

    }

}
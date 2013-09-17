<%@page import="com.freecharge.admin.controller.CaptchaController"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="mt" uri="http://www.freecharge.com/myTlds" %>
<%
  boolean showGoodResult;
  boolean showBadResult;
  if ("POST".equals(request.getMethod())) {
    String sessionToken = CaptchaController.getToken(session);
    String requestToken = request.getParameter("captcha");
    showGoodResult = sessionToken != null && sessionToken.equals(requestToken);
    showBadResult = !showGoodResult;
  } else {
    showGoodResult = showBadResult = false;
  }

%><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta charset="UTF-8" />
    <title>Captcha Reader</title>
    		
	<script type="text/javascript" src="${mt:keyValue("jsprefix2")}/jquery-1.7.1.min.js?v=4"></script>	
  </head>
  <body>
<%  if (showGoodResult) {%>
  <h1 style="color: green;">Your kung fu is good!</h1>
<%  } else if (showBadResult) {%>
  <h1 style="color: red;">This is not right. Try again!</h1>
<%  } %>
    <p>Type in the word seen on the picture</p>
    <form action="" method="post">
      <input name="captcha" type="text" autocomplete="off" />
      <input type="submit" />
    </form>
    <div id="captcha-draw-id"><jsp:include page="captcha-draw.jsp"></jsp:include></div>
     <a href="javascript:void(0)" onclick="redrawCaptcha();">Refresh</a>
     <script type="text/javascript">
     	function redrawCaptcha() {
     		$.ajax({
    			type : "GET",
    			url:"/admin/captcha/regenerate",
    			cache: false,
    			success : function(data) {
    				$("#captcha-draw-id").html(data); 
    			},
    			error:function(xhr,ajaxOptions, thrownError){
    				$("#captcha-draw-id").html("retry"); 
    			}
    		}) 
     	}
     </script>
  </body>
</html>
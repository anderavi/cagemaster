<%@page import="java.util.Calendar"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<html>
  <head>
    <title>Captcha Reader</title>
	<script type="text/javascript" src="/content/jquery-1.10.2.min.js"></script>	
  </head>
  <body>
  	<%
  	if(request.getParameter("message") != null) {
  	%>
  		<div style="font-size: 16px; color: red;"><%=request.getParameter("message") %></div>
  	<%} %>
  	
    <p>Type in the word seen on the picture</p>
    <form action="" method="post">
      <input name="captcha" type="text" autocomplete="off" />
      <input type="submit" />
    </form>
      <%
          Calendar cal = Calendar.getInstance();
          long time = cal.getTimeInMillis();
      %>
      <table>
        <tr>
            <td id="captchaImageHolder"><img alt="captcha image" id="captcha-image" src="/captcha/draw?time=<%=time%>" /> </td>
            <td style="vertical-align: middle;">
                <a href="javascript:void(0);" onclick="reloadCaptcha();" style="color:blue;">Refresh Image</a>
                <script type="text/javascript">
                function reloadCaptcha() {
                    var now = new Date();
                    $("#captchaImageHolder").html("<img alt=\"captcha image\" id=\"captcha-image\" src=\"/captcha/draw?time="+now.getMilliseconds()+"\" />");
                }
                </script>
            </td>
        </tr>
      </table>
    
  </body>
</html>
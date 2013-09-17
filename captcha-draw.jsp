<%@page import="java.util.Calendar"%>
<%
	Calendar cal = Calendar.getInstance();
	long time = cal.getTimeInMillis();
%>
<img alt="captcha image" id="captcha-image" src="/admin/captcha/draw?time=<%=time%>" />
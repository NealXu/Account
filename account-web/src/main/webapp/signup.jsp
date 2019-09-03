<%@ page contentType="text/html; charset=gb2312" language="java" %>
<%@ page import="com.juvenxu.mvnbook.account.service.*,
  org.springframework.context.ApplicationContext,
  org.springframework.web.context.support.WebApplicationContextUtils"%>
<html>
<head>
<style type="text/css">

.text-field {position: absolute; left: 40%; background-color:rgb(255,230,220);}
label {display: inline-table; width: 90px; margin: 0px 0px 10px 20px; }
input {display: inline-table; width: 150px; margin: 0px 20px 10px 0px;}
img {width:150px; margin: 0px 20px 10px 110px;}
h2 {margin: 20px 20px 20px 40px;}
button {margin: 20px 20px 10px 110px}
</style>
</head>
<body>

<%
ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext( getServletContext() );
AccountService accountervice = (AccountService) context.getBean( "accountService" );
String captchaKey = accountervice.generateCaptchaKey();
%>

<div class="text-field">

<h2>ע�����˻�</h2>
<form name="signup" action="signup" method="post">
<label>�˻�ID��</label><input type="text" name="id"></input><br/>
<label>Email��</label><input type="text" name="email"></input><br/>
<label>��ʾ���ƣ�</label><input type="text" name="name"></input><br/>
<label>���룺</label><input type="password" name="password"></input><br/>
<label>ȷ�����룺</label><input type="password" name="confirm_password"></input><br/>
<label>��֤�룺</label><input type="text" name="captcha_value"></input><br/>
<input type="hidden" name="captcha_key" value="<%=captchaKey%>"/>
<img src="<%=request.getContextPath()%>/captcha_image?key=<%=captchaKey%>"/>
</br>
<button>ȷ�ϲ��ύ</button>
</form>
</div>

</body>
</html>

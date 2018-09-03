<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/11/8 0008
  Time: 下午 3:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>获取权限</title>
</head>
<body>
<br />
<form>
  用户id:<input name="userId" type="text" size="10">

  </input>
  <br />
  <input type="submit">
</form>
<br />
<%@ page import="com.nd.gaea.core.utils.StringUtils" %>
<%@ page import="com.nd.auxo.recommend.core.util.MD5Util" %>
<%
    String data = request.getParameter("userId");
    if (StringUtils.isEmpty(data)) {
       return;
    }
    String result = MD5Util.encryptMD5_ND(data );
    response.getWriter().println("result:<br/>" + result+","+System.currentTimeMillis() + "<br/>");
%>
</body>
</html>

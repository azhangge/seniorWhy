<html>
<body>
<br/>

<form>
    <input name="data" type="text" size="100" value='1326'>

    </input>
    <br/>
    <input type="submit">
</form>
<br/>
<br/>
<%@ page import="com.nd.gaea.core.utils.StringUtils" %>
<%@ page import="com.nd.gaea.waf.security.DESUtil" %>
<%
    String data = request.getParameter("data");
    if (StringUtils.isEmpty(data)) {
        return;
    }

    response.getWriter().println("result:<br/>" + DESUtil.encode(data) + "<br/>");


%>
</body>
</html>


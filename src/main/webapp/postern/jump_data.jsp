<html>
<body>
<br />
<form>
    <input name="data" type="text" size="100" >

    </input>
    <br />
    <input type="submit">
</form>
<br />
<br />
<%@ page import="com.nd.auxo.sdk.recommend.util.CustomDataUtil" %>
<%@ page import="com.nd.gaea.core.utils.StringUtils" %>
<%@ page import="com.nd.auxo.sdk.recommend.vo.activity.JumpData" %>
<%@ page import="com.nd.gaea.core.utils.ObjectUtils" %>
<%
    String data = request.getParameter("data");
    if (StringUtils.isEmpty(data)) {
        return;
    }
    JumpData result = CustomDataUtil.parseCustomData(data,JumpData.class);

    response.getWriter().println("result:<br/>" + ObjectUtils.toJson(result) + "<br/>");



%>
</body>
</html>


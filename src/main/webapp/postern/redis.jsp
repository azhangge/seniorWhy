<html>
<body>
<br/>

<form>
    <input name="data" type="text" size="100" value=''>

    </input>
    <br/>
    <input type="submit">
</form>
<br/>
<br/>
<%@ page import="com.nd.gaea.context.support.ApplicationSpringContextHolder" %>
<%@ page import="com.nd.gaea.core.utils.StringUtils" %>
<%@ page import="org.springframework.data.redis.core.RedisTemplate" %>
<%@ page import="java.util.concurrent.TimeUnit" %>
<%@ page import="com.nd.auxo.recommend.core.util.DateUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%
    String data = request.getParameter("data");
    if (StringUtils.isEmpty(data)) {
        return;
    }
    RedisTemplate<String, String> redisTemplate = ApplicationSpringContextHolder.getInstance().getApplicationContext().getBean(RedisTemplate.class);
    String expire = redisTemplate.getExpire(data, TimeUnit.SECONDS) + " s";

    String value = redisTemplate.opsForValue().get(data);


    response.getWriter().println("expire :" + expire + "<br/>");

    response.getWriter().println("value :" + value + "<br/>");


    response.getWriter().println("endTime :" + new Date(com.nd.gaea.core.utils.DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH).getTime()+86399999l) + "<br/>");

    response.getWriter().println("new Date  :" + new Date() + "<br/>");



%>
</body>
</html>


<html>
<body>
<br/>

<form>
    <input name="data" type="text" size="100" value=''>

    </input>
    <br/>
    <input type="submit" value="delete">
</form>
<br/>
<br/>
<%@ page import="com.nd.gaea.context.support.ApplicationSpringContextHolder" %>
<%@ page import="com.nd.gaea.core.utils.StringUtils" %>
<%@ page import="org.springframework.dao.DataAccessException" %>
<%@ page import="org.springframework.data.redis.connection.RedisConnection" %>
<%@ page import="org.springframework.data.redis.core.RedisCallback" %>
<%@ page import="org.springframework.data.redis.core.RedisTemplate" %>
<%
    try {
        final String data = request.getParameter("data");
        if (StringUtils.isEmpty(data)) {
            return;
        }
        RedisTemplate<String, Long> redisTemplate = ApplicationSpringContextHolder.getInstance().getApplicationContext().getBean(RedisTemplate.class);
        Long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.del(data.getBytes());
            }
        });
        response.getWriter().println("result :" + result + "<br/>");
    } catch (Exception e) {
        e.printStackTrace(response.getWriter());
    }


%>
</body>
</html>


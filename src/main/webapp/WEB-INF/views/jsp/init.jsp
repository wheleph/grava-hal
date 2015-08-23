<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title><spring:message code="title"/></title>
    </head>
    <body>
        <form action="start_game" method="POST">
            <button type="submit"><spring:message code="button.start.game"/></button>
        </form>
    </body>
</html>
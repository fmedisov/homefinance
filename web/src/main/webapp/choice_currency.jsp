<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 11.07.2018
  Time: 20:50
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update Currency</title>
</head>
<body>
<jsp:include page="_menu.jsp"></jsp:include>
<br>
<form action="choice_currency" method="post">
    <br>

    <br>
    <select name="currency" >
        <c:forEach var="cur" items="${list_currencies}">
            <option value="${cur.getId()}">${cur.getName()}</option>
        </c:forEach>
    </select>
    <br>
    <br>
    <br>
    <input type="submit" value="Update"/>
</form>
</body>
</html>

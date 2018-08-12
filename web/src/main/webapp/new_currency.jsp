<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 12.07.2018
  Time: 0:15
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Currency</title>
</head>
<body>
<jsp:include page="_menu.jsp"></jsp:include>
<br>
<form action="add_currency" method="post">
    <br>
    Name:<input type="text" name="name" value="<c:out value="${currency.getName}" escapeXml="false"  default="guest"/>"/><br/>
    <br>
    Code:<input type="text" name="code"/><br/>
    <br>
    Symbol:<input type="text" name="symbol"/><br/>
    <br>
    <br>
    <input type="submit" value="Save"/>
</form>
</body>
</html>

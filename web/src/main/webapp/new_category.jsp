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
    <title>New Category</title>
</head>
<body>
<jsp:include page="_menu.jsp"></jsp:include>
<br>
<form action="add_category" method="post">
    <br>
    Name:<input type="text" name="name"/><br/>

    <br>
    <select name="parent_category" >
        <c:forEach var="cat" items="${list_categories}">
            <option value="${cat.getName()}">${cat.getName()}</option>
        </c:forEach>
    </select>
    <br>
    <br>

    <input type="submit" value="Save"/>
</form>
</body>
</html>

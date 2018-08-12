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
    <title>New Account</title>
</head>
<body>
<jsp:include page="_menu.jsp"></jsp:include>
<br>
<form action="add_account" method="post">
    <br>
    Name:<input type="text" name="name"/><br/>

    <br>
    <select name="currency" >
        <c:forEach var="cur" items="${list_currencies}">
            <option value="${cur.getName()}">${cur.getName()}</option>
        </c:forEach>
    </select>
    <br>

    <br>
    <select name="accountType" >
        <c:forEach var="acc_type" items="${list_accountTypes}">
            <option value="${acc_type.getName()}">${acc_type.getName()}</option>
        </c:forEach>
    </select>
    <br>
    <br>

    amount:<input type="text" name="amount"/><br/>
    <br>
    <input type="submit" value="Save"/>
</form>
</body>
</html>

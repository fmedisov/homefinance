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
    <title>New Transaction</title>
</head>
<body>
<jsp:include page="_menu.jsp"></jsp:include>
<br>
<form action="add_transaction" method="post">
    <br>
    Name:<input type="text" name="name"/><br/>
    <br>
    amount:<input type="text" name="amount"/><br/>
    <br>
    Date (YYYY-MM-DD):<input type="text" name="transaction_date"/><br/>
    <br>

    <br>
    <select name="account" >
        <c:forEach var="acc" items="${list_accounts}">
            <option value="${acc.getName()}">${acc.getName()}</option>
        </c:forEach>
    </select>
    <br>

    <br>
    <select name="category" >
        <c:forEach var="cat" items="${list_categories}">
            <option value="${cat.getName()}">${cat.getName()}</option>
        </c:forEach>
    </select>
    <br>

    <br>
    <select name="transactionType" >
        <c:forEach var="tr_type" items="${list_transactionTypes}">
            <option value="${tr_type.getName()}">${tr_type.getName()}</option>
        </c:forEach>
    </select>
    <br>

    <br>

    Tags (tag1:tag2:tag3):<input type="text" name="tags"/><br/>
    <br>

    <input type="submit" value="Save"/>
</form>
</body>
</html>

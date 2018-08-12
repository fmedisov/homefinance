<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Transaction Info</title>
</head>
<body>

<jsp:include page="_menu.jsp"></jsp:include>

<c:forEach var="transaction" items="${list_transactions}">
    <p>${transaction}</p>
</c:forEach>

<jsp:include page="menu_transaction.jsp"></jsp:include>

</body>
</html>

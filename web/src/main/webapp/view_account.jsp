<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Account Info</title>
</head>
<body>

<jsp:include page="_menu.jsp"></jsp:include>

<c:forEach var="account" items="${list_accounts}">
    <p>${account}</p>
</c:forEach>

<jsp:include page="menu_account.jsp"></jsp:include>

</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Currency Info</title>
</head>
<body>

<jsp:include page="_menu.jsp"></jsp:include>

<c:forEach var="currency" items="${list_currencies}">
    <p>${currency}</p>
</c:forEach>

<jsp:include page="menu_currency.jsp"></jsp:include>

</body>
</html>

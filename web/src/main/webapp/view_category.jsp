<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Category Info</title>
</head>
<body>

<jsp:include page="_menu.jsp"></jsp:include>

<c:forEach var="category" items="${list_categories}">
    <p>${category}</p>
</c:forEach>

<jsp:include page="menu_category.jsp"></jsp:include>

</body>
</html>

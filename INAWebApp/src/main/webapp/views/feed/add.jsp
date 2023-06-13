<%@ page import="de.whs.ni37900.ina.praktikum.inawebapp.models.feed.FeedsBean" %>
<%@ page import="jakarta.validation.ConstraintViolation" %>
<%@ page import="java.util.Set" %>
<jsp:useBean id="bean" scope="request" type="de.whs.ni37900.ina.praktikum.inawebapp.models.feed.FeedsBean"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Feed</title>
</head>
<body>
<form method="get">
    <% Set<ConstraintViolation<FeedsBean>> violations = (Set<ConstraintViolation<FeedsBean>>) request.getAttribute("violations"); %>
    <label for="name">Name: <input type="text" name="name" id="name" value="${bean.name}"></label>
    <div style="color: red;">
        <%
            for (ConstraintViolation<FeedsBean> violation : violations) {
                if (!"name".equals(violation.getPropertyPath().toString()))
                    continue;

                out.print("<span>");
                out.print(violation.getMessage());
                out.print("</span>");
                out.println();
            }
        %>
    </div>
    <br/>
    <label for="url1">URL: <input type="text" name="url1" id="url1" value="${bean.url1}"></label>
    <div style="color: red;">
        <%
            for (ConstraintViolation<FeedsBean> violation : violations) {
                if (!"url1".equals(violation.getPropertyPath().toString()))
                    continue;

                out.print("<span>");
                out.print(violation.getMessage());
                out.print("</span>");
                out.println();
            }
        %>
    </div>
    <br/>
    <label for="url2">URL: <input type="text" name="url2" id="url2" value="${bean.url2}"></label>
    <div style="color: red;">
        <%
            for (ConstraintViolation<FeedsBean> violation : violations) {
                if (!"url2".equals(violation.getPropertyPath().toString()))
                    continue;

                out.print("<span>");
                out.print(violation.getMessage());
                out.print("</span>");
                out.println();
            }
        %>
    </div>
    <br/>
    <label for="url3">URL: <input type="text" name="url3" id="url3" value="${bean.url3}"></label>
    <div style="color: red;">
        <%
            for (ConstraintViolation<FeedsBean> violation : violations) {
                if (!"url3".equals(violation.getPropertyPath().toString()))
                    continue;

                out.print("<span>");
                out.print(violation.getMessage());
                out.print("</span>");
                out.println();
            }
        %>
    </div>
    <br/>
    <input type="submit">
</form>
</body>
</html>

<%@ page import="de.whs.ni37900.ina.praktikum.inawebapp.models.user.UserBean" %>
<%@ page import="jakarta.validation.ConstraintViolation" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% final UserBean user = (UserBean) request.getAttribute("user"); %>

<html>
<head>
    <title><% if (user == null) out.print("Login");
    else out.print("Profile"); %></title>
</head>
<body>
<%
    if (user != null) {
        out.println(String.format("Angemeldet als %s", user.getName()));
        out.println(String.format("<form action=\"logout\" method=\"GET\"><input type=\"hidden\" value=\"%s\"><input type=submit value=\"Abmelden\"></form>", user.getId()));
    } else {
        String name = request.getParameter("name");
        if (name == null) name = "";
        String password = request.getParameter("password");
        if (password == null) password = "";

        out.println("<form method=\"POST\">");
        out.println(String.format("<label for=\"name\">Benutzername: <input type=\"text\" name=\"name\" id=\"name\" value=\"%s\"></label><br>", name));
        out.println(String.format("<label for=\"password\">Passwort: <input type=\"password\" name=\"password\" id=\"password\" value=\"%s\"></label><br>", password));
        out.println("<input type=\"submit\" value=\"Anmelden\">");
        out.println("</form>");
    }
%>
<%
    Set<ConstraintViolation<UserBean>> violations = (Set<ConstraintViolation<UserBean>>) request.getAttribute("violations");
    if (violations != null)
        for (ConstraintViolation<UserBean> violation : violations)
            out.println(String.format("<span style=\"color: red;\">%s</span><br>", violation.getMessage()));
%>
<%
    String error = (String) request.getAttribute("error");
    if (error != null)
        out.println(String.format("<span style=\"color: red;\">%s</span><br>", error));
%>
</body>
</html>

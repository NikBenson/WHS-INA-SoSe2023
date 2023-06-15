<%@ page import="jakarta.validation.ConstraintViolation" %>
<%@ page import="java.util.Set" %>
<%@ page import="de.whs.ni37900.ina.p1.RSSFeed" %>
<%@ page import="de.whs.ni37900.ina.praktikum.inawebapp.models.feed.edit.AddFeedBean" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Feed</title>
</head>
<body>
<h1>Edit Feed</h1>
<%
    final List<RSSFeed> feeds = (List<RSSFeed>) request.getAttribute("feeds");
    if (feeds != null && !feeds.isEmpty()) {
        out.println("<table>");
        out.println("<th>Name</th><th>URL</th><th></th>");
        for (final RSSFeed feed : feeds) {
            out.println(String.format("<tr><td>%s</td><td><a href=\"%s\">%s</a></td><td><form method=\"GET\" action=\"delete\"><input name=\"id\" type=\"hidden\" value=\"%s\"></input><input type=\"submit\" value=\"Delete\"></form></td></tr>", feed.getName(), feed.getUri(), feed.getUri(), feed.getId()));
        }
        out.println("<table>");
    }
%>

<form>
    <label for="url">
        Hinzufügen von <input type="text" name="url" placeholder="https://example.com" id="url"
        <%
                              final AddFeedBean bean = (AddFeedBean) request.getAttribute("bean");
                              if(bean != null && bean.getUrl() != null)
                                out.print(String.format("value=\"%s\"",bean.getUrl()));
%>
    ><br>
        <%
            Set<ConstraintViolation<AddFeedBean>> violations = (Set<ConstraintViolation<AddFeedBean>>) request.getAttribute("violations");
            if (violations != null)
                for (ConstraintViolation<AddFeedBean> violation : violations)
                    out.println(String.format("<span style=\"color: red;\">%s</span><br>", violation.getMessage()));
        %>
        <%
            Exception parseException = (Exception) request.getAttribute("parseException");
            if (parseException != null)
                out.println(String.format("<span style=\"color: red;\">%s</span><br>", parseException.getMessage()));
        %>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null)
                out.println(String.format("<span style=\"color: red;\">%s</span><br>", error));
        %>
    </label><br>
    <%
        final RSSFeed[] newFeeds = (RSSFeed[]) request.getAttribute("newFeeds");

        if (newFeeds != null && newFeeds.length != 0) {
            out.println("<div>");
            for (RSSFeed feed : newFeeds) {
                out.println(String.format("<a style=\"color: lime\" href=\"%s\">%s</a><br>", feed.getUri(), feed.getName()));
            }
            out.println("</div><br>");
        }
    %>

    <input type="submit" formmethod="GET" value="Check">
    <input type="submit" formmethod="POST" value="Submit">
</form>
</body>
</html>

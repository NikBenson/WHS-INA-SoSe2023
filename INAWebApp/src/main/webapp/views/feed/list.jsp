<%@ page import="de.whs.ni37900.ina.p1.RSSItem" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.getDefault()); %>

<html>
<head>
    <title>Feed</title>
</head>
<body>
<h1>Feed</h1>
<%
    final Map<String, RSSItem[]> feeds = (Map<String, RSSItem[]>) request.getAttribute("feeds");

    if (feeds.isEmpty()) {
        out.println("Keine Feeds!");
    }

    for (String feedName : feeds.keySet()) {
        out.println(String.format("<h2>%s</h2>", feedName));

        final RSSItem[] items = feeds.get(feedName);

        if (items.length == 0) {

            out.println("Keine Items!");
        } else {
            out.println("<ul>");
            for (final RSSItem item : items) {
                out.println(String.format("<li onClick=\"window.location.href=\\\"%s\\\"\"><div>%s<br><a href=\"%s\">%s</a><br><p>%s</p><br><div><span align=\"left\">%s</span><span align=\"right\">%s</span></div></div></li>", item.link(), item.title(), item.link(), item.link(), item.description(), item.category(), DATE_TIME_FORMATTER.format(item.pubDate())));
            }
            out.println("</ul>");
        }
    }
%>

</body>
</html>

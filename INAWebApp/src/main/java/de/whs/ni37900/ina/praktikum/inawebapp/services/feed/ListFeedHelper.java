package de.whs.ni37900.ina.praktikum.inawebapp.services.feed;

import de.whs.ni37900.ina.p1.RSSFeed;
import de.whs.ni37900.ina.p1.RSSItem;
import de.whs.ni37900.ina.p1.RSSItemParser;
import de.whs.ni37900.ina.praktikum.inawebapp.models.user.UserBean;
import de.whs.ni37900.ina.praktikum.inawebapp.services.HelperBase;
import de.whs.ni37900.ina.praktikum.inawebapp.services.user.AuthHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListFeedHelper extends HelperBase {
    public static ListFeedHelper require(final HttpSession session) {
        return HelperBase.require(session, "ListFeedHelper", ListFeedHelper::new);
    }

    public ListFeedHelper(final HttpSession session) {
        super(session);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        final AuthHelper auth = AuthHelper.require(session);
        final UserBean user = auth.getUser();

        if (!auth.isLoggedIn()) {
            response.sendRedirect("../user/");
            return;
        }

        List<RSSFeed> feeds = user.getFeeds();
        if (feeds == null) {
            feeds = List.of();
        }

        final Map<String, RSSItem[]> parsedFeeds = feeds.stream().collect(Collectors.toMap(RSSFeed::getName, (final RSSFeed feed) -> {
            try {
                return new RSSItemParser(feed).extract();
            } catch (IOException e) {
                return new RSSItem[0];
            }
        }));
        request.setAttribute("feeds", parsedFeeds);

        request.getRequestDispatcher("../views/feed/list.jsp").include(request, response);
    }
}

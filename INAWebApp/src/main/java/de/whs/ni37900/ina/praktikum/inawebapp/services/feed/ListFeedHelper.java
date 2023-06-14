package de.whs.ni37900.ina.praktikum.inawebapp.services.feed;

import de.whs.ni37900.ina.p1.RSSFeed;
import de.whs.ni37900.ina.p1.RSSItem;
import de.whs.ni37900.ina.p1.RSSItemParser;
import de.whs.ni37900.ina.praktikum.inawebapp.models.feed.FeedsBean;
import de.whs.ni37900.ina.praktikum.inawebapp.services.HelperBase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListFeedHelper extends HelperBase {
    public static ListFeedHelper require(final HttpSession session) {
        return HelperBase.require(session, "ListFeedHelper", ListFeedHelper::new);
    }

    public final FeedsBean feeds = new FeedsBean();

    public ListFeedHelper(final HttpSession session) {
        super(session);
    }

    private static long id = 0;


    public void addFeeds(final RSSFeed... feeds) {
        for (var feed : feeds) {
            feed.setId(id++);
        }

        this.feeds.setFeeds(Stream.concat(this.feeds.getFeeds().stream(), Stream.of(feeds)).collect(Collectors.toList()));
    }

    public void clearFeeds() {
        feeds.setFeeds(List.of());
    }

    public void setFeeds(final RSSFeed... feeds) {
        clearFeeds();
        addFeeds(feeds);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        final Map<String, RSSItem[]> parsedFeeds = feeds.getFeeds().stream().collect(Collectors.toMap(RSSFeed::getName, (final RSSFeed feed) -> {
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

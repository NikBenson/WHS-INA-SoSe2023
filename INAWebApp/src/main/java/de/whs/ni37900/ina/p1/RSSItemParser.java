package de.whs.ni37900.ina.p1;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RSSItemParser {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    private final String uri;

    public RSSItemParser(final RSSFeed feed) {
        this(feed.getUri());
    }

    public RSSItemParser(final String uri) {
        this.uri = uri;
    }

    public static RSSItem[] extractFromUri(final String uri) throws IllegalArgumentException, IOException {
        Connection connection = Jsoup.connect(uri);
        final Document document = connection.get();

        return extractFromHTML(document, uri);
    }

    public static RSSItem[] extractFromHTML(final Document document, final String baseURI) throws IllegalArgumentException {
        final Elements links = document.select("rss > channel > item");

        return links.stream().map(link -> extractFromLink(link, baseURI)).toArray(RSSItem[]::new);
    }

    public static RSSItem extractFromLink(final Element item, final String baseURI) throws IllegalArgumentException {
        final String title = item.select("title").text();
        final URI link = URI.create(baseURI).resolve(item.select("link").text()).normalize();
        final String description = item.select("description").text();
        final String category = item.select("category").text();
        final ZonedDateTime pubDate = ZonedDateTime.parse(item.select("pubDate").text(), DATE_TIME_FORMATTER);

        return new RSSItem(title, link, description, category, pubDate);
    }

    public String getUri() {
        return uri;
    }

    public RSSItem[] extract() throws IllegalArgumentException, IOException {
        return extractFromUri();
    }

    public RSSItem[] extractFromUri() throws IllegalArgumentException, IOException {
        return extractFromUri(uri);
    }

    public RSSItem[] extractFromHTML(final Document document) throws IllegalArgumentException {
        return extractFromHTML(document, uri);
    }

    public RSSItem extractFromLink(final Element link) throws IllegalArgumentException {
        return extractFromLink(link, uri);
    }
}

package de.whs.ni37900.ina.p1;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;

class HTMLToRSSParser {
    private final URI uri;

    public HTMLToRSSParser(final URI uri) {
        this.uri = uri;
    }

    public static RSSFeed[] extractFromUri(final URI uri) throws IllegalArgumentException, IOException {
        Connection connection = Jsoup.connect(uri.toString());
        final Document document = connection.get();

        return extractFromHTML(document, uri);
    }

    public static RSSFeed[] extractFromHTML(final Document document, final URI baseURI) throws IllegalArgumentException {
        final Elements links = document.select("head > link[type='application/rss+xml']");

        return links.stream().map(link -> extractFromLink(link, baseURI)).toArray(RSSFeed[]::new);
    }

    public static RSSFeed extractFromLink(final Element link, final URI baseURI) throws IllegalArgumentException {
        final String title = link.attr("title");
        final URI href = baseURI.resolve(link.attr("href")).normalize();

        return new RSSFeed(title, href);
    }

    public URI getUri() {
        return uri;
    }

    public RSSFeed[] extract() throws IllegalArgumentException, IOException {
        return extractFromUri();
    }

    public RSSFeed[] extractFromUri() throws IllegalArgumentException, IOException {
        return extractFromUri(uri);
    }

    public RSSFeed[] extractFromHTML(final Document document) throws IllegalArgumentException {
        return extractFromHTML(document, uri);
    }

    public RSSFeed extractFromLink(final Element link) throws IllegalArgumentException {
        return extractFromLink(link, uri);
    }
}

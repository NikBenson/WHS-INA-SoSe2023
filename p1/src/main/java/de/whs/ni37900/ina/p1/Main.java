package de.whs.ni37900.ina.p1;

import java.io.IOException;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.getDefault());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final String inputURL = scanner.next();

        try {
            URI uri = parseInputURI(inputURL);
            System.out.printf("Parsing RSS feeds for %s:%n", uri);

            final RSSFeed[] feeds = new HTMLToRSSParser(uri).extract();
            if(feeds.length == 0)
                System.out.println("No feeds in website!");
            for (RSSFeed feed : feeds) {
                System.out.printf("  %s:%n", feed.name());

                final RSSItem[] items = new RSSItemParser(feed).extract();
                if(items.length == 0)
                    System.out.println("No items in feed!");
                for (RSSItem item : items) {
                    System.out.printf("    %s:%n", item.title());
                    System.out.printf("      %s%n", item.link());
                    System.out.printf("      %s%n", DATE_TIME_FORMATTER.format(item.pubDate()));
                }
            }

        } catch (IllegalArgumentException | IOException e) {
            System.out.println("Invalid URI!");
        }

    }

    static URI parseInputURI(final String input) throws IllegalArgumentException {
        URI uri = URI.create(input);
        if (uri.getScheme() == null) {
            uri = URI.create("http://" + uri);
        }
        return uri.normalize();
    }
}

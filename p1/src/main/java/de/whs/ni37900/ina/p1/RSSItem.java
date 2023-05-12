package de.whs.ni37900.ina.p1;

import java.net.URI;
import java.time.ZonedDateTime;

public record RSSItem(String title, URI link, String description, String category, ZonedDateTime pubDate) {
}

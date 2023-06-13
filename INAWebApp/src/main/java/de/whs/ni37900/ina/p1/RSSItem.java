package de.whs.ni37900.ina.p1;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Objects;

public final class RSSItem {
    private final String title;
    private final URI link;
    private final String description;
    private final String category;
    private final ZonedDateTime pubDate;

    RSSItem(String title, URI link, String description, String category, ZonedDateTime pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.category = category;
        this.pubDate = pubDate;
    }

    public String title() {
        return title;
    }

    public URI link() {
        return link;
    }

    public String description() {
        return description;
    }

    public String category() {
        return category;
    }

    public ZonedDateTime pubDate() {
        return pubDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RSSItem) obj;
        return Objects.equals(this.title, that.title) &&
                Objects.equals(this.link, that.link) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.category, that.category) &&
                Objects.equals(this.pubDate, that.pubDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link, description, category, pubDate);
    }

    @Override
    public String toString() {
        return "RSSItem[" +
                "title=" + title + ", " +
                "link=" + link + ", " +
                "description=" + description + ", " +
                "category=" + category + ", " +
                "pubDate=" + pubDate + ']';
    }

}

package de.whs.ni37900.ina.praktikum.inawebapp.models.feed;

import de.whs.ni37900.ina.p1.RSSFeed;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class FeedsBean {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<RSSFeed> feeds;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<RSSFeed> getFeeds() {
        return feeds != null ? feeds : List.of();
    }

    public void setFeeds(List<RSSFeed> feeds) {
        this.feeds = feeds;
    }
}

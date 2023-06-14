package de.whs.ni37900.ina.praktikum.inawebapp.models.feed.edit;

import org.hibernate.validator.constraints.URL;

public class AddFeedBean {

    @URL(message = "Bitte gib eine URL ein!")
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

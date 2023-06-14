package de.whs.ni37900.ina.praktikum.inawebapp.services;

import jakarta.servlet.http.HttpSession;

public class HelperBase {
    protected HttpSession session;

    protected static <T extends HelperBase> T require(final HttpSession session, final String name, HelperFactory<T> factory) {
        if(session.getAttribute(name) == null)
            session.setAttribute(name, factory.create(session));

        return (T) session.getAttribute(name);
    }

    public HelperBase(final HttpSession session) {
        this.session = session;
    }

    protected interface HelperFactory<T extends HelperBase> {
        T create(final HttpSession session);
    }
}

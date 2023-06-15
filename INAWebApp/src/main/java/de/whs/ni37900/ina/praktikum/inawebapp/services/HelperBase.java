package de.whs.ni37900.ina.praktikum.inawebapp.services;

import jakarta.servlet.http.HttpSession;

public class HelperBase {
    protected HttpSession session;

    protected static <T extends HelperBase> T require(final HttpSession session, final String name, HelperFactory<T> factory) {
        if(session.getAttribute(name) == null)
            session.setAttribute(name, factory.create(session));

        final T helper =  (T) session.getAttribute(name);
        helper.session = session;
        return helper;
    }

    public HelperBase(final HttpSession session) {
        this.session = session;
    }

    protected interface HelperFactory<T extends HelperBase> {
        T create(final HttpSession session);
    }
}

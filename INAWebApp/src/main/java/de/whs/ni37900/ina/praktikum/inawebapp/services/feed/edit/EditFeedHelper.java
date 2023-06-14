package de.whs.ni37900.ina.praktikum.inawebapp.services.feed.edit;

import de.whs.ni37900.ina.p1.HTMLToRSSParser;
import de.whs.ni37900.ina.p1.RSSFeed;
import de.whs.ni37900.ina.praktikum.inawebapp.models.feed.FeedsBean;
import de.whs.ni37900.ina.praktikum.inawebapp.models.feed.edit.AddFeedBean;
import de.whs.ni37900.ina.praktikum.inawebapp.services.HelperBase;
import de.whs.ni37900.ina.praktikum.inawebapp.services.feed.ListFeedHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EditFeedHelper extends HelperBase {
    private static Validator validator;

    public static EditFeedHelper require(final HttpSession session) {
        return HelperBase.require(session, "AddFeedHelper", EditFeedHelper::new);
    }

    public EditFeedHelper(final HttpSession session) {
        super(session);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        final ListFeedHelper listFeedHelper = ListFeedHelper.require(request.getSession());
        request.setAttribute("feeds", listFeedHelper.feeds);

        final AddFeedBean bean = new AddFeedBean();
        request.setAttribute("bean", bean);

        final String url = request.getParameter("url");

        if (url == null) {
            request.getRequestDispatcher("../../views/feed/edit.jsp").include(request, response);
            return;
        }
        bean.setUrl(url);

        final Set<ConstraintViolation<AddFeedBean>> violations = validator.validate(bean);
        request.setAttribute("violations", violations);
        if (!violations.isEmpty()) {
            request.getRequestDispatcher("../../views/feed/edit.jsp").include(request, response);
            return;
        }


        RSSFeed[] feeds;
        try {
            feeds = new HTMLToRSSParser(url).extract();
        } catch (IOException | IllegalArgumentException e) {
            request.setAttribute("parseException", e);
            request.getRequestDispatcher("../../views/feed/edit.jsp").include(request, response);
            return;
        }

        request.setAttribute("newFeeds", feeds);
        request.getRequestDispatcher("../../views/feed/edit.jsp").include(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String url = request.getParameter("url");

        if (url == null) {
            response.setStatus(400);
            return;
        }

        RSSFeed[] feeds;
        try {
            feeds = new HTMLToRSSParser(url).extract();
        } catch (IOException | IllegalArgumentException e) {
            response.setStatus(400);
            return;
        }

        ListFeedHelper.require(request.getSession()).addFeeds(feeds);
        doGet(request, response);
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String idStr = request.getParameter("id");

        if (idStr == null) {
            response.setStatus(400);
            return;
        }

        final long id;

        try {
            id = Long.parseUnsignedLong(idStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Trying to delete invalid ID!");
            doGet(request, response);
            return;
        }


        final FeedsBean feeds = ListFeedHelper.require(request.getSession()).feeds;

        feeds.setFeeds(feeds.getFeeds().stream().filter(feed -> !Objects.equals(feed.getId(), id)).collect(Collectors.toList()));
        doGet(request, response);
    }
}

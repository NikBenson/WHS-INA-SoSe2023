package de.whs.ni37900.ina.praktikum.inawebapp.services.feed.edit;

import de.whs.ni37900.ina.p1.HTMLToRSSParser;
import de.whs.ni37900.ina.p1.RSSFeed;
import de.whs.ni37900.ina.praktikum.inawebapp.models.feed.edit.AddFeedBean;
import de.whs.ni37900.ina.praktikum.inawebapp.models.user.UserBean;
import de.whs.ni37900.ina.praktikum.inawebapp.services.HelperBase;
import de.whs.ni37900.ina.praktikum.inawebapp.services.feed.ListFeedHelper;
import de.whs.ni37900.ina.praktikum.inawebapp.services.hibernate.PersistenceHelper;
import de.whs.ni37900.ina.praktikum.inawebapp.services.user.AuthHelper;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EditFeedHelper extends HelperBase {
    private final Validator validator;

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

        final AuthHelper auth = AuthHelper.require(session);
        if (!auth.isLoggedIn()) {
            response.sendRedirect("../../user/");
            return;
        }
        final UserBean user = auth.getUser();

        request.setAttribute("feeds", user.getFeeds());

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
        final AuthHelper auth = AuthHelper.require(session);
        if (!auth.isLoggedIn()) {
            response.sendRedirect("../../user/");
            return;
        }
        final UserBean user = auth.getUser();

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

        final PersistenceHelper persistence = PersistenceHelper.require(session);
        for (RSSFeed feed :
                feeds) {
            feed.setOwner(user);
            if (validator.validate(feed).isEmpty())
                persistence.saveOrUpdate(feed);
        }

        doGet(request, response);
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final AuthHelper auth = AuthHelper.require(session);
        if (!auth.isLoggedIn()) {
            response.sendRedirect("../../user/");
            return;
        }
        final UserBean user = auth.getUser();

        final String idStr = request.getParameter("id");

        if (idStr == null) {
            request.setAttribute("error", "Trying to delete invalid ID!");
            response.sendRedirect(".");
            return;
        }

        final long id;

        try {
            id = Long.parseUnsignedLong(idStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Trying to delete invalid ID!");
            response.sendRedirect(".");
            return;
        }


        List<RSSFeed> feeds = user.getFeeds();
        if (feeds == null) {
            feeds = List.of();
        }

        final SessionFactory sessionFactory = PersistenceHelper.require(session).getSessionFactory();
        final Session hibSession = sessionFactory.getCurrentSession();
        final Transaction transaction = hibSession.beginTransaction();
        feeds.stream().filter(feed -> Objects.equals(feed.getId(), id)).forEach(hibSession::remove);
        transaction.commit();
        response.sendRedirect(".");
    }
}

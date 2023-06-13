package de.whs.ni37900.ina.praktikum.inawebapp.services.feed.add;

import de.whs.ni37900.ina.p1.HTMLToRSSParser;
import de.whs.ni37900.ina.p1.RSSFeed;
import de.whs.ni37900.ina.praktikum.inawebapp.models.feed.FeedsBean;
import de.whs.ni37900.ina.praktikum.inawebapp.services.HelperBase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddFeedHelper extends HelperBase {
    private static Validator validator;

    public static AddFeedHelper require(final HttpSession session) {
        return HelperBase.require(session, "AddFeedHelper", AddFeedHelper::new);
    }

    private final FeedsBean bean = new FeedsBean();

    public AddFeedHelper(final HttpSession session) {
        super(session);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");


        final String[] urls = request.getParameterValues("url");

        Map<String, RSSFeed[]> feeds = Stream.of(urls).collect(Collectors.toMap((url) -> url, (url) -> {
            try {
                return new HTMLToRSSParser(url).extract();
            } catch (IOException e) {
                return new RSSFeed[0];
            }
        }));

        Set<ConstraintViolation<FeedsBean>> violations = validator.validate(bean);

        if (!violations.isEmpty()) {
            request.setAttribute("violations", violations);
            request.setAttribute("bean", bean);
            request.getRequestDispatcher("../views/feed/add.jsp").include(request, response);
            return;
        }

        response.setStatus(301);
        response.setHeader("Location", ".");
        response.setHeader("Connection", "close");

    }
}

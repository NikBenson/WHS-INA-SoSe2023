package de.whs.ni37900.ina.praktikum.inawebapp.services.user;

import de.whs.ni37900.ina.praktikum.inawebapp.models.user.UserBean;
import de.whs.ni37900.ina.praktikum.inawebapp.services.HelperBase;
import de.whs.ni37900.ina.praktikum.inawebapp.services.hibernate.PersistenceHelper;
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
import java.util.Set;

public class AuthHelper extends HelperBase {

    public static AuthHelper require(final HttpSession session) {
        return HelperBase.require(session, "AuthHelper", AuthHelper::new);
    }

    private final Validator validator;

    private UserBean user = null;

    public AuthHelper(final HttpSession session) {
        super(session);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public UserBean logIn(final String username, final String password) {
        final SessionFactory sessionFactory = PersistenceHelper.require(session).getSessionFactory();
        final Session hibSession = sessionFactory.getCurrentSession();
        final Transaction transaction = hibSession.beginTransaction();
        final HibernateCriteriaBuilder builder = hibSession.getCriteriaBuilder();
        final JpaCriteriaQuery<UserBean> critquery = builder.createQuery(UserBean.class);
        final Root<UserBean> root = critquery.from(UserBean.class);
        critquery.select(root).where(builder.and(builder.equal(root.get("name"), username), builder.equal(root.get("password"), password)));
        user = hibSession.createQuery(critquery).getSingleResultOrNull();
        transaction.commit();
        return user;
    }

    public UserBean register(final String username, final String password) {
        user = new UserBean();
        user.setName(username);
        user.setPassword(password);

        if (!validator.validate(user).isEmpty())
            return null;

        PersistenceHelper.require(session).saveOrUpdate(user);

        return user;
    }

    public void logOut() {
        user = null;
    }

    public UserBean getUser() {
        if (user != null) {
            final Session currentSession = PersistenceHelper.require(session).getSessionFactory().getCurrentSession();
            currentSession.beginTransaction();
            user = currentSession.get(UserBean.class, user.getId());
            currentSession.getTransaction().commit();
        }

        return user;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("user", getUser());
        request.getRequestDispatcher("../views/login.jsp").include(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String name = request.getParameter("name");
        final String password = request.getParameter("password");

        final UserBean user = new UserBean();
        user.setName(name);
        user.setPassword(password);
        final Set<ConstraintViolation<UserBean>> violations = validator.validate(user);

        if (violations.isEmpty())
            if (logIn(name, password) == null)
                request.setAttribute("error", "Anmeldedaten sind falsch!");

        request.setAttribute("violations", violations);


        doGet(request, response);
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logOut();

        response.sendRedirect(".");
    }
}

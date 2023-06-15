package de.whs.ni37900.ina.praktikum.inawebapp.services.hibernate;

import de.whs.ni37900.ina.praktikum.inawebapp.services.HelperBase;
import de.whs.ni37900.ina.praktikum.inawebapp.services.user.AuthHelper;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class PersistenceHelper extends HelperBase {

	private static SessionFactory sessionFactory;

	public static PersistenceHelper require(final HttpSession session) {
		return HelperBase.require(session, "PersistenceHelper", PersistenceHelper::new);
	}

	public PersistenceHelper(final HttpSession session) {
		super(session);
	}

	private SessionFactory createSessionFactory() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
			StandardServiceRegistryBuilder.destroy(registry);
		}
		return sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		if(sessionFactory == null)
			sessionFactory = createSessionFactory();

		return sessionFactory;
	}

	public void closeSessionFactory() {
		if (sessionFactory != null)
			sessionFactory.close();
		sessionFactory = null;
	}

	public void saveOrUpdate(Object bean) {
		Session hib_session = getSessionFactory().getCurrentSession();
		hib_session.beginTransaction();
		
		hib_session.persist(bean);
		
		hib_session.getTransaction().commit();
		hib_session.close();
		closeSessionFactory();
	}

	public <BeanType> List<BeanType> obtainAll(final Class<BeanType> beanTypeClass) {
		Session hib_session = getSessionFactory().getCurrentSession();
		hib_session.beginTransaction();

		CriteriaQuery<BeanType> critquery = hib_session.getCriteriaBuilder().createQuery(beanTypeClass);
		Root<BeanType> root = critquery.from(beanTypeClass);
		critquery.select(root);
		List<BeanType> dbresults = hib_session.createQuery(critquery).getResultList();
		
		hib_session.getTransaction().commit();
		hib_session.close();
		closeSessionFactory();
		return dbresults;
	}
}
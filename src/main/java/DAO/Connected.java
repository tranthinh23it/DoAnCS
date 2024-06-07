package DAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;

public class Connected {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        Connected.sessionFactory = sessionFactory;
    }

    public static void connection(Class<?> annotatedClass){
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(annotatedClass);

        sessionFactory = configuration.buildSessionFactory();

    }
}

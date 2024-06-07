package DAO;


import DTO.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class Hibernate {
    public static void main( String[] args )
    {
        // Create configuration instance and load hibernate.cfg.xml
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Users.class);

        // Create session factory
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        // Open a session
        Session session = sessionFactory.openSession();

        // Begin transaction
        session.beginTransaction();

//        // Fetch data from Customers table
//        List<Users> users = session.createQuery("select c from Users c", Users.class).list();
//
//        // Print fetched data
//        for (Users user : users) {
//            System.out.println(" ID: " + books.getBookId());
//            System.out.println("Title: " + books.getTitle());
//            System.out.println("Author: " + books.getAuthor());
//            System.out.println("Price: " + books.getPrice());
//            System.out.println("-----------------------");
//            // Add more fields as necessary
//        }

        Users users = new Users();
        users.setPassword("123");
        users.setFullName("Tran Thinh");
        users.setUsername("thinhdb123");
        users.setEmail("ggg@gma.com");


        session.save(users);

//        String hql = "UPDATE Employee set salary = :salary "  +
//                "WHERE id = :employee_id";
//        Query = session.createQuery(hql);
//        query.setParameter("salary", 1000);
//        query.setParameter("employee_id", 10);
//        int result = query.executeUpdate();

//        Query query = session.createQuery("UPDATE Users SET fullName = :name WHERE username = :username");
//        query.setParameter("name", "Tran Quang Viet");
//        query.setParameter("username", "thinh123");


        // Commit transaction
        session.getTransaction().commit();

        // Close session and session factory
        session.close();
        sessionFactory.close();
    }
}

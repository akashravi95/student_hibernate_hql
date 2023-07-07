package service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import model.Students;

public class Crudoperation {
    private SessionFactory sessionFactory;

    public Crudoperation() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
                .configure("service/Hibernate.cfg.xml")
                .build();
        Metadata metadata = new MetadataSources(ssr)
                .getMetadataBuilder()
                .build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    public int save(Students stud) {
        int status = 0;

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(stud);
            transaction.commit();
            status = 1;
        } catch (Exception e) {
            System.out.println(e);
        }
        return status;
    }

    public List<Students> getAllStudents() {
        List<Students> studentsList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Students> query = session.createQuery("FROM Students", Students.class);
            studentsList = query.list();
        } catch (Exception e) {
            System.out.println(e);
        }
        return studentsList;
    }

    public int login(String username, String password) {
        int userId = -1;
        try (Session session = sessionFactory.openSession()) {
            Query<Integer> query = session.createQuery("SELECT id FROM Students WHERE username = :username AND password = :password", Integer.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            List<Integer> resultList = query.getResultList();
            if (!resultList.isEmpty()) {
                userId = resultList.get(0);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return userId;
    }

    public Students getUserInfo(int userId) {
        Students user = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Students> query = session.createQuery("FROM Students WHERE id = :userId", Students.class);
            query.setParameter("userId", userId);
            user = query.uniqueResult();
        } catch (Exception e) {
            System.out.println(e);
        }
        return user;
    }

    public int editUser(Students stud) {
        int status = 0;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<?> query = session.createQuery("UPDATE Students SET name = :name, age = :age, username = :username, password = :password, mail_id = :mail_id WHERE id = :id");
            query.setParameter("name", stud.getName());
            query.setParameter("age", stud.getAge());
            query.setParameter("username", stud.getUsername());
            query.setParameter("password", stud.getPassword());
            query.setParameter("mail_id", stud.getMail_id());
            query.setParameter("id", stud.getId());
            query.executeUpdate();
            transaction.commit();

            status = 1;

        } catch (Exception e) {
            System.out.println(e);
        }
        return status;
    }

    public int deleteUser(int userId) {
        int status = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<?> query = session.createQuery("DELETE FROM Students WHERE id = :userId");
            query.setParameter("userId", userId);
            query.executeUpdate();

            transaction.commit();
            status = 1;

        } catch (Exception e) {
            System.out.println(e);
        }
        return status;
    }
}

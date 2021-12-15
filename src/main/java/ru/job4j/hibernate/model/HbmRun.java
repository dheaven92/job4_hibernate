package ru.job4j.hibernate.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.function.Consumer;

public class HbmRun {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure()
            .build();
    private final SessionFactory sessionFactory = new MetadataSources(registry)
            .buildMetadata()
            .buildSessionFactory();

    public static void main(String[] args) {
        new HbmRun().runExampleManyToMany();
    }

    private void runExampleManyToMany() {
        executeTransaction(session -> {
            Author author1 = Author.of("Neil Gaiman");
            Author author2 = Author.of("Terry Pratchett");
            Author author3 = Author.of("Samantha van Leer");
            Book book1 = Book.of("Good Omens");
            Book book2 = Book.of("The Sandman: Book of Dreams");
            Book book3 = Book.of("Between the Lines");
            Book book4 = Book.of("Discworld");
            author1.addBook(book1);
            author1.addBook(book2);
            author2.addBook(book1);
            author2.addBook(book4);
            author3.addBook(book3);
            session.persist(author1);
            session.persist(author2);
            session.persist(author3);
            Author author1InDb = session.find(Author.class, 1);
            session.remove(author1InDb);
        });
    }

    private void runExampleOneToMany() {
        executeTransaction(session -> {
            List<CarModel> models = List.of(
                    CarModel.of("Spark"),
                    CarModel.of("Tahoe"),
                    CarModel.of("Traverse"),
                    CarModel.of("Cobalt"),
                    CarModel.of("Nexia")
            );
            CarBrand brand = CarBrand.of("Chevrolet");
            models.forEach(brand::addCarModel);
            session.save(brand);
        });
    }

    private void executeTransaction(final Consumer<Session> command) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        try {
            command.accept(session);
            transaction.commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}



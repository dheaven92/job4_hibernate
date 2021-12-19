package ru.job4j.hibernate.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.Collection;
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
        new HbmRun().runHqlExample();
    }

    private void runExampleBiderectedOneToMany() {
        executeTransaction(session -> {
            CarBrand chevrolet = CarBrand.of("Chevrolet");
            List<CarModel> chevroletModels = List.of(
                    CarModel.of("Spark", chevrolet),
                    CarModel.of("Tahoe", chevrolet),
                    CarModel.of("Traverse", chevrolet),
                    CarModel.of("Cobalt", chevrolet),
                    CarModel.of("Nexia", chevrolet)
            );
            chevroletModels.forEach(chevrolet::addCarModel);
            session.save(chevrolet);

            CarBrand nissan = CarBrand.of("Nissan");
            List<CarModel> nissanModels = List.of(
                    CarModel.of("Qashqai", nissan),
                    CarModel.of("Murano", nissan)
            );
            nissanModels.forEach(nissan::addCarModel);
            session.save(nissan);
        });
        /* in the same transaction */
        executeTransaction(session -> {
            List<CarBrand> brands = session.createQuery("from CarBrand").list();
            brands.stream()
                    .map(CarBrand::getCarModels)
                    .flatMap(Collection::stream)
                    .forEach(model -> System.out.println(model.getName()));
        });
        /* join fetch */
        List<CarBrand> brands = new ArrayList<>();
        executeTransaction(session -> {
            List list = session.createQuery(
                    "select distinct brand from CarBrand brand join fetch brand.carModels"
            ).list();
            brands.addAll(list);
        });
        brands.stream()
                .map(CarBrand::getCarModels)
                .flatMap(Collection::stream)
                .forEach(model -> System.out.println(model.getName()));
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

    private void runHqlExample() {
        executeTransaction(session -> {
            /* select all */
            session.createQuery("from Candidate candidate").list();

            /* select by id */
            session.createQuery("from Candidate candidate where id = :id")
                    .setParameter("id", 1)
                    .uniqueResult();

            /* select by name */
            session.createQuery("from Candidate candidate where name = :name")
                    .setParameter("name", "Bob")
                    .list();

            /* update */
            session.createQuery(
                    "update Candidate candidate set name = :name, experience = :experience, salary = :salary where id = :id"
            )
                    .setParameter("id", 1)
                    .setParameter("name", "Bob The Greatest")
                    .setParameter("experience", 10)
                    .setParameter("salary", 1000000.0)
                    .executeUpdate();

            /* delete */
            session.createQuery("delete Candidate candidate where id = :id")
                    .setParameter("id", 2)
                    .executeUpdate();
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



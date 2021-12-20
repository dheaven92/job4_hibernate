package ru.job4j.hibernate.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrdersStoreTest {

    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/integration_schema.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Statement st = getConnection().createStatement()) {
            st.executeUpdate(builder.toString());
        }
    }

    @After
    public void reset() throws SQLException {
        try (Statement st = getConnection().createStatement()) {
            st.executeUpdate("DROP TABLE orders");
        }
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name", "description"));
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderThenUpdateAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);
        Order order = store.save(Order.of("name", "description"));
        order.setName("name updated");
        order.setDescription("description updated");
        boolean updated = store.update(order);
        List<Order> all = (List<Order>) store.findAll();
        assertThat(updated, is(Boolean.TRUE));
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getName(), is("name updated"));
        assertThat(all.get(0).getDescription(), is("description updated"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderThenFindById() {
        OrdersStore store = new OrdersStore(pool);
        Order order = store.save(Order.of("name", "description"));
        Order foundOrder = store.findById(order.getId());
        assertThat(foundOrder.getName(), is("name"));
        assertThat(foundOrder.getDescription(), is("description"));
        assertThat(foundOrder.getId(), is(1));
    }

    @Test
    public void whenSaveOrdersThenFindByName() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name", "description"));
        store.save(Order.of("another name", "description"));
        List<Order> foundOrders = store.findByName("name");
        assertThat(foundOrders.size(), is(2));
        assertThat(foundOrders.get(0).getName(), is("name"));
        assertThat(foundOrders.get(1).getName(), is("another name"));
        assertThat(foundOrders.get(0).getId(), is(1));
        assertThat(foundOrders.get(1).getId(), is(2));
    }

    private Connection getConnection() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        return pool.getConnection();
    }
}
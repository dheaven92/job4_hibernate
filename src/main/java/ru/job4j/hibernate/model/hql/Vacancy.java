package ru.job4j.hibernate.model.hql;

import javax.persistence.*;

@Entity
@Table(name = "vacancy")
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private double salary;

    @ManyToOne
    @JoinColumn(name = "vacancy_bank_id")
    private VacancyBank vacancyBank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public VacancyBank getVacancyBank() {
        return vacancyBank;
    }

    public void setVacancyBank(VacancyBank vacancyBank) {
        this.vacancyBank = vacancyBank;
    }

    @Override
    public String toString() {
        return "Vacancy{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", salary=" + salary
                + '}';
    }
}

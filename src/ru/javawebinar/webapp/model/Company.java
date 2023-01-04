package ru.javawebinar.webapp.model;

import java.util.List;
import java.util.Objects;

public class Company {
    private final String website;
    private final String name;
    private final List<Period> periods;

    public Company(String website, String name, List<Period> period) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(period, "period must not be null");
        this.website = website;
        this.name = name;
        this.periods = period;
    }

    public String getWebsite() {
        return website;
    }

    public String getName() {
        return name;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (!Objects.equals(website, company.website)) return false;
        if (!name.equals(company.name)) return false;
        return periods.equals(company.periods);
    }

    @Override
    public int hashCode() {
        int result = website != null ? website.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + periods.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Company{" +
                "website='" + website + '\'' +
                ", name='" + name + '\'' +
                ", period=" + periods +
                '}';
    }
}

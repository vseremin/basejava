package ru.javawebinar.webapp.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CompanySection extends AbstractSection {
    public static final long serialVersionUID = 1L;
    private final List<Company> companies;

    public CompanySection(Company company) {
        this(Arrays.asList(company));
    }

    public CompanySection(List<Company> company) {
        Objects.requireNonNull(company, "company must not be null");
        this.companies = company;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanySection that = (CompanySection) o;

        return Objects.equals(companies, that.companies);
    }

    @Override
    public int hashCode() {
        return companies != null ? companies.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "company=" + companies + "\n";
    }
}

package ru.javawebinar.webapp.model;

import java.util.List;

public class CompanySection extends AbstractSection {
    private final List<Company> companies;

    public CompanySection(List<Company> company) {
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

        return companies.equals(that.companies);
    }

    @Override
    public int hashCode() {
        return companies.hashCode();
    }

    @Override
    public String toString() {
        return "company=" + companies + "\n";
    }
}

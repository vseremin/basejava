package ru.javawebinar.webapp.model;

import java.util.List;

public class Company {
    private final String website;
    private final String name;
    private final String post;
    private final List<Period> period;

    public Company(String website, String name, String post, List<Period> period) {
        this.website = website;
        this.name = name;
        this.post = post;
        this.period = period;
    }

    public String getWebsite() {
        return website;
    }

    public String getName() {
        return name;
    }

    public String getPost() {
        return post;
    }

    public List<Period> getPeriod() {
        return period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (!website.equals(company.website)) return false;
        if (!name.equals(company.name)) return false;
        if (!post.equals(company.post)) return false;
        return period.equals(company.period);
    }

    @Override
    public int hashCode() {
        int result = website.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + post.hashCode();
        result = 31 * result + period.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Company{" +
                "website='" + website + '\'' +
                ", name='" + name + '\'' +
                ", post='" + post + '\'' +
                ", period=" + period +
                '}';
    }
}

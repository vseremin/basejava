package ru.javawebinar.webapp.web;

import ru.javawebinar.webapp.model.Company;
import ru.javawebinar.webapp.model.CompanySection;
import ru.javawebinar.webapp.model.Resume;
import ru.javawebinar.webapp.model.SectionType;
import ru.javawebinar.webapp.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class UtilForWeb {
    public static String getSectionToString(List<String> list) {
        return String.join("\n", list);
    }

    public static List<String> tarnsformValueToList(String value) {
        return Stream.of(value.split("\r\n")).filter(p -> !p.trim().equals("")).toList();
    }

    public static List<Company> transformValueToCompany(String value, HttpServletRequest request) {
        List<Company> companies = new ArrayList<>();
        String[] values = request.getParameterValues(value);
        for (int i = 0; i < values.length; i++) {
            String website = request.getParameter(value + "website_" + i);
            List<Company.Period> periods = new ArrayList<>();
            String[] startDates = request.getParameterValues(value + "startData_" + i);
            String[] endDates = request.getParameterValues(value + "endData_" + i);
            String[] titles = request.getParameterValues(value + "title_" + i);
            String[] descriptions = request.getParameterValues(value + "description_" + i);
            for (int j = 0; j < startDates.length; j++) {
                try {
                    LocalDate startData = DateUtil.parse(startDates[j]);
                    LocalDate endData = DateUtil.parse(endDates[j]);
                    if (startData != null && endData != null) {
                        if (!titles[j].trim().equals("")) {
                            periods.add(new Company.Period(startData, endData, titles[j], descriptions[j]));
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (!website.trim().equals("") || !values[i].trim().equals("") || periods.size() > 0) {
                companies.add(new Company(website, values[i], periods));
            }
        }
        return companies;
    }

    public static List<Company> getCompany(Resume r, SectionType type) {
        List<Company> listCompany = new ArrayList<>();
        listCompany.add(new Company());
        if (r.getSection().get(type) != null) {
            if (((CompanySection) r.getSection().get(type)).getCompanies() != null) {
                listCompany.addAll(((CompanySection) r.getSection().get(type)).getCompanies());
            }
        }
        return listCompany;
    }

    public static List<Company.Period> getPeriod(Company company) {
        List<Company.Period> listPeriod = company.getPeriods() != null ?
                company.getPeriods() : new ArrayList<>();
        listPeriod.add(new Company.Period());
        return listPeriod;
    }

    public static String convertDataToString(Company.Period period) {
        return DateUtil.format(period.getStartDate()) + " - " + DateUtil.format(period.getEndDate());

    }
}

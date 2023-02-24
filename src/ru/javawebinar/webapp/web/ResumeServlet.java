package ru.javawebinar.webapp.web;

import ru.javawebinar.webapp.Config;
import ru.javawebinar.webapp.model.*;
import ru.javawebinar.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResumeServlet extends HttpServlet {
    private Resume newResume;
    public static Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.getInstance().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "view", "edit" -> r = storage.get(uuid);
            case "add" -> {
                r = new Resume();
                r.setFullName("");
                newResume = r;
            }
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        if (storage.getAllSorted().stream().map(Resume::getUuid).noneMatch(uuid::equals)) {
            storage.save(newResume);
        }

        Resume r = storage.get(uuid);
        r.setFullName(fullName);
        for (Contacts type : Contacts.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (type == SectionType.EDUCATION || type == SectionType.EXPERIENCE) {
                String[] values = request.getParameterValues(type.name());
                for (int i = 0; i < values.length && value != null; i++) {
                    value = values[i];
                }
            }
            if (value != null && value.trim().length() != 0) {
                r.addSections(type, switch (type) {
                    case PERSONAL, OBJECTIVE -> new TextSection(value);
                    case ACHIEVEMENT, QUALIFICATIONS -> new ListSection(UtilForWeb.tarnsformValueToList(value));
                    case EDUCATION, EXPERIENCE ->
                            new CompanySection(UtilForWeb.transformValueToCompany(type.name(), request));
                });
            } else {
                r.getSection().remove(type);
            }
        }
        storage.update(r);
        response.sendRedirect("resume");
    }
}

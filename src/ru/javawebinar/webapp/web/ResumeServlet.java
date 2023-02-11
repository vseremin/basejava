package ru.javawebinar.webapp.web;

import ru.javawebinar.webapp.Config;
import ru.javawebinar.webapp.model.Resume;
import ru.javawebinar.webapp.storage.SqlStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    public static SqlStorage sqlStorage = new SqlStorage(Config.getInstance().getDbUrl(),
                Config.getInstance().getDbUser(), Config.getInstance().getDbPassword());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String uuid = request.getParameter("uuid");
        response.getWriter().write("""
                <html>
                <title>Resume</title>
                <body>
                <center>
                <table border = "1">
                <tr>
                <th>Uuid</th>
                <th>Full Name</th>
                </tr>
                """);

        List<Resume> resumes = sqlStorage.getAllSorted();
        if (uuid != null && resumes.stream().anyMatch(p -> p.getUuid().equals(uuid))) {
            response.getWriter().write( "<td>" + "<center>" + uuid + "</center>" + "</td>\n" +
                    "<td>" + "<center>" + resumes.stream().filter(p -> p.getUuid().equals(uuid))
                    .findFirst().get().getFullName() + "</center>" + "</td>\n");
        } else {
            for (Resume resume : resumes) {
                response.getWriter().write("<tr>\n" +
                        "<td height = 50>" + "<center>" + resume.getUuid() + "</center>" + "</td>\n" +
                        "<td>" + "<center>" + resume.getFullName() + "</center>" + "</td>\n" +
                        "</tr>\n");
            }
        }
        response.getWriter().write("""
                </center>
                </table>
                </body>
                </html>""");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

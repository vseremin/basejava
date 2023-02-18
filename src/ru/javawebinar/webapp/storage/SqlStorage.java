package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.model.*;
import ru.javawebinar.webapp.sql.SqlHelper;
import ru.javawebinar.webapp.util.JsonParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
            sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        sqlHelper.connecting("DELETE FROM resume", p -> {
            p.execute();
            return null;
        });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = (?) WHERE uuid = (?)")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            deleteItems(conn, resume.getUuid(), "DELETE FROM contact WHERE resume_uuid = ?");
            saveContacts(conn, resume);
            deleteItems(conn, resume.getUuid(), "DELETE FROM section WHERE resume_uuid = ?");
            saveSectionsJson(conn, resume);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume " +
                            "(full_name, uuid) VALUES (?,?)")) {
                        ps.setString(1, r.getFullName());
                        ps.setString(2, r.getUuid());
                        ps.execute();
                    }
                    saveContacts(conn, r);
                    saveSectionsJson(conn, r);
                    return null;
                }
        );
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume resume = getResult(conn, uuid, "SELECT * FROM resume WHERE uuid = ? ", o -> {
                if (!(o.next())) {
                    throw new NotExistStorageException(uuid);
                }
                return new Resume(o.getString(2), uuid);
            });
            getResult(conn, uuid, "SELECT * FROM contact WHERE resume_uuid = ?", o -> {
                addContacts(o, resume);
                return null;
            });
            getResult(conn, uuid, "SELECT * FROM section WHERE resume_uuid = ?", o -> {
                addSectionsJson(o, resume);
                return null;
            });
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.connecting("DELETE FROM resume WHERE uuid =?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumes.add(new Resume(rs.getString(2), rs.getString(1)));
                }
            }
            for (Resume resume : resumes) {
                getResult(conn, resume.getUuid(), "SELECT * FROM contact WHERE resume_uuid = ?", o -> {
                    addContacts(o, resume);
                    return null;
                });
                getResult(conn, resume.getUuid(), "SELECT * FROM section WHERE resume_uuid = ?", o -> {
                    addSectionsJson(o, resume);
                    return null;
                });
            }
            return null;
        });
        return resumes;
    }

    @Override
    public int size() {
        return sqlHelper.connecting("SELECT COUNT(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    private void saveContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact " +
                "(resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<Contacts, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section " +
                "(resume_uuid, type_section, value_section) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : r.getSection().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, (e.getKey()).name());
                ps.setString(3, getSection(e.getValue()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveSectionsJson(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section " +
                "(resume_uuid, type_section, value_section) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : r.getSection().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, (e.getKey()).name());
                AbstractSection section = e.getValue();
                ps.setString(3, JsonParser.write(section, AbstractSection.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private <T> String getSection(T section) {
        return switch (section.getClass().getSimpleName()) {
            case "String" -> (String) section;
            case "TextSection" -> ((TextSection) section).getText();
            case "ListSection" -> String.join("\n", ((ListSection) section).getList());
            default -> "";
        };
    }

    private void addContacts(ResultSet rs, Resume resume) throws SQLException {
        while (rs.next()) {
            String value = rs.getString("value");
            if (value != null) {
                Contacts type = Contacts.valueOf(rs.getString("type"));
                resume.addContact(type, value);
            }
        }
    }

    private void addSections(ResultSet rs, Resume resume) throws SQLException {
        while (rs.next()) {
            String value = rs.getString("value_section");
            if (value != null) {
                String stringType = rs.getString("type_section");
                if (Arrays.stream(SectionType.values()).anyMatch((t) -> t.name().equals(stringType))) {
                    SectionType type = SectionType.valueOf(stringType);
                    resume.addSections(type, type.name().equals("PERSONAL") || type.name().equals("OBJECTIVE")
                            ? new TextSection(value) : new ListSection(value.split("\n")));
                }
            }
        }
    }

    private void addSectionsJson(ResultSet rs, Resume resume) throws SQLException {
        while (rs.next()) {
            String value = rs.getString("value_section");
            if (value != null) {
                SectionType type = SectionType.valueOf(rs.getString("type_section"));
                resume.addSections(type, JsonParser.read(value, AbstractSection.class));
            }
        }
    }

    private <T> T getResult(Connection conn, String uuid, String sqlQuery, Function<T> func) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            return func.getItem(rs);
        }
    }

    private void deleteItems(Connection conn, String uuid, String query) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }

    interface Function<T> {
        T getItem(ResultSet r) throws SQLException;
    }
}

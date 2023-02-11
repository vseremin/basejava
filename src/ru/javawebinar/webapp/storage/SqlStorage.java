package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.model.*;
import ru.javawebinar.webapp.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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
            if (saveName("UPDATE resume SET full_name = (?) WHERE uuid = (?)", conn, resume) == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
            deleteItems(conn, resume.getUuid(), "DELETE FROM contact WHERE resume_uuid = ?");
            saveSection(conn, resume, resume.getContacts().entrySet(), "INSERT INTO contact " +
                    "(resume_uuid, type, value) VALUES (?,?,?)");
            deleteItems(conn, resume.getUuid(), "DELETE FROM section WHERE resume_uuid = ?");
            saveSection(conn, resume, resume.getSection().entrySet(), "INSERT INTO section " +
                    "(resume_uuid, type_section, value_section) VALUES (?,?,?)");
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    saveName("INSERT INTO resume (full_name, uuid) VALUES (?,?)", conn, r);
                    saveSection(conn, r, r.getContacts().entrySet(), "INSERT INTO contact " +
                            "(resume_uuid, type, value) VALUES (?,?,?)");
                    saveSection(conn, r, r.getSection().entrySet(), "INSERT INTO section " +
                            "(resume_uuid, type_section, value_section) VALUES (?,?,?)");
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
                addItem(o, resume, "value", "type");
                return null;
            });
            getResult(conn, uuid, "SELECT * FROM section WHERE resume_uuid = ?", o -> {
                addItem(o, resume, "value_section", "type_section");
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
                    addItem(o, resume, "value", "type");
                    return null;
                });
                getResult(conn, resume.getUuid(), "SELECT * FROM section WHERE resume_uuid = ?", o -> {
                    addItem(o, resume, "value_section", "type_section");
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

    private <K, V> void saveSection(Connection conn, Resume r, Set<Map.Entry<K, V>> entry, String query) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            for (Map.Entry<K, V> e : entry) {
                ps.setString(1, r.getUuid());
                ps.setString(2, ((Enum) e.getKey()).name());
                ps.setString(3, getSection(e.getValue()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private <T> String getSection(T section) {
        if (section instanceof TextSection) {
            return ((TextSection) section).getText();
        }
        if (section instanceof String) {
            return (String) section;
        }
        return ((ListSection) section).getList()
                .stream().map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

    private int saveName(String query, Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            return ps.executeUpdate();
        }
    }

    private void addItem(ResultSet rs, Resume resume, String columnValue, String columnType) throws SQLException {
        while (rs.next()) {
            String value = rs.getString(columnValue);
            if (value != null) {
                String stringType = rs.getString(columnType);
                if (Arrays.stream(SectionType.values()).anyMatch((t) -> t.name().equals(stringType))) {
                    SectionType type = SectionType.valueOf(stringType);
                    resume.addSections(type, type.name().equals("PERSONAL") || type.name().equals("OBJECTIVE")
                            ? new TextSection(value) : new ListSection(value.split("\n")));
                } else if (Arrays.stream(Contacts.values()).anyMatch((t) -> t.name().equals(stringType))) {
                    Contacts type = Contacts.valueOf(stringType);
                    resume.addContact(type, value);
                }
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

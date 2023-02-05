package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.model.Contacts;
import ru.javawebinar.webapp.model.Resume;
import ru.javawebinar.webapp.sql.SqlHelper;

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
            if (saveName("UPDATE resume SET full_name = (?) WHERE uuid = (?)", conn, resume) == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
                ps.setString(1, resume.getUuid());
                ps.execute();
            }
            saveContact(conn, resume);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    saveName("INSERT INTO resume (full_name, uuid) VALUES (?,?)", conn, r);
                    saveContact(conn, r);
                    return null;
                }
        );
    }


    @Override
    public Resume get(String uuid) {
        return sqlHelper.connecting("" +
                        "                    SELECT * FROM resume r " +
                        "                 LEFT JOIN contact c " +
                        "                        ON r.uuid = c.resume_uuid " +
                        "                     WHERE r.uuid = ? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(rs.getString("full_name"), uuid);
                    addContact(rs, r);
                    return r;
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
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid = ?")) {
                    ps.setString(1, resume.getUuid());
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    addContact(rs, resume);
                }
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

    private void saveContact(Connection conn, Resume r) throws SQLException {
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

    private int saveName(String query, Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            return ps.executeUpdate();
        }
    }

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        do {
            String value = rs.getString("value");
            if (value != null) {
                Contacts type = Contacts.valueOf(rs.getString("type"));
                resume.addContact(type, value);
            }
        } while (rs.next());
    }
}

package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.model.Contacts;
import ru.javawebinar.webapp.model.Resume;
import ru.javawebinar.webapp.sql.SqlHelper;

import java.sql.*;
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
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume " +
                    "SET full_name = (?) WHERE uuid = (?)")) {
                if (saveName(ps, resume) == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
                ps.setString(1, resume.getUuid());
                ps.execute();
            }
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact " +
                    "(resume_uuid, type, value) VALUES (?,?,?)")) {
                saveContact(ps, resume);
            }

            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume " +
                            "(full_name, uuid) VALUES (?,?)")) {
                        saveName(ps, r);
                    }
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact " +
                            "(resume_uuid, type, value) VALUES (?,?,?)")) {
                        saveContact(ps, r);
                    }
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
                    return addResume(rs, uuid);
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
        sqlHelper.connecting("SELECT * FROM resume",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        resumes.add(get(rs.getString(1)));
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

    private void saveContact(PreparedStatement ps, Resume r) throws SQLException {
        for (Map.Entry<Contacts, String> e : r.getContacts().entrySet()) {
            ps.setString(1, r.getUuid());
            ps.setString(2, e.getKey().name());
            ps.setString(3, e.getValue());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    private int saveName(PreparedStatement ps, Resume r) throws SQLException {
        ps.setString(1, r.getFullName());
        ps.setString(2, r.getUuid());
        return ps.executeUpdate();
    }

    private Resume addResume(ResultSet rs, String uuid) throws SQLException {
        Resume r = new Resume(rs.getString("full_name"), uuid);
        do {
            String value = rs.getString("value");
            if (value != null) {
                r.addContact(Contacts.valueOf(rs.getString("type")), value);
            }
        } while (rs.next());
        return r;
    }
}

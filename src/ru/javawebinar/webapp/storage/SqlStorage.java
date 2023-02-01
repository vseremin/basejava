package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;
import ru.javawebinar.webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        try {
            connecting("DELETE FROM resume", p -> {
                p.execute();
                return null;
            });
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void update(Resume resume) {
        get(resume.getUuid());
        delete(resume.getUuid());
        save(resume);
    }

    @Override
    public void save(Resume r) {
        try {
            connecting("INSERT INTO resume (full_name, uuid) VALUES (?, ?)", p -> {
                p.setString(1, r.getFullName());
                p.setString(2, r.getUuid());
                p.execute();
                return null;
            });
        } catch (SQLException e) {
            throw new ExistStorageException(r.getUuid());
        }
    }

    @Override
    public Resume get(String uuid) {
        try {
            return (Resume) connecting("SELECT * FROM resume r WHERE  r.uuid =?", ps -> {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                return new Resume(rs.getString("full_name"), uuid);
            });
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(String uuid) {
        get(uuid);
        try {
            connecting("DELETE FROM resume WHERE uuid =?", ps -> {
                ps.setString(1, uuid);
                ps.execute();
                return null;
            });
        } catch (SQLException ex) {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        try {
            return (List<Resume>) connecting("SELECT * FROM resume", ps -> {
                ResultSet rs = ps.executeQuery();
                List<Resume> resumes = new ArrayList<>();
                while (rs.next()) {
                    resumes.add(new Resume(rs.getString(2), rs.getString(1).trim()));
                }
                return resumes;

            });
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public int size() {
        return getAllSorted().size();
    }

    private Object connecting(String query, Strategy strategy) throws SQLException {
        Object o;
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            o = strategy.execute(ps);
        }
        return o;
    }

    public interface Strategy<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }
}

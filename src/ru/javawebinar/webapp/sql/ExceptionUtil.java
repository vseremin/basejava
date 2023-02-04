package ru.javawebinar.webapp.sql;

import org.postgresql.util.PSQLException;
import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.StorageException;

import java.sql.SQLException;

public class ExceptionUtil {
    private ExceptionUtil() {}

    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException) {
            if (e.getSQLState().equals("23505")) {
                throw new ExistStorageException(e);
            }
        }
        return  new StorageException(e);
    }
}

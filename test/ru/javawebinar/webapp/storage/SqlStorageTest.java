package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.Config;

public class SqlStorageTest extends AbstractStorageTest {
    private static final Storage storage = new SqlStorage(Config.getInstance().getDbUrl(),
            Config.getInstance().getDbUser(), Config.getInstance().getDbPassword());

    public SqlStorageTest() {
        super(storage);
    }
}

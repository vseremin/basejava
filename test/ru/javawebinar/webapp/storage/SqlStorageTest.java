package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.Config;

public class SqlStorageTest extends AbstractStorageTest {
    private static final Storage storage = Config.getInstance().getStorage();

    public SqlStorageTest() {
        super(storage);
    }
}

package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.storage.serializer.DataStreamSerializer;

public class DataPathStorageTest extends AbstractStorageTest {

    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_STRING_DIR, new DataStreamSerializer()));
    }
}
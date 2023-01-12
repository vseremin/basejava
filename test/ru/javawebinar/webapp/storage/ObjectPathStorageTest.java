package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.storage.serializer.SerializerOption;

public class ObjectPathStorageTest extends AbstractStorageTest {

    public ObjectPathStorageTest() {
        super(new PathStorage(STORAGE_STRING_DIR, new SerializerOption()));
    }
}
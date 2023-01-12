package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.storage.serializer.SerializerOption;

public class ObjectStreamStorageTest extends AbstractStorageTest {

    public ObjectStreamStorageTest() {
        super(new FileStorage(STORAGE_DIR, new SerializerOption()));
    }

}
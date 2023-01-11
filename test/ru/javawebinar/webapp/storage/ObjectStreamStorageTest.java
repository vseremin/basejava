package ru.javawebinar.webapp.storage;

public class ObjectStreamStorageTest extends AbstractStorageTest {

    public ObjectStreamStorageTest() {
        super(new AbstractFileStorage(STORAGE_DIR, new ObjectStorage()));
    }

}
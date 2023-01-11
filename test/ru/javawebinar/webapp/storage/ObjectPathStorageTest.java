package ru.javawebinar.webapp.storage;

public class ObjectPathStorageTest extends AbstractStorageTest {

    public ObjectPathStorageTest() {
        super(new AbstractPathStorage(STORAGE_STRING_DIR, new ObjectStorage()));
    }
}
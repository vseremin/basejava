package ru.javawebinar.webapp.storage;

public class ObjectPathStorageTest extends AbstractStorageTest {

    public ObjectPathStorageTest() {
        super(new ObjectPathStorage(STORAGE_STRING_DIR));
    }
}
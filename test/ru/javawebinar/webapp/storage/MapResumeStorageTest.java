package ru.javawebinar.webapp.storage;

public class MapResumeStorageTest extends AbstractStorageTest {

    private static final Storage storage = new MapResumeStorage();

    public MapResumeStorageTest() {
        super(storage);
    }

}
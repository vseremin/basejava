package ru.javawebinar.webapp.storage;

public class MapStorageTest extends AbstractStorageTest {

    private static final Storage storage = new MapStorage();

    public MapStorageTest() {
        super(storage);
    }

}
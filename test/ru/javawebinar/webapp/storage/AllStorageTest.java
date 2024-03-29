package ru.javawebinar.webapp.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ArrayStorageTest.class,
    SortedArrayStorageTest.class,
    ListStorageTest.class,
    MapStorageTest.class,
    MapResumeStorageTest.class,
    ObjectStreamStorageTest.class,
    ObjectPathStorageTest.class,
    XmlPathStorageTest.class,
    JsonPathStorageTest.class,
    DataPathStorageTest.class,
    SqlStorageTest.class
})
public class AllStorageTest {
}

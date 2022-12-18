package ru.javawebinar.webapp.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;


public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    private static Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
        AbstractArrayStorageTest.storage = storage;
    }

    @Test(expected = StorageException.class)
    public void saveOverFlow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (Exception exception) {
            Assert.fail("Overflow");
        }
        storage.save(new Resume());
    }
}
package ru.javawebinar.webapp.storage;

import org.junit.Test;
import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.model.Resume;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {
    private static final Storage storage = new SortedArrayStorage();
    public SortedArrayStorageTest() {
        super(storage);
    }

    @Test(expected = ExistStorageException.class)
    public void addResumeExist() {
        storage.save(new Resume(AbstractArrayStorageTest.UUID_1));
    }

}
package ru.javawebinar.webapp.storage;

import org.junit.Test;
import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.model.Resume;

import static org.junit.Assert.assertEquals;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {
    private static final Storage storage = new SortedArrayStorage();
    public SortedArrayStorageTest() {
        super(storage);
    }

    @Test(expected = ExistStorageException.class)
    public void addResumeExist() {
        storage.save(new Resume(AbstractArrayStorageTest.UUID_1));
    }

    @Test
    public void addResume() {
        int startSize = storage.size();
        storage.save(new Resume("uuid5"));
        storage.save(new Resume("uuid6"));
        int endSize = storage.size();
        assertEquals(endSize, startSize + 2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteResumeNotExist() {
        storage.delete("???");
    }

    @Test
    public void deleteResume() {
        storage.delete(AbstractArrayStorageTest.UUID_1);
        storage.delete(AbstractArrayStorageTest.UUID_2);
        assertEquals(1, storage.size());
    }
}
package ru.javawebinar.webapp.storage;

import org.junit.Test;
import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.model.Resume;

import static org.junit.Assert.assertEquals;

public class ArrayStorageTest extends AbstractArrayStorageTest {

    private static final Storage storage = new ArrayStorage();
    
    public ArrayStorageTest() {
        super(storage);
    }
    @Test(expected = ExistStorageException.class)
    public void addResumeExist() {
        storage.save(new Resume(AbstractArrayStorageTest.UUID_1));
    }

    @Test
    public void addResume() {
        storage.save(new Resume("uuid5"));
        assertEquals(4, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteResumeNotExist() {
        storage.delete("No");
    }

    @Test
    public void deleteResume() {
        storage.delete(AbstractArrayStorageTest.UUID_1);
        assertEquals(2, storage.size());
    }
}

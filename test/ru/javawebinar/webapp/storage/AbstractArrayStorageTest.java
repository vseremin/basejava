package ru.javawebinar.webapp.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;

import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest {
    private static Storage storage;
    protected static final String UUID_1 = "uuid1";
    private static final Resume RESUME_1 = new Resume(UUID_1);
    protected static final String UUID_2 = "uuid2";
    private static final Resume RESUME_2 = new Resume(UUID_2);
    protected static final String UUID_3 = "uuid3";
    private static final Resume RESUME_3 = new Resume(UUID_3);

    public AbstractArrayStorageTest(Storage storage) {
        AbstractArrayStorageTest.storage = storage;
    }
    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void save() {
        int startSize = storage.size();
        storage.save(new Resume());
        assertEquals(storage.size(), startSize + 1);
    }

    @Test(expected = ExistStorageException.class)
    public void saveAlreadyExist() {
        storage.save(new Resume(UUID_1));
    }

    @Test(expected = StorageException.class)
    public void saveOverFlow() {
        for (int i = storage.size(); i < 10000; i++) {
            storage.save(new Resume());
        }
        assertEquals(10000, storage.size());
        storage.save(new Resume());
    }

    @Test
    public void update() {
        Resume resume = new Resume(UUID_1);
        storage.update(resume);
        assertSame(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        Resume resume = new Resume("not");
        storage.update(resume);
    }

    @Test
    public void get() {
        assertEquals(storage.get(UUID_1), RESUME_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete("NULL");
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        assertEquals(resumes[0], RESUME_1);
        assertEquals(resumes[1], RESUME_2);
        assertEquals(resumes[2], RESUME_3);
        assertEquals(resumes.length, 3);
    }


    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }
}
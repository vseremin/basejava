package ru.javawebinar.webapp.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.model.Resume;

import static org.junit.Assert.*;

public class AbstractStorageTest {
    protected static Storage storage;
    protected static final String UUID_1 = "uuid1";
    public static final String NAME_1 = "1";
    private static final Resume RESUME_1 = new Resume(NAME_1, UUID_1);

    public static final String NAME_2 = "2";
    protected static final String UUID_2 = "uuid2";
    private static final Resume RESUME_2 = new Resume(NAME_2, UUID_2);

    public static final String NAME_3 = "2";
    protected static final String UUID_3 = "uuid3";
    private static final Resume RESUME_3 = new Resume(NAME_3, UUID_3);

    public static final String NAME_4 = "";
    private static final String UUID_4 = "uuid4";
    protected static final Resume RESUME_4 = new Resume(NAME_4, UUID_4);
    private static final String UUID_NOT_EXIST = "dummy";

    public AbstractStorageTest(Storage storage) {
        AbstractStorageTest.storage = storage;
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
        assertSize(0);
        assertArrayEquals(storage.getAllSorted().toArray(), new Storage[0]);
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertGet(RESUME_4);
        assertSize(4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveAlreadyExist() {
        storage.save(new Resume(NAME_1, UUID_1));
    }

    @Test
    public void update() {
        Resume resume = new Resume(NAME_1, UUID_1);
        storage.update(resume);
        assertSame(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        Resume resume = new Resume(NAME_3, UUID_NOT_EXIST);
        storage.update(resume);
    }

    @Test
    public void get() {
        for (Resume resume : storage.getAllSorted()) {
            assertGet(resume);
        }
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NOT_EXIST);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_NOT_EXIST);
    }

    @Test
    public void delete() {
        storage.delete(RESUME_2.getUuid());
        assertSize(2);
    }

    @Test
    public void getAll() {
        Resume[] resumes = new Resume[3];
        storage.getAllSorted().toArray(resumes);
        assertEquals(resumes[0], RESUME_1);
        assertEquals(resumes[1], RESUME_2);
        assertEquals(resumes[2], RESUME_3);
        assertEquals(resumes.length, 3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    private void assertSize(int num) {
        Assert.assertEquals(num, storage.size());
    }

    private void assertGet(Resume resume) {
        assertEquals(storage.get(resume.getUuid()), resume);
    }
}
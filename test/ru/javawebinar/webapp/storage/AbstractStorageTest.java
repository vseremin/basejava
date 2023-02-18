package ru.javawebinar.webapp.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.webapp.Config;
import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.model.Contacts;
import ru.javawebinar.webapp.model.Resume;

import java.io.File;
import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static ru.javawebinar.webapp.TestData.*;

public class AbstractStorageTest {
    protected static final String STORAGE_STRING_DIR = Config.getInstance().getPathStorageDir();
    protected static final File STORAGE_DIR = new File(Config.getInstance().getPathStorageDir());
    protected static Storage storage;
//    protected static final String UUID_1 = String.valueOf(UUID.randomUUID());
    public static final String NAME_1 = "name1";
//    private static final Resume RESUME_1 = new ResumeTestData().init(NAME_1, UUID_1);//new Resume(NAME_1, UUID_1);//
//    public static final String NAME_2 = "name2";
//    protected static final String UUID_2 = String.valueOf(UUID.randomUUID());
//    private static final Resume RESUME_2 = new ResumeTestData().init(NAME_2, UUID_2);//new Resume(NAME_2, UUID_2);//
////
    public static final String NAME_3 = "name3";
//    protected static final String UUID_3 = String.valueOf(UUID.randomUUID());
//    private static final Resume RESUME_3 = new ResumeTestData().init(NAME_3, UUID_3);//new Resume(NAME_3, UUID_3);//
//
//    public static final String NAME_4 = "";
//    private static final String UUID_4 = String.valueOf(UUID.randomUUID());
//    protected static final Resume RESUME_4 = new ResumeTestData().init(NAME_4, UUID_4);//new Resume(NAME_4, UUID_4);//
    private static final String UUID_NOT_EXIST = String.valueOf(UUID.randomUUID());

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
        resume.addContact(Contacts.TELEPHONE, "777");
        storage.update(resume);
        assertEquals(resume, storage.get(UUID_1));
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
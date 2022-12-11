package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (size >= STORAGE_LIMIT) {
//            System.out.println("Storage overflow");
            throw new StorageException("Storage overflow", r.getUuid());
        } else if (index >= 0) {
//            System.out.println("Resume " + r.getUuid() + " already exist");
            throw new ExistStorageException(r.getUuid());
        } else {
            addResume(index, r);
            size++;
        }
    }

    public void update(Resume resume) {
        int index = search(resume.getUuid());
        if (index >= 0) storage[index] = resume;
    }

    public Resume get(String uuid) {
        int index = search(uuid);
        return index >= 0 ? storage[index]: null;
    }

    public void delete(String uuid) {
        int index = search(uuid);
        if (index >= 0) {
            deleteResume(index);
            size--;
            storage[size] = null;
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int search (String uuid) {
        int index = getIndex(uuid);
        if (index <= -1) {
//            System.out.println("Resume " + uuid + " not exist");
//            return -1;
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    public int size() {
        return size;
    }

    protected abstract void addResume(int index, Resume r);

    protected abstract void deleteResume(int index);

    protected abstract int getIndex(String uuid);

}

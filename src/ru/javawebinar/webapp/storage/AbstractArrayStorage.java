package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    @Override
    public void clearStorage() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void updateResume(Resume resume, int index) {
        storage[index] = resume;
    }

    @Override
    public void saveResume(int index, Resume r) {
        if (getSize() >= AbstractArrayStorage.STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            addResume(index, r);
            size++;
        }
    }

    @Override
    public Resume getResume(int index) {
        return index >= 0 ? storage[index] : null;
    }

    @Override
    public void deleteFromStorage(int index) {
        deleteResume(index);
        size--;
        storage[size + 1] = null;
    }

    @Override
    public Resume[] getAllResumes() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    public int getSize() {
        return size;
    }

    protected abstract void deleteResume(int index);
}

package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

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
    public void doUpdate(Object searchKey, Resume resume) {
        storage[(int) searchKey] = resume;
    }

    @Override
    public void doSave(Object index, Resume r) {
        if (getSize() >= AbstractArrayStorage.STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else {
            addResume((int) index, r);
            size++;
        }
    }

    @Override
    public Resume doGet(Object index) {
        return (int) index >= 0 ? storage[(int) index] : null;
    }

    @Override
    public void doDelete(Object index) {
        deleteResume((int) index);
        size--;
        storage[size + 1] = null;
    }

    @Override
    public List<Resume> doGetAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    @Override
    public int getSize() {
        return size;
    }

    protected abstract void deleteResume(int index);

    protected abstract void addResume(int index, Resume r);
}

package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected static final int STORAGE_LIMIT = 10000;

    @Override
    public void clear() {
        clearStorage();
    }

    @Override
    public void update(Resume resume) {
        int index = search(resume.getUuid());
        if (index >= 0) {
            updateResume(resume, index);
        }
    }

    @Override
    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (getSize() >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        } else if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
        } else {
            saveResume(index, r);
        }
    }

    @Override
    public Resume get(String uuid) {
        int index = search(uuid);
        return getResume(index);
    }

    @Override
    public void delete(String uuid) {
        int index = search(uuid);
        if (index >= 0) {
            deleteFromStorage(index);
        }
    }

    @Override
    public Resume[] getAll() {
        return getAllResumes();
    }

    @Override
    public int size() {
        return getSize();
    }

    protected int search(String uuid) {
        int index = getIndex(uuid);
        if (index <= -1) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    protected abstract void clearStorage();

    protected abstract void updateResume(Resume resume, int index);

    protected abstract void saveResume(int index, Resume r);

    protected abstract void addResume(int index, Resume r);

    protected abstract int getIndex(String uuid);

    public abstract Resume getResume(int index);

    protected abstract void deleteFromStorage(int index);

    protected abstract Resume[] getAllResumes();

    protected abstract int getSize();
}

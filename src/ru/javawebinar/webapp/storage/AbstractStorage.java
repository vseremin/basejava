package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void clear() {
        clearStorage();
    }

    @Override
    public void update(Resume resume) {
        getNotExistingSearchKey(resume);
        updateResume(resume);
    }

    @Override
    public void save(Resume r) {
        getExistingSearchKey(r);
        saveResume((int) getSearchKey(r.getUuid()), r);
    }

    @Override
    public Resume get(String uuid) {
        getNotExistingSearchKey(new Resume(uuid));
        return getResume((int) getSearchKey(uuid));
    }

    @Override
    public void delete(String uuid) {
        getNotExistingSearchKey(new Resume(uuid));
        deleteFromStorage((int) getSearchKey(uuid));
    }

    @Override
    public Resume[] getAll() {
        return getAllResumes();
    }

    @Override
    public int size() {
        return getSize();
    }

    protected void getExistingSearchKey(Resume r) {
        if (isExist(r.getUuid())) {
            throw new ExistStorageException(r.getUuid());
        }
    }

    protected void getNotExistingSearchKey(Resume r) {
        if (!isExist(r.getUuid())) {
            throw new NotExistStorageException(r.getUuid());
        }
    }

    protected abstract void clearStorage();

    protected abstract void updateResume(Resume resume);

    protected abstract void saveResume(int index, Resume r);

    protected abstract void addResume(int index, Resume r);

    protected abstract Object getSearchKey(String uuid);

    public abstract Resume getResume(int index);

    protected abstract void deleteFromStorage(int index);

    protected abstract Resume[] getAllResumes();

    protected abstract int getSize();

    protected abstract boolean isExist(String uuid);
}

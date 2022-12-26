package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    public static final Comparator<Resume> RESUME_COMPARARTOR =
            Comparator.comparing(Resume::getUuid)
                    .thenComparing(Resume::getFullName);

    @Override
    public void clear() {
        clearStorage();
    }

    @Override
    public void update(Resume resume) {
        doUpdate(getExistingSearchKey(resume.getUuid()), resume);
    }

    @Override
    public void save(Resume r) {
        doSave(getNotExistingSearchKey(r.getUuid()), r);
    }

    @Override
    public Resume get(String uuid) {
        return doGet(getExistingSearchKey(uuid));
    }

    @Override
    public void delete(String uuid) {
        doDelete(getExistingSearchKey(uuid));
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = doGetAll();
        list.sort(RESUME_COMPARARTOR);
        return list;
    }

    @Override
    public int size() {
        return getSize();
    }

    protected Object getExistingSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    protected Object getNotExistingSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract void clearStorage();

    protected abstract void doUpdate(Object searchKey, Resume resume);

    protected abstract void doSave(Object searchKey, Resume r);

    protected abstract Object getSearchKey(String uuid);

    public abstract Resume doGet(Object searchKey);

    protected abstract void doDelete(Object searchKey);

    protected abstract List<Resume> doGetAll();

    protected abstract int getSize();

    protected abstract boolean isExist(Object searchKey);
}

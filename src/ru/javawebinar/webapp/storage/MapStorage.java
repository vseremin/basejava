package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.model.Resume;

import java.util.*;

public class MapStorage extends AbstractStorage {

    protected static final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void clearStorage() {
        storage.clear();
    }

    @Override
    protected void doUpdate(Object searchKey, Resume resume) {
        storage.put((String) searchKey, resume);
    }

    @Override
    protected void doSave(Object searchKey, Resume r) {
        storage.put((String) searchKey, r);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    public Resume doGet(Object searchKey) {
        return storage.get((String) searchKey);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove((String) searchKey);
    }

    @Override
    protected List<Resume> doGetAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    protected int getSize() {
        return storage.size();
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return storage.containsKey((String) searchKey);
    }
}

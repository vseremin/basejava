package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.model.Resume;

import java.util.*;

public class MapResumeStorage extends AbstractStorage {

    protected static final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void clearStorage() {
        storage.clear();
    }

    @Override
    protected void doUpdate(Object searchKey, Resume resume) {
        storage.put(((Resume) searchKey).getUuid(), resume);
    }

    @Override
    protected void doSave(Object searchKey, Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    public Resume doGet(Object searchKey) {
        return storage.get(((Resume) searchKey).getUuid());
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove(((Resume) searchKey).getUuid());
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
        return searchKey != null;
    }
}

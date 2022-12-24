package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {

    private static final List<Resume> storage = new ArrayList<>();

    @Override
    protected void clearStorage() {
        storage.clear();
    }

    @Override
    protected void doUpdate(Object searchKey, Resume resume) {
        storage.set((int) searchKey, resume);
    }

    @Override
    protected void doSave(Object index, Resume r) {
        addResume((int) index, r);
    }

    protected void addResume(int index, Resume r) {
        storage.add(r);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Resume doGet(Object searchKey) {
        return (int) searchKey >= 0 ? storage.get((int) searchKey) : null;
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected Resume[] doGetAll() {
        Resume[] r = new Resume[getSize()];
        return storage.toArray(r);
    }

    @Override
    protected int getSize() {
        return storage.size();
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }
}

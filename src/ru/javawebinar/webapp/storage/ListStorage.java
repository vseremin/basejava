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
    protected void updateResume(Resume resume) {
        storage.set((int) getSearchKey(resume.getUuid()), resume);
    }

    @Override
    protected void saveResume(int index, Resume r) {
        addResume(index, r);
    }

    @Override
    protected void addResume(int index, Resume r) {
        storage.add(r);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        if (storage.contains(searchKey)) {
            return storage.indexOf(searchKey);
        } else {
            return -1;
        }
    }

    @Override
    public Resume getResume(int index) {
        return index >= 0 ? storage.get(index) : null;
    }

    @Override
    protected void deleteFromStorage(int index) {
        storage.remove(index);
    }

    @Override
    protected Resume[] getAllResumes() {
        Resume[] r = new Resume[getSize()];
        return  storage.toArray(r);
    }

    @Override
    protected int getSize() {
        return storage.size();
    }

    @Override
    protected boolean isExist(String uuid) {
        return (int) getSearchKey(uuid) >= 0;
    }
}

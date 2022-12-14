package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    public static final Comparator<Resume> RESUME_COMPARARTOR =
            Comparator.comparing(Resume::getUuid);

    @Override
    public void addResume(int index, Resume r) {
        int insertIdx = -index - 1;
        System.arraycopy(storage, insertIdx, storage, insertIdx + 1, size - insertIdx);
        storage[-(index + 1)] = r;
    }

    @Override
    public void deleteResume(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume("", uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARARTOR);
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

}

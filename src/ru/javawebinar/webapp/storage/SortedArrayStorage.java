package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume r) {
        if (size == 0) {
            storage[size] = r;
            size++;
            return;
        } else if (size >= STORAGE_LIMIT) {
            System.out.println("Storage overflow");
            return;
        }

        int position = -Arrays.binarySearch(storage, 0, size, r);
        if (position == 0) {
            System.out.println("Resume " + r.getUuid() + " already exist");
        } else if (position - 1 == size) {
            storage[size] = r;
            size++;
        } else {
            size++;
            System.arraycopy(storage, position - 1, storage, position, size - position);
            storage[position - 1] = r;
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index <= -1) {
            System.out.println("Resume " + uuid + " not exist");
            return;
        }
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
        size--;
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}

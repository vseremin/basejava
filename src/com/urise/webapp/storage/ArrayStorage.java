package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    public static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (size >= STORAGE_LIMIT) {
            System.out.println("В массиве нет свободного места");
        } else if (getIndex(r.getUuid()) != -1) {
            System.out.println("Резюме с " + r.getUuid() + " уже есть в базе!");
        } else {
            storage[size] = r;
            size++;
        }
    }

    public Resume get(String uuid) {
        if (getIndex(uuid) == -1) {
            System.out.println("Резюме с " + uuid + " нет в базе!");
            return null;
        }
        return storage[getIndex(uuid)];
    }

    public void delete(String uuid) {
        if (getIndex(uuid) == -1) {
            System.out.println("Резюме с " + uuid + " нет в базе!");
            return;
        }
        int index = getIndex(uuid);
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    public void update(Resume resume) {
        if (getIndex(resume.getUuid()) == -1) {
            System.out.println("Резюме с " + resume.getUuid() + " нет в базе!");
            return;
        }
        int index = getIndex(resume.getUuid());
        storage[index] = resume;
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}

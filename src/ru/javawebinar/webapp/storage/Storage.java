package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public interface Storage {

    void clear();

    void update(Resume resume);

    void save(Resume r);

    Resume get(String uuid);

    void delete(String uuid);

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll();

    int size();

}

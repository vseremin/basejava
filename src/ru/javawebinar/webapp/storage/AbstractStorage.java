package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.ExistStorageException;
import ru.javawebinar.webapp.exception.NotExistStorageException;
import ru.javawebinar.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    public static final Comparator<Resume> RESUME_COMPARATOR =
            Comparator.comparing(Resume::getFullName)
                    .thenComparing(Resume::getUuid);

    @Override
    public void clear() {
        clearStorage();
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        doUpdate(getExistingSearchKey(resume.getUuid()), resume);
    }

    @Override
    public void save(Resume r) {
        LOG.info("Save " + r);
        doSave(getNotExistingSearchKey(r.getUuid()), r);
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return doGet(getExistingSearchKey(uuid));
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        doDelete(getExistingSearchKey(uuid));
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> list = doGetAll();
        list.sort(RESUME_COMPARATOR);
        return list;
    }

    @Override
    public int size() {
        return getSize();
    }

    protected SK getExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    protected SK getNotExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract void clearStorage();

    protected abstract void doUpdate(SK searchKey, Resume resume);

    protected abstract void doSave(SK searchKey, Resume r);

    protected abstract SK getSearchKey(String uuid);

    public abstract Resume doGet(SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract List<Resume> doGetAll();

    protected abstract int getSize();

    protected abstract boolean isExist(SK searchKey);
}

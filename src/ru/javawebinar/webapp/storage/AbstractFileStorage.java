package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected void clearStorage() {
        String[] listFiles = directory.list();
        if (listFiles != null) {
            for (String file : listFiles) {
                File resume = new File(file);
                if (!resume.delete()) {
                    throw new StorageException("File delete error", resume.getName());
                }
            }
        }
        throw new StorageException("Storage must not be null", "Storage must not be null");
    }

    @Override
    protected void doUpdate(File searchKey, Resume resume) {
        try {
            doWrite(resume, searchKey);
        } catch (IOException e) {
            throw new StorageException("File write error", searchKey.getName(), e);
        }
    }

    @Override
    protected void doSave(File file, Resume r) {
        try {
            file.createNewFile();
            doUpdate(file, r);
        } catch (IOException e) {
            throw new StorageException("Couldn`t create file " + file.getAbsolutePath(), file.getName(), e);
        }
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    public Resume doGet(File searchKey) {
        try {
            return doRead(searchKey);
        } catch (IOException e) {
            throw new StorageException("File read error", searchKey.getName(), e);
        }
    }

    @Override
    protected void doDelete(File searchKey) {
        if (!searchKey.delete()) {
            throw new StorageException("File delete error", searchKey.getName());
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        String[] listFiles = directory.list();
        if (listFiles == null) {
            throw new StorageException("Directory read error", null);
        }
        List<Resume> resumes = new ArrayList<>();
        for (String file : listFiles) {
            resumes.add(doGet(new File(file)));
        }
        return resumes;
    }

    @Override
    protected int getSize() {
        String[] listFiles = directory.list();
        if (listFiles == null) {
            throw new StorageException("Directory read error",  null);
        }
        return listFiles.length;
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    protected abstract void doWrite(Resume r, File file) throws IOException;

    protected abstract Resume doRead(File file) throws IOException;
}

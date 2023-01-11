package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;
    private StorageStrategy storageStrategy;

    protected AbstractFileStorage(File directory, StorageStrategy storageStrategy) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
        this.storageStrategy = storageStrategy;
    }

    @Override
    protected void clearStorage() {
        File[] listFiles = directory.listFiles();
        if (listFiles == null) {
            throw new StorageException("listFiles is null", null);
        } else {
            for (File file : listFiles) {
                doDelete(file);
            }
        }
    }

    @Override
    protected void doUpdate(File searchKey, Resume resume) {
        try {
            storageStrategy.doWrite(resume, new BufferedOutputStream(new FileOutputStream(searchKey)));
        } catch (IOException e) {
            throw new StorageException("File write error", searchKey.getName(), e);
        }
    }

    @Override
    protected void doSave(File file, Resume r) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Couldn`t create file " + file.getAbsolutePath(), file.getName(), e);
        }
        doUpdate(file, r);
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    public Resume doGet(File searchKey) {
        try {
            return storageStrategy.doRead(new BufferedInputStream(new FileInputStream(searchKey)));
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
        File[] listFiles = directory.listFiles();
        if (listFiles == null) {
            throw new StorageException("Directory read error", null);
        }
        List<Resume> resumes = new ArrayList<>();
        for (File file : listFiles) {
            resumes.add(doGet(file));
        }
        return resumes;
    }

    @Override
    protected int getSize() {
        String[] listFiles = directory.list();
        if (listFiles == null) {
            throw new StorageException("Directory read error", null);
        }
        return listFiles.length;
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

//    public abstract void doWrite(Resume r, OutputStream os) throws IOException;
//
//    public abstract Resume doRead(InputStream is) throws IOException;
}

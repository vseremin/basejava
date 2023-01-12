package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;
import ru.javawebinar.webapp.storage.serializer.Serializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final Serializer storageStrategy;

    protected FileStorage(File directory, Serializer storageStrategy) {
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
        for (File file : getFileArray()) {
            doDelete(file);
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
        List<Resume> resumes = new ArrayList<>();
        for (File file : getFileArray()) {
            resumes.add(doGet(file));
        }
        return resumes;
    }

    @Override
    protected int getSize() {
        return getFileArray().length;
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    private File[] getFileArray() {
        if (directory.listFiles() != null) {
            return directory.listFiles();
        }
        throw new StorageException("Directory read error", null);
    }
}

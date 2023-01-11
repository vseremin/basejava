package ru.javawebinar.webapp.storage;

import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AbstractPathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private StorageStrategy storageStrategy;

    protected AbstractPathStorage(String dir, StorageStrategy storageStrategy) {
        directory = Paths.get(dir);

        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + "is not directory");
        }
        this.storageStrategy = storageStrategy;
    }

    @Override
    protected void clearStorage() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    protected void doUpdate(Path searchKey, Resume resume) {
        try {
            storageStrategy.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(searchKey)));
        } catch (IOException e) {
            throw new StorageException("File write error", searchKey.toString(), e);
        }
    }

    @Override
    protected void doSave(Path path, Resume r) {
        try {
            Files.createFile(Paths.get(directory + File.separator + r.getUuid()));
        } catch (IOException e) {
            throw new StorageException("Couldn`t create file " + path, null);
        }
        doUpdate(path, r);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(directory + File.separator + uuid);
    }

    @Override
    public Resume doGet(Path searchKey) {
        try {
            return storageStrategy.doRead(new BufferedInputStream(Files.newInputStream(searchKey)));
        } catch (IOException e) {
            throw new StorageException("File read error", searchKey.toString(), e);
        }
    }

    @Override
    protected void doDelete(Path searchKey) {
        try {
            Files.delete(searchKey);
        } catch (IOException e) {
            throw new StorageException("File delete error", searchKey.toString());
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        List<Resume> resumes = new ArrayList<>();
        try {
            List<Path> path = Files.list(directory).collect(Collectors.toList());
            for (Path p : path) {
                resumes.add(doGet(p));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resumes;
    }

    @Override
    protected int getSize() {
        List<Path> listPath;
        try {
            listPath = Files.list(directory).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Directory read error", null);
        }
        return listPath.size();
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

}

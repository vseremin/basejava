package ru.javawebinar.webapp.storage.serializer;

import ru.javawebinar.webapp.exception.StorageException;
import ru.javawebinar.webapp.model.Resume;

import java.io.*;

public class SerializerOption implements Serializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(r);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }
}

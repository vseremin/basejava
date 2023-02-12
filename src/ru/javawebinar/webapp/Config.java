package ru.javawebinar.webapp;

import ru.javawebinar.webapp.storage.SqlStorage;
import ru.javawebinar.webapp.storage.Storage;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    protected static File PROPS;
    private final Properties props = new Properties();
    private final File storageDir;
    private final Storage storage;

    private Config() {
        PROPS = new File("/home/user/StartJava/Basejava/config/resumes.properties");
        try (InputStream is = new FileInputStream(PROPS)) {
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            storage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"),
                    props.getProperty("db.password"));
        }  catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public String getPathStorageDir() {
        return storageDir.getAbsolutePath();
    }

    public Storage getStorage() {
        return storage;
    }
}

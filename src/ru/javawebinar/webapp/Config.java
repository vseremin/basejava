package ru.javawebinar.webapp;

import ru.javawebinar.webapp.storage.SqlStorage;
import ru.javawebinar.webapp.storage.Storage;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    protected static File PROPS;// = new File(getHomeDir(),  "config/resumes.properties");

    private final Properties props = new Properties();
    private final File storageDir;
    private final Storage storage;

    private Config() {
        PROPS = new File(getHomeDir(), "config/resumes.properties");
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

    private static String getHomeDir() {
        String homeDir = System.getProperty("homeDir");
        File file = new File(homeDir == null ? "." : homeDir);
        if (!file.isDirectory()) {
            throw new IllegalStateException(homeDir + " is not directory");
        }
        return homeDir;
    }
}

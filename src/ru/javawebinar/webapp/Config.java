package ru.javawebinar.webapp;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    protected static final File PROPS = new File("./storage/resumes.properties");
    private final Properties props = new Properties();
    private final File storageDir;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private Config() {
        try (InputStream is = new FileInputStream("config/resumes.properties")) {
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
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

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}

package ru.javawebinar.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFile {
    public static void main(String[] args) {
        String filePath = ".gitignore";
        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }
        File dir = new File("src/ru/javawebinar");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        printDirectoryDeeply("src/ru/javawebinar", "");

    }

    //TODO: make pretty output
    static void printDirectoryDeeply(String name, String offset) {
        String fileString = "File:      ";
        String dirString = "Directory: ";
        offset = (new StringBuilder(offset).append("    ")).toString();
        File[] listFiles = new File(name).listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isFile()) {
                    System.out.println(fileString + offset + file.getName());
                }

                String fileName = name + File.separator + file.getName();
                if (file.isDirectory()) {
                    System.out.println(dirString + offset + file.getName());
                    printDirectoryDeeply(fileName, offset);
                }
            }
        }
    }
}

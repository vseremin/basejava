package ru.javawebinar.webapp.web;

import java.util.List;
import java.util.stream.Stream;

public class UtilForWeb {
    public static String getSectionToString(List<String> list) {
        return String.join("\n", list);
    }

    public static List<String> tarnsformValueToList(String value) {
        return Stream.of(value.split("\r\n")).filter(p -> p.trim() != "").toList();
    }

}

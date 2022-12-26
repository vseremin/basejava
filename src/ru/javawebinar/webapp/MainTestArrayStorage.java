package ru.javawebinar.webapp;

import ru.javawebinar.webapp.model.Resume;
import ru.javawebinar.webapp.storage.SortedArrayStorage;
import ru.javawebinar.webapp.storage.Storage;

/**
 * Test for your com.urise.webapp.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    private static final Storage ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        final Resume r1 = new Resume("","uuid1");
        final Resume r2 = new Resume("","uuid0");
        final Resume r5 = new Resume("","uuid5");
        final Resume r3 = new Resume("","uuid3");
        final Resume r4 = new Resume("","uuid3");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r5);
//        ARRAY_STORAGE.delete("NULL");
        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

//        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));
//        System.out.println("Index of r4: " +
//                Arrays.binarySearch(ARRAY_STORAGE.getAll(), 0, ARRAY_STORAGE.size(), r4));

        ARRAY_STORAGE.update(r4);
        System.out.println(ARRAY_STORAGE.getAllSorted().get(0) == r1);

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }
}

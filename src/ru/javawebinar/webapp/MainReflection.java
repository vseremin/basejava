package ru.javawebinar.webapp;

import ru.javawebinar.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws ReflectiveOperationException {
        Resume r = new Resume();
        Field field = r.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(r));
        field.set(r, "new_uuid");
        //TODO : invoke r.toString() via reflection
        System.out.println(r);

        Resume r2 = new Resume();
        Method test = r2.getClass().getMethod("toString");
        System.out.println(test.invoke(r2));
    }
}

package ru.javawebinar.webapp;

import ru.javawebinar.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws ReflectiveOperationException {
        Resume r = new Resume();
        Class<? extends Resume> resumeClass = r.getClass();
        Field field = resumeClass.getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(r));
        field.set(r, "new_uuid");
        //TODO : invoke r.toString() via reflection

        Method test = resumeClass.getMethod("toString");
        Object result = test.invoke(r);
        System.out.println(result);
    }
}

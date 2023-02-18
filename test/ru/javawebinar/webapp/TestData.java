package ru.javawebinar.webapp;

import ru.javawebinar.webapp.model.*;

import java.time.Month;
import java.util.UUID;

public class TestData {
    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String UUID_4 = UUID.randomUUID().toString();

    public static final Resume RESUME_1;
    public static final Resume RESUME_2;
    public static final Resume RESUME_3;
    public static final Resume RESUME_4;

    static {
        RESUME_1 = new Resume("Name1", UUID_1);
        RESUME_2 = new Resume( "Name2", UUID_2);
        RESUME_3 = new Resume("Name3", UUID_3);
        RESUME_4 = new Resume("Name4", UUID_4);

        RESUME_1.addContact(Contacts.MAIL, "mail1@ya.ru");
        RESUME_1.addContact(Contacts.TELEPHONE, "11111");

        RESUME_4.addContact(Contacts.TELEPHONE, "44444");
        RESUME_4.addContact(Contacts.SKYPE, "Skype");

        RESUME_1.addSections(SectionType.OBJECTIVE, new TextSection("Objective1"));
        RESUME_1.addSections(SectionType.PERSONAL, new TextSection("Personal data"));
        RESUME_1.addSections(SectionType.ACHIEVEMENT, new ListSection("Achivment11", "Achivment12", "Achivment13"));
        RESUME_1.addSections(SectionType.QUALIFICATIONS, new ListSection("Java", "SQL", "JavaScript"));
        RESUME_1.addSections(SectionType.EXPERIENCE,
                new CompanySection(
                        new Company("http://Organization11.ru","Organization11",
                                new Company.Period(2005, Month.JANUARY, "position1", "content1"),
                                new Company.Period(2001, Month.MARCH, 2005, Month.JANUARY, "position2", "content2"))));
        RESUME_1.addSections(SectionType.EXPERIENCE,
                new CompanySection(
                        new Company("http://Organization2.ru","Organization2",
                                new Company.Period(2015, Month.JANUARY, "position1", "content1"))));
        RESUME_1.addSections(SectionType.EDUCATION,
                new CompanySection(
                        new Company(null,"Institute",
                                new Company.Period(1996, Month.JANUARY, 2000, Month.DECEMBER, "aspirant", null),
                                new Company.Period(2001, Month.MARCH, 2005, Month.JANUARY, "student", "IT facultet"))
                        ));

        RESUME_2.addContact(Contacts.SKYPE, "skype2");
        RESUME_2.addContact(Contacts.TELEPHONE, "22222");
    }
}

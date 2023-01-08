package ru.javawebinar.webapp;

import ru.javawebinar.webapp.model.*;

import java.time.LocalDate;
import java.util.*;

public class ResumeTestData {

    public static void main(String[] args) {
        EnumMap<Contacts, String> contacts = new EnumMap<>(Contacts.class);

        contacts.put(Contacts.TELEPHONE, "+7(921) 855-0482");
        contacts.put(Contacts.SKYPE, "grigory.kislin");
        contacts.put(Contacts.GITHUB, "https://github.com/gkislin");
        contacts.put(Contacts.MAIL, "gkislin@yandex.ru");
        contacts.put(Contacts.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        contacts.put(Contacts.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        contacts.put(Contacts.HOMEPAGE, "http://gkislin.ru/");

        EnumMap<SectionType, AbstractSection> section = new EnumMap<>(SectionType.class);
        List<String> listSection = new ArrayList<>();
        listSection.add("Организация команды и успешная реализация Java проектов для сторонних заказчиков: " +
                "приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов " +
                "на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для " +
                "комплексных DIY смет");
        listSection.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"" +
                "Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное " +
                "взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 " +
                "выпускников. ");
        listSection.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. " +
                "Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk. ");

        section.put(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, " +
                "инициативность. Пурист кода и архитектуры. "));

        section.put(SectionType.ACHIEVEMENT, new ListSection(listSection));

        section.put(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web " +
                "и Enterprise технологиям"));

        section.put(SectionType.QUALIFICATIONS, new ListSection(new ArrayList<>(Arrays.asList("JEE AS: GlassFish " +
                "(v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2 ", "Version control: Subversion, Git, Mercury, " +
                "ClearCase, Perforce ", "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, " +
                "MySQL, SQLite, MS SQL, HSQLDB ", "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, " +
                "Groovy ", "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts "))));
        LocalDate data = LocalDate.of(2013, 10, 1);
        Company company1 = new Company("http://javaops.ru/", "Java Online Projects",
                new ArrayList<>(List.of(new Company.Period(data, LocalDate.now(), "Автор проекта.",
                        "Создание, организация и проведение Java онлайн проектов и стажировок."))));
        data = LocalDate.of(2014, 10, 1);
        Company company2 = new Company(
                "https://www.wrike.com/", "Wrike",
                new ArrayList<>(List.of(new Company.Period(data,
                        LocalDate.of(2016, 1, 1), "Старший разработчик (backend)",
                        "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, " +
                                "Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, " +
                                "авторизация по OAuth1, OAuth2, JWT SSO."))));
        Company company3 = new Company("http://www.alcatel.ru/", "Alcatel", new ArrayList<>(List.of(
                new Company.Period(LocalDate.of(1997, 9, 1),
                        LocalDate.of(2005, 1, 1), "Инженер по аппаратному и " +
                        "программному тестированию", "Тестирование, отладка, внедрение ПО цифровой телефонной " +
                        "станции Alcatel 1000 S12 (CHILL, ASM)."))));
        section.put(SectionType.EXPERIENCE, new CompanySection(new ArrayList<>(Arrays.asList(company1, company2,
                company3))));
        Company company4 = new Company("https://www.coursera.org/course/progfun", "Coursera",
                new ArrayList<>(List.of(new Company.Period(LocalDate.of(2013, 3, 1),
                        LocalDate.of(2013, 5, 1), "'Functional Programming Principles " +
                        "in Scala' by Martin Odersky", ""))));
        Company company5 = new Company("http://www.luxoft-training.ru/training/catalog/course.html?ID=22366",
                "Luxoft", new ArrayList<>(List.of(new Company.Period(
                LocalDate.of(2011, 3, 1), LocalDate.of(2011, 4, 1),
                "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'", ""))));
        Company company6 = new Company("http://www.ifmo.ru/", "Санкт-Петербургский национальный " +
                "исследовательский университет информационных технологий, механики и оптики", new ArrayList<>(
                Arrays.asList(new Company.Period(LocalDate.of(1987, 9, 1), LocalDate.of(
                                1993, 7, 1), "Инженер (программист Fortran, C)", ""),
                        new Company.Period(LocalDate.of(1993, 9, 1),
                                LocalDate.of(1996, 7, 1),
                                "Аспирантура (программист С, С++)", ""))));
        Company company7 = new Company("https://mipt.ru/", "Заочная физико-техническая школа при МФТИ",
                new ArrayList<>(List.of(new Company.Period(LocalDate.of(1984, 9, 1),
                        LocalDate.of(1987, 6, 1), "", "Закончил с отличием"))));
        section.put(SectionType.EDUCATION, new CompanySection(new ArrayList<>(Arrays.asList(company4, company5,
                company6, company7))));
        Resume resume = new Resume("uuid1", "Григорий Кислин", contacts, section);
        System.out.println(resume);
    }

    public Resume init(String fullName, String uuid) {
        EnumMap<Contacts, String> contacts = new EnumMap<>(Contacts.class);

        contacts.put(Contacts.TELEPHONE, "+7(999) 888-8888");
        contacts.put(Contacts.SKYPE, "skype.test");
        contacts.put(Contacts.GITHUB, "https://github.com/");
        contacts.put(Contacts.MAIL, "yandex@yandex.ru");
        contacts.put(Contacts.LINKEDIN, "https://www.linkedin.com/in/");
        contacts.put(Contacts.STACKOVERFLOW, "https://stackoverflow.com/users");
        contacts.put(Contacts.HOMEPAGE, "http://ya.ru");

        EnumMap<SectionType, AbstractSection> section = new EnumMap<>(SectionType.class);
        List<String> listSection = new ArrayList<>();
        listSection.add("Организация команды и успешная реализация Java проектов для сторонних заказчиков: " +
                "приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов " +
                "на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для " +
                "комплексных DIY смет");
        listSection.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"" +
                "Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное " +
                "взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 " +
                "выпускников. ");
        listSection.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. " +
                "Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk. ");

        section.put(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, " +
                "инициативность. Пурист кода и архитектуры. "));

        section.put(SectionType.ACHIEVEMENT, new ListSection(listSection));

        section.put(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web " +
                "и Enterprise технологиям"));

        section.put(SectionType.QUALIFICATIONS, new ListSection(new ArrayList<>(Arrays.asList("JEE AS: GlassFish " +
                "(v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2 ", "Version control: Subversion, Git, Mercury, " +
                "ClearCase, Perforce ", "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, " +
                "MySQL, SQLite, MS SQL, HSQLDB ", "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, " +
                "Groovy ", "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts "))));
        LocalDate data = LocalDate.of(2013, 10, 1);
        Company company1 = new Company("http://javaops.ru/", "Java Online Projects",
                new ArrayList<>(List.of(new Company.Period(data, LocalDate.now(), "Автор проекта.",
                        "Создание, организация и проведение Java онлайн проектов и стажировок."))));
        data = LocalDate.of(2014, 10, 1);
        Company company2 = new Company(
                "https://www.wrike.com/", "Wrike",
                new ArrayList<>(List.of(new Company.Period(data,
                        LocalDate.of(2016, 1, 1), "Старший разработчик (backend)",
                        "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, " +
                                "Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, " +
                                "авторизация по OAuth1, OAuth2, JWT SSO."))));
        Company company3 = new Company("http://www.alcatel.ru/", "Alcatel", new ArrayList<>(List.of(
                new Company.Period(LocalDate.of(1997, 9, 1),
                        LocalDate.of(2005, 1, 1), "Инженер по аппаратному и " +
                        "программному тестированию", "Тестирование, отладка, внедрение ПО цифровой телефонной " +
                        "станции Alcatel 1000 S12 (CHILL, ASM)."))));
        section.put(SectionType.EXPERIENCE, new CompanySection(new ArrayList<>(Arrays.asList(company1, company2,
                company3))));
        Company company4 = new Company("https://www.coursera.org/course/progfun", "Coursera",
                new ArrayList<>(List.of(new Company.Period(LocalDate.of(2013, 3, 1),
                        LocalDate.of(2013, 5, 1), "'Functional Programming Principles " +
                        "in Scala' by Martin Odersky", ""))));
        Company company5 = new Company("http://www.luxoft-training.ru/training/catalog/course.html?ID=22366",
                "Luxoft", new ArrayList<>(List.of(new Company.Period(
                LocalDate.of(2011, 3, 1), LocalDate.of(2011, 4, 1),
                "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'", ""))));
        Company company6 = new Company("http://www.ifmo.ru/", "Санкт-Петербургский национальный " +
                "исследовательский университет информационных технологий, механики и оптики", new ArrayList<>(
                Arrays.asList(new Company.Period(LocalDate.of(1987, 9, 1), LocalDate.of(
                                1993, 7, 1), "Инженер (программист Fortran, C)", ""),
                        new Company.Period(LocalDate.of(1993, 9, 1),
                                LocalDate.of(1996, 7, 1),
                                "Аспирантура (программист С, С++)", ""))));
        Company company7 = new Company("https://mipt.ru/", "Заочная физико-техническая школа при МФТИ",
                new ArrayList<>(List.of(new Company.Period(LocalDate.of(1984, 9, 1),
                        LocalDate.of(1987, 6, 1), "", "Закончил с отличием"))));
        section.put(SectionType.EDUCATION, new CompanySection(new ArrayList<>(Arrays.asList(company4, company5,
                company6, company7))));
        return new Resume(uuid, fullName, contacts, section);
    }
}

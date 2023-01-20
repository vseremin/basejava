package ru.javawebinar.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Unique identifier
    private final String uuid;
    private final String fullName;
    private Map<Contacts, String> contacts = new EnumMap<>(Contacts.class);
    private Map<SectionType, AbstractSection> section = new EnumMap<>(SectionType.class);

    public Resume() {
        this("fullName");
    }

    public Resume(String fullName) {
        this(fullName, UUID.randomUUID().toString());
    }

    public Resume(String fullName, String uuid) {
        this.fullName = fullName;
        this.uuid = uuid;
    }

    public Resume(String uuid, String fullName, Map<Contacts, String> contacts, Map<SectionType, AbstractSection> section) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.contacts = contacts;
        this.section = section;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public Map<Contacts, String> getContacts() {
        return contacts;
    }

    public void addContact(Contacts contact, String value) {
        contacts.put(contact, value);
    }

    public Map<SectionType, AbstractSection> getSection() {
        return section;
    }

    public void addSections(SectionType sectionType, AbstractSection abstractSection) {
        section.put(sectionType, abstractSection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) && Objects.equals(fullName, resume.fullName) &&
                Objects.equals(contacts, resume.contacts) && Objects.equals(section, resume.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, section);
    }

    @Override
    public String toString() {
        return "Resume{" +
                "\nuuid='" + uuid + '\'' +
                ", \nfullName='" + fullName + '\'' +
                ", \ncontacts=" + contacts +
                ", \nsection=" + section +
                '}';
    }
}

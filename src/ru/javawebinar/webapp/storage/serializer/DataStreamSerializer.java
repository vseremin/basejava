package ru.javawebinar.webapp.storage.serializer;

import ru.javawebinar.webapp.model.*;
import ru.javawebinar.webapp.util.LocalDateAdapter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements Serializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getFullName());
            dos.writeUTF(r.getUuid());
            Map<Contacts, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<Contacts, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

            Map<SectionType, AbstractSection> section = r.getSection();
            dos.writeInt(section.size());

            for (Map.Entry<SectionType, AbstractSection> entry : section.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                switch (entry.getKey()) {
                    case PERSONAL, OBJECTIVE -> writeText(String.valueOf(entry.getValue()),
                            (t) -> dos.writeUTF((String) t));
                    case ACHIEVEMENT, QUALIFICATIONS -> writeList(dos, ((ListSection) entry.getValue()).getList(),
                            (t) -> dos.writeUTF((String) t));
                    case EDUCATION, EXPERIENCE ->
                            writeList(dos, ((CompanySection) entry.getValue()).getCompanies(), (t) -> {
                                dos.writeUTF(((Company) t).getWebsite());
                                dos.writeUTF(((Company) t).getName());
                                writeList(dos, ((Company) t).getPeriods(), (s) -> {
                                    try {
                                        dos.writeUTF(new LocalDateAdapter().marshal(((Company.Period) s).getStartDate()));
                                        dos.writeUTF(new LocalDateAdapter().marshal(((Company.Period) s).getEndDate()));
                                        dos.writeUTF(((Company.Period) s).getTitle());
                                        dos.writeUTF(((Company.Period) s).getDescription());
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            });
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(Contacts.valueOf(dis.readUTF()), dis.readUTF());
            }

            int sizeSection = dis.readInt();
            for (int i = 0; i < sizeSection; i++) {
                String sectionType = readText(dis::readUTF);
                switch (SectionType.valueOf(sectionType)) {
                    case PERSONAL, OBJECTIVE -> resume.addSections(SectionType.valueOf(sectionType),
                            new TextSection(readText(dis::readUTF)));
                    case ACHIEVEMENT, QUALIFICATIONS -> resume.addSections(SectionType.valueOf(sectionType),
                            new ListSection(readList(dis, dis::readUTF)));
                    case EDUCATION, EXPERIENCE -> resume.addSections(SectionType.valueOf(sectionType),
                            new CompanySection(readList(dis, () ->
                                    new Company(dis.readUTF(), dis.readUTF(), readList(dis, () -> {
                                                try {
                                                    return (new Company.Period(new LocalDateAdapter()
                                                            .unmarshal(dis.readUTF()), new LocalDateAdapter()
                                                            .unmarshal(dis.readUTF()), dis.readUTF(), dis.readUTF()));
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                    ))
                            )));
                }
            }
            return resume;
        }

    }

    private void writeText(String text, TextWriter writer) throws IOException {
        writer.write(text);
    }

    private <T> String readText(TextReader<T> reader) throws IOException {
        return String.valueOf(reader.read());
    }

    private <T> void writeList(DataOutputStream dos, List<T> value, Writer writer) throws IOException {
        dos.writeInt(value.size());
        for (T t : value) {
            writer.write(t);
        }
    }

    private <T> List readList(DataInputStream dis, Reader<T> reader) throws IOException {
        List<T> list = new ArrayList<>();
        int sizeList = dis.readInt();
        for (int j = 0; j < sizeList; j++) {
            list.add(reader.read());
        }
        return list;
    }

    public interface TextReader<T> {
        T read() throws IOException;
    }

    public interface TextWriter<T> {
        void write(T t) throws IOException;
    }

    public interface Reader<T> {
        T read() throws IOException;
    }

    public interface Writer<T> {
        void write(T t) throws IOException;
    }

}

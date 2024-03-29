package ru.javawebinar.webapp.storage.serializer;

import ru.javawebinar.webapp.model.*;
import ru.javawebinar.webapp.util.LocalDateAdapter;

import java.io.*;
import java.util.*;

public class DataStreamSerializer implements Serializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getFullName());
            dos.writeUTF(r.getUuid());
            writeWithException(dos, r.getContacts().entrySet(), (t) -> {
                dos.writeUTF(((Map.Entry<Contacts, String>) t).getKey().name());
                dos.writeUTF(((Map.Entry<Contacts, String>) t).getValue());
            });

            writeWithException(dos, r.getSection().entrySet(), (v) -> {
                SectionType sectionType = ((Map.Entry<SectionType, AbstractSection>) v).getKey();
                AbstractSection section = ((Map.Entry<SectionType, AbstractSection>) v).getValue();
                dos.writeUTF(sectionType.name());
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE ->
                            dos.writeUTF(String.valueOf(section));
                    case ACHIEVEMENT, QUALIFICATIONS -> writeWithException(dos,
                            ((ListSection) section).getList(), (t) -> dos.writeUTF((String) t));
                    case EDUCATION, EXPERIENCE -> writeWithException(dos,
                            ((CompanySection) section).getCompanies(), (t) -> {
                                dos.writeUTF(((Company) t).getWebsite());
                                dos.writeUTF(((Company) t).getName());
                                writeWithException(dos, ((Company) t).getPeriods(), (s) -> {
                                    try {
                                        dos.writeUTF(
                                                new LocalDateAdapter().marshal(((Company.Period) s).getStartDate()));
                                        dos.writeUTF(new LocalDateAdapter().marshal(((Company.Period) s).getEndDate()));
                                        dos.writeUTF(((Company.Period) s).getTitle());
                                        dos.writeUTF(((Company.Period) s).getDescription());
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            });
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            readSection(dis, () -> {
                resume.addContact(Contacts.valueOf(dis.readUTF()), dis.readUTF());
                return null;
            });

            readSection(dis, () -> {
                String sectionType = dis.readUTF();
                switch (SectionType.valueOf(sectionType)) {
                    case PERSONAL, OBJECTIVE -> resume.addSections(SectionType.valueOf(sectionType),
                            new TextSection(dis.readUTF()));
                    case ACHIEVEMENT, QUALIFICATIONS -> resume.addSections(SectionType.valueOf(sectionType),
                            new ListSection((List<String>) readList(dis, dis::readUTF)));
                    case EDUCATION, EXPERIENCE -> resume.addSections(SectionType.valueOf(sectionType),
                            new CompanySection((List<Company>) readList(dis, () ->
                                    new Company(dis.readUTF(), dis.readUTF(),
                                            (List<Company.Period>) readList(dis, () -> {
                                                        try {
                                                            return (new Company.Period(new LocalDateAdapter()
                                                                    .unmarshal(dis.readUTF()), new LocalDateAdapter()
                                                                    .unmarshal(dis.readUTF()),
                                                                    dis.readUTF(), dis.readUTF()));
                                                        } catch (Exception e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                    }
                                            ))
                            )));
                }
                return null;
            });
            return resume;
        }
    }

    private <T> void writeWithException(DataOutputStream dos, Collection<T> value, Writer writer) throws IOException {
        dos.writeInt(value.size());
        for (T t : value) {
            writer.write(t);
        }
    }

    private <T> void readSection(DataInputStream dis, Reader<T> reader) throws IOException {
        int sizeList = dis.readInt();
        for (int i = 0; i < sizeList; i++) {
            reader.read();
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

    public interface Reader<T> {
        T read() throws IOException;
    }

    public interface Writer<T> {
        void write(T t) throws IOException;
    }
}

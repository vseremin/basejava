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
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
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
                    case PERSONAL, OBJECTIVE -> dos.writeUTF(String.valueOf(entry.getValue()));
                    case ACHIEVEMENT, QUALIFICATIONS -> writeList(dos, ((ListSection) entry.getValue()).getList(),
                            (t) -> {
                                try {
                                    dos.writeUTF((String) t);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                    case EDUCATION, EXPERIENCE ->
                            writeList(dos, ((CompanySection) entry.getValue()).getCompanies(), (t) -> {
                                try {
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
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            });
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(fullName, uuid);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(Contacts.valueOf(dis.readUTF()), dis.readUTF());
            }

            int sizeSection = dis.readInt();
            for (int i = 0; i < sizeSection; i++) {
                String sectionType = dis.readUTF();
                switch (SectionType.valueOf(sectionType)) {
                    case PERSONAL, OBJECTIVE -> resume.addSections(SectionType.valueOf(sectionType),
                            new TextSection(dis.readUTF()));
                    case ACHIEVEMENT, QUALIFICATIONS -> resume.addSections(SectionType.valueOf(sectionType),
                            new ListSection(readList(dis, () -> {
                                try {
                                    return dis.readUTF();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })));
                    case EDUCATION, EXPERIENCE -> resume.addSections(SectionType.valueOf(sectionType),
                            new CompanySection(readList(dis, () -> {
                                try {
                                    return new Company(dis.readUTF(), dis.readUTF(), readList(dis, () -> {
                                                try {
                                                    return (new Company.Period(new LocalDateAdapter()
                                                            .unmarshal(dis.readUTF()), new LocalDateAdapter()
                                                            .unmarshal(dis.readUTF()), dis.readUTF(), dis.readUTF()));
                                                } catch (Exception e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                    ));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })));
                }
            }
            return resume;
        }

    }

    private <T> void writeList(DataOutputStream dos, List<T> value, Writer writer) {
        try {
            dos.writeInt(value.size());
            for (T t : value) {
                writer.write(t);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private <T> List readList(DataInputStream dis, Reader<T> reader) {
        List<T> list = new ArrayList<>();
        try {
            int sizeList = dis.readInt();
            for (int j = 0; j < sizeList; j++) {
                list.add(reader.read());
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface Reader<T> {
        T read();
    }

    public interface Writer<T> {
        void write(T t);
    }
}

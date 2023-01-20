package ru.javawebinar.webapp.storage.serializer;

import ru.javawebinar.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements Serializer {
    private Resume resume;

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<Contacts, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<Contacts, String> entry : contacts.entrySet()) {
                writeTextSection(dos, entry.getKey().name(), entry.getValue());
            }

            Map<SectionType, AbstractSection> section = r.getSection();
            dos.writeInt(section.size());

            for (Map.Entry<SectionType, AbstractSection> entry : section.entrySet()) {
                if (entry.getValue().getClass().getName().contains("TextSection")) {
                    writeTextSection(dos, entry.getKey().name(), String.valueOf(entry.getValue()));
                } else if (entry.getValue().getClass().getName().contains("ListSection")) {
                    writeListSection(dos, entry.getKey().name(), ((ListSection) entry.getValue()).getList());
                } else if (entry.getValue().getClass().getName().contains("CompanySection")) {
                    writeCompanySection(dos, entry.getKey().name(), ((CompanySection) entry.getValue()).getCompanies());
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            resume = new Resume(fullName, uuid);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(Contacts.valueOf(dis.readUTF()), dis.readUTF());
            }

            int sizeSection = dis.readInt();
            for (int i = 0; i < sizeSection; i++) {
                String sectionType = dis.readUTF();
                if (sectionType.equals("PERSONAL") || sectionType.equals("OBJECTIVE")) {
                    readTextSection(dis, sectionType);
                } else if (sectionType.equals("ACHIEVEMENT") || sectionType.equals("QUALIFICATIONS")) {
                    readListSection(dis, sectionType);
                } else if (sectionType.equals("EXPERIENCE") || sectionType.equals("EDUCATION")) {
                    readCompanySection(dis, sectionType);
                }
            }
            return resume;
        }

    }

    private void writeTextSection(DataOutputStream dos, String name, String value) {
        try {
            dos.writeUTF(name);
            dos.writeUTF(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readTextSection(DataInputStream dis, String sectionName) {
        try {
            resume.addSections(SectionType.valueOf(sectionName), new TextSection(dis.readUTF()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeListSection(DataOutputStream dos, String name, List<String> value) {
        try {
            dos.writeUTF(name);
            dos.writeInt(value.size());
            for (String str : value) {
                dos.writeUTF(str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void readListSection(DataInputStream dis, String sectionType) {
        List<String> list = new ArrayList<>();
        int sizeList = 0;
        try {
            sizeList = dis.readInt();
            for (int j = 0; j < sizeList; j++) {
                list.add(dis.readUTF());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resume.addSections(SectionType.valueOf(sectionType), new ListSection(list));
    }

    private void writeCompanySection(DataOutputStream dos, String name, List<Company> value) {
        try {
            dos.writeUTF(name);
            dos.writeInt(value.size());
            for (Company company : value) {
                dos.writeUTF(company.getWebsite());
                dos.writeUTF(company.getName());
                dos.writeInt(company.getPeriods().size());
                for (Company.Period period : company.getPeriods()) {
                    dos.writeLong(period.getStartDate().toEpochDay());
                    dos.writeLong(period.getEndDate().toEpochDay());
                    dos.writeUTF(period.getTitle());
                    dos.writeUTF(period.getDescription());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readCompanySection(DataInputStream dis, String sectionType) {
        try {
            List<Company> listCompany = new ArrayList<>();
            int sizeCompany = dis.readInt();
            System.out.println("3 " + sectionType);
            for (int j = 0; j < sizeCompany; j++) {
                String website = dis.readUTF();
                String companyName = dis.readUTF();
                int sizePeriod = dis.readInt();
                List<Company.Period> periods = new ArrayList<>();
                for (int k = 0; k < sizePeriod; k++) {
                    periods.add(new Company.Period(LocalDate.ofEpochDay(dis.readLong()), LocalDate.ofEpochDay(dis.readLong()), dis.readUTF(), dis.readUTF()));
                }
                listCompany.add(new Company(website, companyName, periods));
            }

            resume.addSections(SectionType.valueOf(sectionType), new CompanySection(listCompany));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

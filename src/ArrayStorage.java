import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    protected final Resume[] storage = new Resume[10000];
    private int size;

    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    public void save(Resume r) {
        if (size < storage.length) {
            storage[size] = r;
            size++;
        } else {
            System.out.println("В массиве нет свободного места");
        }
    }

    public Resume get(String uuid) {
        if (getIndex(uuid) != - 1) {
            return storage[getIndex(uuid)];
        }
        System.out.println("Резюме с " + uuid + " нет в базе!");
        return null;
    }

    public void delete(String uuid) {
        if (getIndex(uuid) != -1) {
            int index = getIndex(uuid);
            System.arraycopy(storage,  index + 1, storage, index, size - index - 1);
            size--;
            return;
        }
        System.out.println("Резюме с " + uuid + " нет в базе!");
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}

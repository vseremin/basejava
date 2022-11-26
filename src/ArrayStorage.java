import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int lastIndex;

    void clear() {
        for (int i = 0; i < lastIndex; i++) {
            storage[i] = null;
        }
    }

    void save(Resume r) {
        if (lastIndex < storage.length) {
            storage[lastIndex] = r;
            lastIndex++;
        } else {
            System.out.println("В массиве нет свободного места");
        }
    }

    Resume get(String uuid) {
        for (Resume resume : storage) {
            if (resume.uuid.equals(uuid)) {
                return resume;
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < lastIndex; i++) {
            if (storage[i].uuid.equals(uuid)) {
                for (int j = i; j < storage.length - 1; j++) {
                    storage[j] = storage[j + 1];
                }
                break;
            }
        }
        lastIndex--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, lastIndex);
    }

    int size() {
        return lastIndex;
    }
}

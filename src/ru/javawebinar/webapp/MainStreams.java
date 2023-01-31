package ru.javawebinar.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainStreams {
    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3, 3, 2, 3};
        System.out.println(minValue(arr1));
        int[] arr2 = {9, 8};
        System.out.println(minValue(arr2));
        for (Integer i : oddOrEven(Arrays.stream(arr1).boxed().collect(Collectors.toList()))) {
            System.out.println(i);
        }
    }

    static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce((s, x) -> (s * 10) + x).
                getAsInt();
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers.stream()
                .collect(Collectors.partitioningBy(x -> x % 2 == 0, Collectors.toList()));
        return map.get(map.get(false).size() % 2 != 0);
    }
}

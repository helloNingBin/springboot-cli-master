package com.example.final_augues.javaGuide.youhua;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class App {
    public static void main(String[] args) {
        String detailPathOfA = "1,2,3", detailPathOfB = "1,2,3";
        checkDetail(detailPathOfA, detailPathOfB);
    }

    public static void checkDetail(String detailPathOfA, String detailPathOfB) {
        List<DetailDTO> resultListOfA = readDataFromFile(detailPathOfA, DetailDTO::convert);
        Map<String, DetailDTO> resultMapOfA = convertToMap(resultListOfA);
        System.out.println(resultListOfA);
        System.out.println(resultMapOfA);

        List<DetailDTO> resultListOfB = readDataFromFile(detailPathOfB, DetailDTO::convert);
        Map<String, DetailDTO> resultMapOfB = convertToMap(resultListOfB);
        System.out.println(resultListOfB);
        System.out.println(resultMapOfB);
    }

    public static <T> List<T> readDataFromFile(String filePath, Function<Integer, T> converter) {
        List<T> list = new ArrayList<>();
        for (String s : filePath.split(",")) {
            list.add(converter.apply(Integer.valueOf(s)));
        }
        return list;
    }

    public static <T extends BaseDTO> Map<String, T> convertToMap(List<T> list) {
        Map<String, T> map = new HashMap<>();
        list.forEach(t -> {
            map.put(t.getKey(), t);
        });
        return map;
    }
}

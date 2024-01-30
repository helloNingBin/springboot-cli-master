package com.example.final_augues.javaGuide.youhua;

import java.util.ArrayList;
import java.util.List;

public class DetailDTO extends BaseDTO {
    private int id;
    private String name;
    private int age;
    private static int instanceIndex = 0;


    public static DetailDTO convert(Integer l) {
        return new DetailDTO(l, "name_" + l, l * 10);
    }

    public static List<DetailDTO> getDetailList() {
        List<DetailDTO> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            instanceIndex++;
            list.add(new DetailDTO(instanceIndex, "name_" + instanceIndex, instanceIndex * 10));
        }
        return list;
    }

    public DetailDTO(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "DetailDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    String getKey() {
        return id + "_" + name;
    }
}

package com.netty.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;
@Slf4j
@RestController
public class StudentAPI {
    private Integer ii;
    private Integer jj;

    @Override
    public String toString() {
        return "StudentAPI{" +
                "ii=" + ii +
                ", jj=" + jj +
                '}' + super.toString();
    }

    public StudentAPI() {
        log.info("====================");
    }

    public StudentAPI(Integer ii, Integer jj) {
        this.ii = ii;
        this.jj = jj;
    }

    public Integer getIi() {
        return ii;
    }

    public void setIi(Integer ii) {
        this.ii = ii;
    }

    public Integer getJj() {
        return jj;
    }

    public void setJj(Integer jj) {
        this.jj = jj;
    }

    static List<Student> studentList;
    static{
        studentList = new ArrayList<>();
        studentList.add(new Student(1,"aa",11));
        studentList.add(new Student(2,"bb",22));
    }
//    @CrossOrigin
    @GetMapping("/getStudentById/{id}")
    public Student getStudentById(@PathVariable int id){
        System.out.println("========getStudentById===========");
        Stream<Student> studentStream = studentList.stream().filter(s -> s.id == id);
        return studentStream.findFirst().get();
    }
    @RequestMapping("/getStudentList")
    public List<Student> getStudentList(){
        System.out.println("============getStudentList=========");
        return studentList;
    }
    @GetMapping("/getStudentById2/{id}")
    public String getStudentById2(@PathVariable Long id) throws InterruptedException {
        log.info("========getStudentById===========");
        Thread.sleep(10000);
        return "sdfdsfdsf"+System.currentTimeMillis();
    }
    public static void main(String[] args) {
        Student s1 = new Student(1, "s1", 11);
        Student s2 = new Student(2, "s2", 22);
        Map<Student,Integer> map = new HashMap<>();
        for (int i = 0 ;i < 100;i++){
            map.put(new Student(i, "s1", 11), i);
        }
    }
}
class Student{
    public int id;
    public String name;
    public int age;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && age == student.age &&
                Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

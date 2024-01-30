package com.example.openoffice.classLayout;

import org.openjdk.jol.info.ClassLayout;

public class StudentLayout {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(5000);
        Student s = new Student(11, "22233333333白白白白白白白白白白折");
        //代码一
        System.out.println(ClassLayout.parseInstance(s).toPrintable());
        synchronized (s){
            //代码二
            System.out.println(ClassLayout.parseInstance(s).toPrintable());
        }
    }
}

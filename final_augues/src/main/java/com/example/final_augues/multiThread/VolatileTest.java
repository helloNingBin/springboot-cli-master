package com.example.final_augues.multiThread;

public class VolatileTest {
    private int i = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println(String.class.getClassLoader());
        System.out.println(Long.class.getClassLoader());
        ClassLoader classLoader = VolatileTest.class.getClassLoader();
        System.out.println(classLoader);
        System.out.println(classLoader.getParent());
        System.out.println(classLoader.getParent().getParent());
    }

    public VolatileTest() {
        // 对象创建时，增加计数器
        System.out.println("create VolatileTest object.");
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            // 对象销毁时，减少计数器
            System.out.println("destry VolatileTest object.");
        } finally {
            super.finalize();
        }
    }
}

package com.bunleng.student_manager.util;

public class StudentIdGenerator {
    private static int counter = 1;

    public static String generateId() {
        return String.format("YBL%03d", counter++);
    }
}

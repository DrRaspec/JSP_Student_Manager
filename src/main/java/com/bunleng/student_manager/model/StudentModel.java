package com.bunleng.student_manager.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentModel {
    private String id;
    private String name;
    private String email;
    private String major;

    public static StudentModel createWithAutoId(String name, String email, String major) {
        return new StudentModel(UUID.randomUUID().toString(), name, email, major);
    }
}
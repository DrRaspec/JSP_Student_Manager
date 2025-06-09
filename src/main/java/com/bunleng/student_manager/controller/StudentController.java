package com.bunleng.student_manager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.bunleng.student_manager.model.StudentModel;
import com.bunleng.student_manager.util.StudentIdGenerator;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class StudentController {
    private List<StudentModel> students = new ArrayList<>();

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        model.addAttribute("currentLocale", currentLocale);
        model.addAttribute("currentLang", currentLocale.getLanguage());
    }

    // Home page
    @GetMapping("/")
    public String home(Model model) {
        int totalStudents = students.size();
        String mostPopularMajor = students.stream()
        .collect(Collectors.groupingBy(StudentModel::getMajor, Collectors.counting()))
        .entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("N/A");
        model.addAttribute("students", students);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("mostPopularMajor", mostPopularMajor);
        return "index";
    }

    // show the student form
    @GetMapping("/student/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new StudentModel());
        return "pages/student/add";
    }

    // list all students
    @GetMapping("/student/list")
    public String listStudents(Model model) {
        model.addAttribute("students", students);
        System.out.println("Listing all students: " + students);
        return "pages/student/list";
    }

    // add a new student
    @PostMapping("/student/add")
    public String addStudent(@ModelAttribute("student") StudentModel student) {
        student.setId(StudentIdGenerator.generateId());
        students.add(student);
        return "redirect:/";
    }


    @GetMapping("/student/edit/{id}")
    public ModelAndView showEditStudentForm(@PathVariable String id) {
        StudentModel studentToEdit = students.stream().filter(student -> student.getId().equals(id)).findFirst().orElse(null);
        if (studentToEdit == null) {
            return new ModelAndView("redirect:/student/list");
        }
        
        ModelAndView modelAndView = new ModelAndView("pages/student/edit");
        modelAndView.addObject("student", studentToEdit);
        return modelAndView;
    }

    // edit a student
    @PostMapping("/student/edit")
    public String editStudent(@ModelAttribute("student") StudentModel updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(updatedStudent.getId())) {
                students.set(i, updatedStudent);
                break;
            }
        }

        return "redirect:/";
    }

    @GetMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable String id) {
        students.removeIf(student -> student.getId().equals(id));
        return "redirect:/student/list";
    }
}

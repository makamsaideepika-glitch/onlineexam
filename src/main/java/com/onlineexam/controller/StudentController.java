package com.onlineexam.controller;

import com.onlineexam.Student;
import com.onlineexam.repository.StudentRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Student Registration Page
    @GetMapping("/student/register")
    public String registerPage() {
        return "student-register";
    }

    // Student Registration
    @PostMapping("/student/register")
    public String registerStudent(
            @RequestParam String student_name,
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        if (studentRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists!");
            return "student-register";
        }

        Student student = new Student();

        student.setStudent_name(student_name);
        student.setUsername(username);
        student.setPassword(password);

        studentRepository.save(student);

        model.addAttribute("message",
                "Registration successful! Please login.");

        return "student-login";
    }

    // Student Login Page
    @GetMapping("/student/login")
    public String loginPage() {
        return "student-login";
    }

    // Student Login
    @PostMapping("/student/login")
    public String loginStudent(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        Student student = studentRepository.findByUsername(username);

        if (student != null && student.getPassword().equals(password)) {

            // Store logged-in student's ID in session
            session.setAttribute("studentId", student.getStudent_id());

            // Store student's name in session
            session.setAttribute("studentName", student.getStudent_name());

            model.addAttribute("studentName",
                    student.getStudent_name());

            return "student-dashboard";
        }

        model.addAttribute("error",
                "Invalid username or password!");

        return "student-login";
    }
 // Student Dashboard
    @GetMapping("/student/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Integer studentId = (Integer) session.getAttribute("studentId");

        if (studentId == null) {
            return "redirect:/student/login";
        }

        String studentName = (String) session.getAttribute("studentName");

        model.addAttribute("studentName", studentName);

        return "student-dashboard";
    }
    // Student Profile
    @GetMapping("/student/profile")
    public String profile(HttpSession session, Model model) {

        Integer studentId =
                (Integer) session.getAttribute("studentId");

        if (studentId == null) {
            return "redirect:/student/login";
        }

        Student student =
                studentRepository.findById(studentId).orElse(null);

        if (student == null) {
            return "redirect:/student/login";
        }

        model.addAttribute("student", student);

        return "student-profile";
    }
    // Logout
    @GetMapping("/student/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
    
}

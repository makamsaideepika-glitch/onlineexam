package com.onlineexam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import com.onlineexam.Question;
import com.onlineexam.repository.QuestionRepository;
import org.springframework.web.bind.annotation.PathVariable;
import com.onlineexam.repository.ResultRepository;
@Controller
public class AdminController {
	private final QuestionRepository questionRepository;
	private final ResultRepository resultRepository;
	public AdminController(
	        QuestionRepository questionRepository,
	        ResultRepository resultRepository) {

	    this.questionRepository = questionRepository;
	    this.resultRepository = resultRepository;
	}
    // Admin Login Page
    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "admin-login";
    }

    // Admin Login
    @PostMapping("/admin/login")
    public String adminLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        if (username.equals("admin")
                && password.equals("admin123")) {

            session.setAttribute("adminLoggedIn", true);

            return "redirect:/admin/dashboard";
        }

        model.addAttribute(
                "error",
                "Invalid admin username or password!");

        return "admin-login";
    }

    // Admin Dashboard
    @GetMapping("/admin/dashboard")
    public String adminDashboard(
            HttpSession session) {

        Boolean adminLoggedIn =
                (Boolean) session.getAttribute("adminLoggedIn");

        if (adminLoggedIn == null || !adminLoggedIn) {
            return "redirect:/admin/login";
        }

        return "admin-dashboard";
    }
 // Add Question Page
    @GetMapping("/admin/questions/add")
    public String addQuestionPage(
            HttpSession session,
            Model model) {

        Boolean adminLoggedIn =
                (Boolean) session.getAttribute("adminLoggedIn");

        if (adminLoggedIn == null || !adminLoggedIn) {
            return "redirect:/admin/login";
        }

        return "add-question";
    }


    // Save Question
    @PostMapping("/admin/questions/add")
    public String addQuestion(
            @RequestParam String question_text,
            @RequestParam String option1,
            @RequestParam String option2,
            @RequestParam String option3,
            @RequestParam String option4,
            @RequestParam int correct_answer,
            HttpSession session,
            Model model) {

        Boolean adminLoggedIn =
                (Boolean) session.getAttribute("adminLoggedIn");

        if (adminLoggedIn == null || !adminLoggedIn) {
            return "redirect:/admin/login";
        }

        Question question = new Question();

        question.setQuestion_text(question_text);
        question.setOption1(option1);
        question.setOption2(option2);
        question.setOption3(option3);
        question.setOption4(option4);
        question.setCorrect_answer(correct_answer);

        questionRepository.save(question);

        model.addAttribute(
                "message",
                "Question added successfully!");

        return "add-question";
    }
    
    // View Questions
    @GetMapping("/admin/questions")
    public String viewQuestions(
            HttpSession session,
            Model model) {

        Boolean adminLoggedIn =
                (Boolean) session.getAttribute("adminLoggedIn");

        if (adminLoggedIn == null || !adminLoggedIn) {
            return "redirect:/admin/login";
        }

        model.addAttribute(
                "questions",
                questionRepository.findAll());

        return "view-questions";
    }


    // Delete Question
    @GetMapping("/admin/questions/delete/{id}")
    public String deleteQuestion(
            @PathVariable int id,
            HttpSession session) {

        Boolean adminLoggedIn =
                (Boolean) session.getAttribute("adminLoggedIn");

        if (adminLoggedIn == null || !adminLoggedIn) {
            return "redirect:/admin/login";
        }

        questionRepository.deleteById(id);

        return "redirect:/admin/questions";
    }
    
 // View Student Results
    @GetMapping("/admin/results")
    public String adminResults(
            HttpSession session,
            Model model) {

        Boolean adminLoggedIn =
                (Boolean) session.getAttribute("adminLoggedIn");

        if (adminLoggedIn == null || !adminLoggedIn) {
            return "redirect:/admin/login";
        }

        model.addAttribute(
                "results",
                resultRepository.findAll());

        return "admin-results";
    }
    // Admin Logout
    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}

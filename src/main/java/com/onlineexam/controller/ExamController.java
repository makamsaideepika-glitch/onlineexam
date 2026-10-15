package com.onlineexam.controller;

import com.onlineexam.Question;
import com.onlineexam.Result;
import com.onlineexam.repository.QuestionRepository;
import com.onlineexam.repository.ResultRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class ExamController {

    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;

    public ExamController(
            QuestionRepository questionRepository,
            ResultRepository resultRepository) {

        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;
    }

    // Display Exam
    @GetMapping("/student/exam")
    public String showExam(Model model) {

        List<Question> questions = questionRepository.findAll();

        model.addAttribute("questions", questions);

        return "exam";
    }

    // Submit Exam
    @PostMapping("/student/exam/submit")
    public String submitExam(
            @RequestParam Map<String, String> answers,
            HttpSession session,
            Model model) {

        Integer studentId =
                (Integer) session.getAttribute("studentId");

        if (studentId == null) {
            return "redirect:/student/login";
        }

        List<Question> questions =
                questionRepository.findAll();

        int score = 0;

        for (Question question : questions) {

            String key =
                    "question_" + question.getQuestion_id();

            String selectedAnswer =
                    answers.get(key);

            if (selectedAnswer != null) {

                int selectedOption =
                        Integer.parseInt(selectedAnswer);

                if (selectedOption ==
                        question.getCorrect_answer()) {

                    score++;
                }
            }
        }

        Result result = new Result();

        result.setStudentId(studentId);
        result.setScore(score);
        result.setTotalQuestions(questions.size());
        result.setExamDate(LocalDate.now());

        resultRepository.save(result);

        model.addAttribute("score", score);
        model.addAttribute(
                "totalQuestions",
                questions.size()
        );

        return "result";
    }

    // View Student Results
    @GetMapping("/student/results")
    public String viewResults(
            HttpSession session,
            Model model) {

        Integer studentId =
                (Integer) session.getAttribute("studentId");

        if (studentId == null) {
            return "redirect:/student/login";
        }

        List<Result> results =
                resultRepository.findByStudentId(studentId);

        model.addAttribute("results", results);

        return "student-results";
    }
}
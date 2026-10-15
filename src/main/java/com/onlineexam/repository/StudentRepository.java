package com.onlineexam.repository;

import com.onlineexam.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByUsername(String username);
}
package com.onlineexam.repository;

import com.onlineexam.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Integer> {

    List<Result> findByStudentId(int studentId);
}
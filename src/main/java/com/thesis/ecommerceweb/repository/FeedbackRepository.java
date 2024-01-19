package com.thesis.ecommerceweb.repository;

import com.thesis.ecommerceweb.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    int countByPid(int pid);
    List<Feedback> findAllByPid(int pid);
    Feedback findFeedbackByPidAndUsername(int pid, String username);
}

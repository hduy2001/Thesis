package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.model.Feedback;
import com.thesis.ecommerceweb.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    FeedbackRepository feedbackRepository;

    public void addFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedBack(int pid) {
        return feedbackRepository.findAllByPid(pid);
    }

    public int countRating(int pid) {
        return feedbackRepository.countByPid(pid);
    }
}

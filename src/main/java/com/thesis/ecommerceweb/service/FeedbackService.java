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

    public void addFeedback(int pid, String username, double rating, long timestamp) {
        Feedback existFeedback = feedbackRepository.findFeedbackByPidAndUsername(pid, username);
        if (existFeedback != null) {
            existFeedback.setRating(rating);
            existFeedback.setTimeStamp(timestamp);
            feedbackRepository.save(existFeedback);
        } else {
            Feedback feedback = new Feedback();
            feedback.setPid(pid);
            feedback.setUsername(username);
            feedback.setRating(rating);
            feedback.setTimeStamp(timestamp);
            feedbackRepository.save(feedback);
        }
    }

    public List<Feedback> getAllFeedBack(int pid) {
        return feedbackRepository.findAllByPid(pid);
    }

    public int countRating(int pid) {
        return feedbackRepository.countByPid(pid);
    }
}

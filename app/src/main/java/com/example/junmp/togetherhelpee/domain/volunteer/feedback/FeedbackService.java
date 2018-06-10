package com.example.junmp.togetherhelpee.domain.volunteer.feedback;

import com.example.junmp.togetherhelpee.common.util.network.RetrofitBuilder;

import java.io.IOException;

public class FeedbackService {
    private FeedbackRepository feedbackRepository = RetrofitBuilder.builder().create(FeedbackRepository.class);

    public void save(FeedbackForm form) {
        try {
            this.feedbackRepository.save(form).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

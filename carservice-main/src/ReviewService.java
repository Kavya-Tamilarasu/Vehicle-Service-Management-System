package com.example.usermanagement.service;

import com.example.usermanagement.model.Review;
import com.example.usermanagement.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repo;

    public List<Review> getAllReviews() { return repo.findAll(); }

    public Optional<Review> getReviewById(Long id) { return repo.findById(id); }

    public List<Review> getReviewsByUserId(Long userId) { return repo.findByUser_Id(userId); }

    public List<Review> getReviewsByServiceRecordId(Long srId) { return repo.findByServiceRecord_Id(srId); }

    public Double getAverageRating() { return repo.findAverageRating(); }

    public Review saveReview(Review r) {
        if (r.getDateSubmitted() == null) r.setDateSubmitted(LocalDate.now());
        return repo.save(r);
    }

    public Review updateReview(Long id, Review updated) {
        Review r = repo.findById(id).orElseThrow();
        r.setRating(updated.getRating());
        r.setComment(updated.getComment());
        if (updated.getUser() != null) r.setUser(updated.getUser());
        if (updated.getServiceRecord() != null) r.setServiceRecord(updated.getServiceRecord());
        return repo.save(r);
    }

    public void deleteReview(Long id) { repo.deleteById(id); }
}

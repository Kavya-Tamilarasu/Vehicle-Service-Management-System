package com.example.usermanagement.repository;

import com.example.usermanagement.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUser_Id(Long userId);
    List<Review> findByServiceRecord_Id(Long serviceRecordId);
    void deleteByServiceRecord_Id(Long serviceRecordId);

    @Query("SELECT AVG(r.rating) FROM Review r")
    Double findAverageRating();
}

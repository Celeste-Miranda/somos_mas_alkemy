package com.alkemy.java.repository;

import com.alkemy.java.model.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial,Long> {

}

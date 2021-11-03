package com.alkemy.java.repository;

import com.alkemy.java.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    News findByName(String name);

}

package com.alkemy.java.repository;

import com.alkemy.java.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select * from comments\n" +
            "where news_id = ?1 ;"
            , nativeQuery = true)
    List<Comment> getCommentsByIdNews(Long id);

    @Query("SELECT c from Comment c order by c.createDate asc")
    List<Comment> findAllByOrder();
}

package com.alkemy.java.repository;

import com.alkemy.java.model.Member;
import com.alkemy.java.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByName(String name);

}

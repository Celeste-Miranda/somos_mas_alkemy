package com.alkemy.java.repository;


import com.alkemy.java.model.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Long> {


   
    @Query(value = "SELECT MAX(s.numberOrder) FROM Slide s")
    int maxOrder();
    
    Slide findByNumberOrder(int numberOrder);

    Slide getById(Long id);


    @Query("SELECT m FROM Slide m JOIN m.organizationId n WHERE n.id LIKE :id ORDER BY m.numberOrder")
    List<Slide> findAllByOrganizationId(@Param("id") Long id);


}

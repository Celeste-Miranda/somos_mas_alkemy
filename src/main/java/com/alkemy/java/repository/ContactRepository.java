package com.alkemy.java.repository;

import com.alkemy.java.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository <Contact,Long> {
    Optional<Contact> findByEmail(String email);
}

package com.alkemy.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id_user=?")
@Where(clause = "deleted=false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Field first name should not be null or empty!")
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "Field last name should not be null or empty!")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "Field email should not be null or empty!")
    @Column(name = "email", unique = true)
    @Email
    private String email;

    @NotNull(message = "Field password should not be null or empty!")
    @Column(name = "password")
    private String password;

    @Column(name = "photo")
    private String photo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "deleted")
    private Boolean deleted = Boolean.FALSE;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "last_update")
    private Date lastUpdate;


    public User(String firstName, String lastName, String email, String password, String photo, Role role, Date creationDate, Date lastUpdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.role = role;
        this.creationDate = creationDate;
        this.lastUpdate = lastUpdate;
    }
}

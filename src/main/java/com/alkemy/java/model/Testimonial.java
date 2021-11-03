package com.alkemy.java.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "testimonials")
@SQLDelete(sql = "UPDATE testimonials SET deleted = true where id=?")
@Where(clause = "deleted=false")
public class Testimonial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Field name should not be null or empty")
    private String name;

    private String image;

    private String content;

    private Boolean deleted = Boolean.FALSE;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "last_update")
    private Date updateDate;

}

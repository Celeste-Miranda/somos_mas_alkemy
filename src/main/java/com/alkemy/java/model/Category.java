package com.alkemy.java.model;


import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "categories")
@SQLDelete(sql = "UPDATE categories SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Data
@NoArgsConstructor @AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Field name should not be null or empty")
    private String name;

    private String description;

    private String image;

    private boolean deleted = Boolean.FALSE;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "last_update")
    private Date lastUpdate;

}
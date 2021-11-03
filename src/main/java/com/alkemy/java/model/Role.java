package com.alkemy.java.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;


import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Field name should not be null or empty")
    private String name;

    private String description;

    @Column(name = "creation_date")
    private Date createDate;

    @Column(name = "last_update")
    private Date updateDate;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<User> userList;

    public Role(String name, String description, Date createDate, Date updateDate) {
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
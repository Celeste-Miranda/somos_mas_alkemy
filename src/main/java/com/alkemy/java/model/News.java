package com.alkemy.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "news")
@SQLDelete(sql = "UPDATE news SET deleted = true WHERE id=?")
@Where(clause = " deleted = false")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Field name should not be null or empty")
    @Column(length = 100)
    private String name;

    @NotNull(message = "Field content should not be null or empty")
    private String content;

    @NotNull(message = "Field image should not be null or empty")
    private String image;

    @Column(name = "creation_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    private Boolean deleted = Boolean.FALSE;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id")
    private Category category;
}

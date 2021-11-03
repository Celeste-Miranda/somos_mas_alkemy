package com.alkemy.java.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor  @AllArgsConstructor
@Table(name = "slides")
@SQLDelete(sql = "UPDATE slides SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Slide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{slide.image.empty}")
    private String imageUrl;

    @NotEmpty(message = "{slide.text.empty}")
    private String text;

    @NotEmpty(message = "{slide.numorder.empty}")
    @Column(name = "number_order")
    private int numberOrder;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="organization_id")
    private Organization organizationId;

    private boolean deleted = Boolean.FALSE;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "last_update")
    private Date lastUpdate;

}

package com.alkemy.java.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE members SET deleted = true WHERE id=?")
@Where(clause = " deleted = false ")
@Table(name = "Members")
@Builder
public class Member{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "This field cannot be null or empty!")
    private String name;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "instagram_url")
    private String instagramUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @NotEmpty(message = "This field cannot be null or empty!")
    private String image;

    private String description;

    @Column(name = "created_Date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @Column(name = "update_date")
    @Temporal(TemporalType.DATE)
    private Date updateDate;

    private boolean deleted = Boolean.FALSE;
}

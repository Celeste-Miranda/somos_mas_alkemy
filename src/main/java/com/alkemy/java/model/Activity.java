package com.alkemy.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "activities")
@SQLDelete(sql = "UPDATE activities SET deleted = true WHERE id=?")
@Where(clause = " deleted = false ")
public class Activity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Field name should not be null or empty")
    private String name;

    @Lob
    @NotBlank (message = "Field content should not be null or empty")
    private String content;

    @NotBlank(message = "Field image should not be null or empty")
    private String image;

    @Column (name = "create_date")
    private Date createDate;

    @Column (name = "update_date")
    private Date updateDate;

    private Boolean deleted = Boolean.FALSE;

    public Activity(String name, String content, String image, Date createDate, Date updateDate) {
        this.name = name;
        this.content = content;
        this.image = image;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}

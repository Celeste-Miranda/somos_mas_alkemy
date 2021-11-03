package com.alkemy.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotEmpty(message = "{error.comment.body.empty}")
    @Size(max = 255, message = "{error.comment.body.limit}")
    private String body;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false, nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update", nullable = false)
    private Date lastUpdate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

}

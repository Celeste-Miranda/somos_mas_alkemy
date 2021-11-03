package com.alkemy.java.model;

import com.alkemy.java.dto.ContactRequestDto;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "contacts")
@SQLDelete(sql = "UPDATE contacts SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank (message = "Field name should not be null or empty")
    private String name;

    @NotBlank(message = "Field email should not be null or empty")
    @Email
    private String email;

    @Lob
    @NotBlank(message = "Field message should not be null or empty")
    private String message;

    @NotBlank (message = "Field phone should not be null or empty")
    private String phone;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "update_date")
    private Date updateDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "deleted_at")
    private Date deletedAt;

    private Boolean deleted = Boolean.FALSE;


    public static Contact fromDtoToContact(ContactRequestDto contactDto) {
        return Contact.builder()
                .name(contactDto.getName())
                .email(contactDto.getEmail())
                .message(contactDto.getMessage())
                .phone(contactDto.getPhone())
                .createdDate(new Date())
                .updateDate(new Date())
                .build();
    }
}

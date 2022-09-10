package com.sayed.enrolment.file;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class AppFile {
    @Id
    @Column(nullable = false)
    private String id;
    private String url;
    private Timestamp date;
    private String parentId;
    private Integer size;
    private final String type = "FILE";
}

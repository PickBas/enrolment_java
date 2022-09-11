package com.sayed.enrolment.file;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp date;
    private String parentId;
    private Integer size;
    private final String type = "FILE";
    private String children = null;
}

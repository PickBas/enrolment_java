package com.sayed.enrolment.folder;

import com.sayed.enrolment.file.AppFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class Folder {
    @Id
    @Column(nullable = false)
    private String id;
    private String url;
    private Timestamp date;
    private String parentId;
    private Integer size;
    private final String type = "FOLDER";
    @OneToMany
    private List<AppFile> children;

    public void addChild(AppFile file) {
        this.size += file.getSize();
        children.add(file);
    }
}
